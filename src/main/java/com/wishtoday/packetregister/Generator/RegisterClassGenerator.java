package com.wishtoday.packetregister.Generator;

import com.wishtoday.packetregister.ClassLoader.SimpleClassLoader;
import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Data.Storage.FieldStorage;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.PacketState;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;

import static org.objectweb.asm.Opcodes.*;

@Log4j2
public class RegisterClassGenerator {
    private final String className;
    private final String packageName;
    private String C2SRegisterMethod = "C2SRegister";
    private String S2CRegisterMethod = "S2CRegister";
    private String allRegisterMethods = "register";
    private ClassWriter cw;
    private final int PUBSTA = ACC_PUBLIC + ACC_STATIC;
    public RegisterClassGenerator(String className
            , String packageName
    , String C2SRegisterMethod
    , String S2CRegisterMethod
    , String allRegisterMethods) {
        this.className = className;
        this.packageName = packageName;
        this.C2SRegisterMethod = C2SRegisterMethod;
        this.S2CRegisterMethod = S2CRegisterMethod;
        this.allRegisterMethods = allRegisterMethods;
    }

    public RegisterClassGenerator(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }
    public void start() {
        this.generateClass();
        this.generateS2CRegister();
        this.generateC2SRegister();
        this.generateAllRegister();
        this.save();
    }
    private void save() {
        this.cw.visitEnd();
        byte[] array = cw.toByteArray();
        Class<?> clazz = new SimpleClassLoader().loadClass(getThisClassInternalName(), array);
        try {
            clazz.getMethod(this.allRegisterMethods).invoke(null);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            log.error("load class error", e);
        }
    }
    public void generateClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        cw.visit(Opcodes.V21, ACC_PUBLIC
                , getCompleteName(),null
                , "java/lang/Object" , null);
        this.cw = cw;
    }
    private void generateAllRegister() {
        ClassWriter cw = this.cw;
        MethodVisitor mv = cw.visitMethod(PUBSTA, this.allRegisterMethods
                , "()V", null, null);
        mv.visitCode();
        mv.visitMethodInsn(INVOKESTATIC, getCompleteName(), "S2CRegister", "()V", false);
        mv.visitMethodInsn(INVOKESTATIC, getCompleteName(), "C2SRegister", "()V", false);
        mv.visitInsn(RETURN);
        mv.visitEnd();
    }
    private void generateS2CRegister() {
        ClassWriter cw = this.cw;
        MethodVisitor mv = cw.visitMethod(PUBSTA, this.S2CRegisterMethod
                , "()V", null, null);
        mv.visitCode();
        Collection<PacketClassInfo<CustomPayload>> info = PacketClassManager.getInstance()
                .getAllPacketClassInfo();
        for (PacketClassInfo<CustomPayload> classInfo : info) {
            if (classInfo.getState() != PacketState.S2C) continue;
            FieldStorage idStorage = classInfo.getRegisterInfo().getID();
            FieldStorage codecStorage = classInfo.getRegisterInfo().getCODEC();
            String payloadRegisterInternal = Type.getInternalName(PayloadTypeRegistry.class);
            String payloadRegisterDesc = Type.getDescriptor(PayloadTypeRegistry.class);
            mv.visitMethodInsn(INVOKESTATIC ,payloadRegisterInternal, "playS2C", "()" + payloadRegisterDesc, true);
            mv.visitFieldInsn(GETSTATIC, idStorage.getClassPath(), idStorage.getElementName(), Type.getDescriptor(CustomPayload.Id.class));
            mv.visitFieldInsn(GETSTATIC, codecStorage.getClassPath(), codecStorage.getElementName(), Type.getDescriptor(PacketCodec.class));
            mv.visitMethodInsn(INVOKEINTERFACE, payloadRegisterInternal, "register", getRegisterDesc(), true);
            mv.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
            mv.visitLdcInsn("S2C注册完成.");
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println", "(Ljava/lang/String;)V", false);
        }
        mv.visitMaxs(0, 0);
        mv.visitInsn(RETURN);
        mv.visitEnd();
    }
    private void generateC2SRegister() {
        ClassWriter cw = this.cw;
        MethodVisitor mv = cw.visitMethod(PUBSTA, this.C2SRegisterMethod
                , "()V", null, null);
        mv.visitCode();
        Collection<PacketClassInfo<CustomPayload>> info = PacketClassManager.getInstance()
                .getAllPacketClassInfo();
        for (PacketClassInfo<CustomPayload> classInfo : info) {
            if (classInfo.getState() != PacketState.C2S) continue;
            FieldStorage idStorage = classInfo.getRegisterInfo().getID();
            FieldStorage codecStorage = classInfo.getRegisterInfo().getCODEC();
            String payloadRegisterInternal = Type.getInternalName(PayloadTypeRegistry.class);
            String payloadRegisterDesc = Type.getDescriptor(PayloadTypeRegistry.class);
            mv.visitMethodInsn(INVOKESTATIC ,payloadRegisterInternal, "playC2S", "()" + payloadRegisterDesc, true);
            mv.visitFieldInsn(GETSTATIC, idStorage.getClassPath(), idStorage.getElementName(), Type.getDescriptor(CustomPayload.Id.class));
            mv.visitFieldInsn(GETSTATIC, codecStorage.getClassPath(), codecStorage.getElementName(), Type.getDescriptor(PacketCodec.class));
            mv.visitMethodInsn(INVOKEINTERFACE, payloadRegisterInternal, "register", getRegisterDesc(), true);
        }
        mv.visitMaxs(0, 0);
        mv.visitInsn(RETURN);
        mv.visitEnd();
    }
    private String getRegisterDesc() {
        return "(" +
                Type.getDescriptor(CustomPayload.Id.class) +
                Type.getDescriptor(PacketCodec.class) +
                ")" +
                Type.getDescriptor(CustomPayload.Type.class);
    }
    private String getCompleteName() {
        return String.format("%s.%s", packageName, className).replace(".","/");
    }
    private String getThisClassInternalName() {
        //return this.getCompleteName();
        return String.format("%s.%s", packageName, className);
    }
}
