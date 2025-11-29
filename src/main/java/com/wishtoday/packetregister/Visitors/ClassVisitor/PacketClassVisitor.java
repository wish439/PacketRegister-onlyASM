package com.wishtoday.packetregister.Visitors.ClassVisitor;

import com.wishtoday.Annotation.Packet;
import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.ClassUtil;
import com.wishtoday.packetregister.Util.DescUtils;
import com.wishtoday.packetregister.Util.PacketState;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.Class.PacketVisitor;
import com.wishtoday.packetregister.Visitors.FieldVisitor.PacketFieldVisitor;
import com.wishtoday.packetregister.Visitors.MethodVisitor.PacketHandlerVisitor;
import lombok.Setter;
import net.minecraft.network.packet.CustomPayload;
import org.objectweb.asm.*;

public class PacketClassVisitor extends ClassVisitor {
    public PacketClassVisitor() {
        super(Opcodes.ASM9);
    }
    private String classPath;
    @Setter
    private PacketState state;

    @Override
    public void visit(int version
            , int access
            , String name
            , String signature
            , String superName
            , String[] interfaces) {
        this.classPath = name.replace("/", ".");
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public FieldVisitor visitField(int access
            , String name
            , String descriptor
            , String signature
            , Object value) {
        return new PacketFieldVisitor(name, classPath);
    }

    @Override
    public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
        if (descriptor.equals(Type.getDescriptor(Packet.class))) {
            return new PacketVisitor(this.classPath, this);
        }
        /*if (descriptor.equals(Type.getDescriptor(EmptyCodec.class))) {
            return new EmptyCodecVisitor(this.classPath);
        }*/
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public MethodVisitor visitMethod(
            int access
            , String name
            , String descriptor
            , String signature
            , String[] exceptions) {
        return new PacketHandlerVisitor(this.classPath, name, this.state);
    }
    @SuppressWarnings("unchecked")
    @Override
    public void visitEnd() {
        if (state == null) return;
        PacketClassManager.getInstance()
                .computeIfAbsent(this.classPath, k -> new PacketClassInfo())
                        .setClazz((Class<? extends CustomPayload>) ClassUtil.getClass(classPath));
        super.visitEnd();
    }
}
