package com.wishtoday.packetregister.Visitors.AnnotationVisitor;

import lombok.Getter;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.Opcodes;

@Getter
public abstract class AnnotationClassPathVisitor extends AnnotationVisitor {
    protected String classPath;
    protected String elementName;
    public AnnotationClassPathVisitor(String classPath, String elementName) {
        super(Opcodes.ASM9);
        this.classPath = classPath;
        this.elementName = elementName;
    }
}
