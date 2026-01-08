package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Data.Storage.FieldStorage;
import com.wishtoday.packetregister.Exception.ClassLoadException;
import com.wishtoday.packetregister.Generator.ClassGenerator;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.DescUtils;
import com.wishtoday.packetregister.Util.PacketState;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.minecraft.network.packet.CustomPayload;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import static org.objectweb.asm.Opcodes.*;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.RETURN;

import static com.wishtoday.packetregister.Packetregister.log;

public abstract class AbstractHandlerRegister extends ClassGenerator
        implements HandlerRegister {

    protected List<PacketClassInfo> infos;
    protected String registerMethodName;

    protected AbstractHandlerRegister(
            String className
            , String packageName
            , List<PacketClassInfo> infos
            , String registerMethodName) {
        super(className, packageName);
        this.infos = infos;
        this.registerMethodName = registerMethodName;
    }

    protected void generateLambdaClasses() {
        int i = 0;
        ClassWriter cw = this.cw;
        for (PacketClassInfo info : this.infos) {
            MethodVisitor mv = cw.visitMethod(
                    ACC_PRIVATE + ACC_STATIC + ACC_SYNTHETIC
                    , this.getLambdaMethodName(i)
                    , this.getLambdaMethodDesc(info)
                    , null, null);
            mv.visitCode();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitVarInsn(ALOAD, 1);
            mv.visitMethodInsn(INVOKESTATIC
                    , info.getMethodStorage().getClassPath()
                    , info.getMethodStorage().getElementName()
                    , this.getLambdaMethodDesc(info)
                    , false);
            mv.visitInsn(RETURN);
            mv.visitMaxs(0, 0);
            mv.visitEnd();
            i++;
        }
    }

    @Override
    protected void load(byte[] data) {

        Class<?> aClass = loader.loadClass(getThisClassInternalName(), data);
        try {
            aClass.getMethod(this.registerMethodName).invoke(null);
        } catch (NoSuchMethodException e) {
            log.error("No such method: {}", this.registerMethodName);
            throw new ClassLoadException(e);
        } catch (InvocationTargetException e) {
            log.error("Error invoking method: {}", this.registerMethodName);
            throw new ClassLoadException(e);
        } catch (IllegalAccessException e) {
            log.error("Illegal access to method: {}", this.registerMethodName);
            throw new ClassLoadException(e);
        }
    }

    @Override
    public void generate() {
        this.registerHandlers();
        this.cw.visitEnd();
        this.load(this.cw.toByteArray());
    }

    @Override
    public void registerHandlers() {
        this.generateLambdaClasses();
        Handle bsm = new Handle(
                H_INVOKESTATIC,
                "java/lang/invoke/LambdaMetafactory",
                "metafactory",
                "(Ljava/lang/invoke/MethodHandles$Lookup;"
                        + "Ljava/lang/String;"
                        + "Ljava/lang/invoke/MethodType;"
                        + "Ljava/lang/invoke/MethodType;"
                        + "Ljava/lang/invoke/MethodHandle;"
                        + "Ljava/lang/invoke/MethodType;"
                        + ")Ljava/lang/invoke/CallSite;",
                false);
        MethodVisitor mv = cw.visitMethod(ACC_PUBLIC + ACC_STATIC, this.registerMethodName, "()V", null, null);
        int i = 0;
        mv.visitCode();
        for (PacketClassInfo info : this.infos) {
            PacketState state = info.getState();
            if (state != this.getProcessState()) continue;
            FieldStorage id = info.getRegisterInfo().getID();
            mv.visitFieldInsn(
                    GETSTATIC
                    , id.getClassPath()
                    , id.getElementName()
                    , Type.getDescriptor(CustomPayload.Id.class));
            Handle handle = new Handle(
                    H_INVOKESTATIC
                    , this.getCompleteName()
                    , this.getLambdaMethodName(i)
                    , this.getLambdaMethodDesc(info)
                    , false);
            mv.visitInvokeDynamicInsn(
                    "receive"
                    , String.format("()%s"
                            , this.getPlayPayloadHandlerDesc(state))
                    , bsm
                    , Type.getMethodType(Type.VOID_TYPE, Type.getType(CustomPayload.class), Type.getType(state.getClassDesc()))
                    , handle
                    , Type.getMethodType(Type.VOID_TYPE, Type.getType(info.getClazz()), Type.getType(state.getClassDesc())));
            mv.visitMethodInsn(
                    INVOKESTATIC
                    , DescUtils.checkInternal(state.getPlayNetworkDesc())
                    , "registerGlobalReceiver"
                    , this.getRegisterMethodDesc()
                    , false);
            log.info("generated {}", info.getClazz().getName());
            i++;
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    private String getPlayPayloadHandlerDesc(PacketState state) {
        return String.format("L%s%s;", state.getPlayNetworkDesc(), "$PlayPayloadHandler");
    }

    protected abstract PacketState getProcessState();

    protected String getLambdaMethodName(int num) {
        return "lambda$" + num;
    }

    protected String getLambdaMethodDesc(PacketClassInfo info) {
        return String.format("(%s%s)V"
                , Type.getDescriptor(info.getClazz())
                , info.getState().getClassDesc());
    }

    protected abstract String getRegisterMethodDesc();

    //Client should register Server Payload
    //The single game can't invoke this method again,so we must register Server side.
    public static class AbstractHandlerRegisterFactory {
        public static AbstractHandlerRegister create(
                EnvType envType) {
            PacketClassManager instance = PacketClassManager.getInstance();
            ServerHandlerRegister register = new ServerHandlerRegister("ServerHandlerRegister"
                    , "com.wishtoday.packetregister.GeneratorData"
                    , instance.getC2SPacketClassInfoList()
                    , "serverPacketRegister");
            if (envType == EnvType.SERVER) {
                return register;
//                return new ServerHandlerRegister("ServerHandlerRegister", "com.wishtoday", instance.getC2SPacketClassInfoList(), "serverPacketRegister");
            }
            register.generate();
            return new ClientHandlerRegister("ClientHandlerRegister"
                    , "com.wishtoday.packetregister.GeneratorData"
                    , instance.getS2CPacketClassInfoList()
                    , "clientPacketRegister");
        }
    }

    @Override
    public String toString() {
        return this.getProcessState().name();
    }
}
