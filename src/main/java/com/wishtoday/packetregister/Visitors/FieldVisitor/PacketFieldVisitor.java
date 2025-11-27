package com.wishtoday.packetregister.Visitors.FieldVisitor;

import com.wishtoday.Annotation.Codec;
import com.wishtoday.Annotation.ID;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.Field.CodecVisitor;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.Field.IDVisitor;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

public class PacketFieldVisitor extends org.objectweb.asm.FieldVisitor {
    private final String fieldName;
    private final String className;

    public PacketFieldVisitor(String fieldName, String className) {
        super(Opcodes.ASM9);
        this.fieldName = fieldName;
        this.className = className;
    }

    @Override
    public AnnotationVisitor visitAnnotation(
            String descriptor
            , boolean visible) {
        if (descriptor.equals(Type.getDescriptor(ID.class))) {
            return new IDVisitor(this.className, this.fieldName);
        }
        if (descriptor.equals(Type.getDescriptor(Codec.class))) {
            return new CodecVisitor(this.className, this.fieldName);
        }
        return super.visitAnnotation(descriptor, visible);
    }
}
