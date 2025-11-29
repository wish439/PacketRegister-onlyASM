package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.ClassLoader.SimpleClassLoader;
import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Exception.ClassLoadException;
import com.wishtoday.packetregister.Generator.ClassGenerator;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.math.BlockPos;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

@Log4j2
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

    protected abstract void generateLambdaClasses();

    protected void load(byte[] data) {

        Class<?> aClass = loader.loadClass(getThisClassInternalName(), data);
        try {
            /*ClassLoader loader = BlockPos.class.getClassLoader();
            Method method = ClassLoader.class.getDeclaredMethod("defineClass", String.class, byte[].class, int.class, int.class);
            Object o = method.invoke(loader, this.getThisClassInternalName(), data, 0, data.length);
            ((Class<?>)o).getMethod(this.registerMethodName).invoke(null);*/
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

    public void generate() {
        this.registerHandlers();
        this.cw.visitEnd();
        this.load(this.cw.toByteArray());
    }

    protected String getLambdaMethodName(int num) {
        return "lambda$" + num;
    }

    protected abstract String getLambdaMethodDesc(PacketClassInfo info);

    protected abstract String getRegisterMethodDesc();

    public static class AbstractHandlerRegisterFactory {
        public static AbstractHandlerRegister create(
                EnvType envType) {
            PacketClassManager instance = PacketClassManager.getInstance();
            if (envType == EnvType.SERVER) {
                return new ServerHandlerRegister("ServerHandlerRegister", "com.wishtoday.packetregister.GeneratorData", instance.getC2SPacketClassInfoList(), "serverPacketRegister");
//                return new ServerHandlerRegister("ServerHandlerRegister", "com.wishtoday", instance.getC2SPacketClassInfoList(), "serverPacketRegister");
            }
            return null;
            //return new ClientHandlerRegister("ClientHandlerRegister", "com.wishtoday.packetregister.GeneratorData", instance.getS2CPacketClassInfoList(), "clientPacketRegister");
        }
    }
}
