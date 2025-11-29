package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Data.Storage.FieldStorage;
import com.wishtoday.packetregister.Util.PacketState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Handle;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

import java.util.List;

import static org.objectweb.asm.Opcodes.*;

public class ServerHandlerRegister extends AbstractHandlerRegister {
    protected ServerHandlerRegister(
            String className
            , String packageName
            , List<PacketClassInfo> packetClassInfoList
            , String registerMethodName) {
        super(className, packageName, packetClassInfoList, registerMethodName);
    }

    @Override
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
    protected String getLambdaMethodDesc(PacketClassInfo info) {
        return String.format("(%s%s)V"
                , Type.getDescriptor(info.getClazz())
                , Type.getDescriptor(ServerPlayNetworking.Context.class));
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
            if (info.getState() == PacketState.S2C) continue;
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
//                    , String.format("(%s%s)%s"
//                            , Type.getDescriptor(info.getClazz())
//                            , Type.getDescriptor(CustomPayload.class)
//                            , Type.getDescriptor(ServerPlayNetworking.Context.class)
                            , Type.getDescriptor(ServerPlayNetworking.PlayPayloadHandler.class))
                    , bsm
                    , Type.getMethodType(Type.VOID_TYPE, Type.getType(CustomPayload.class), Type.getType(ServerPlayNetworking.Context.class))
                    , handle
                    , Type.getMethodType(Type.VOID_TYPE, Type.getType(info.getClazz()), Type.getType(ServerPlayNetworking.Context.class)));
//            mv.visitVarInsn(ASTORE, 0);
            mv.visitMethodInsn(
                    INVOKESTATIC
                    , Type.getInternalName(ServerPlayNetworking.class)
                    , "registerGlobalReceiver"
                    , this.getRegisterMethodDesc()
                    , false);
            i++;
        }
        mv.visitInsn(RETURN);
        mv.visitMaxs(0, 0);
        mv.visitEnd();
    }

    @Override
    protected String getRegisterMethodDesc() {
        return String.format("(%s%s)Z"
                , Type.getDescriptor(CustomPayload.Id.class)
                , Type.getDescriptor(ServerPlayNetworking.PlayPayloadHandler.class));
    }
}
