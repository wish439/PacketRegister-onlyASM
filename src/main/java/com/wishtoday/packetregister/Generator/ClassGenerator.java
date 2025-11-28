package com.wishtoday.packetregister.Generator;

import org.objectweb.asm.ClassWriter;

import static org.objectweb.asm.Opcodes.*;

public abstract class ClassGenerator {
    protected final String className;
    protected final String packageName;
    protected ClassWriter cw;
    protected ClassGenerator(String className, String packageName) {
        this.className = className;
        this.packageName = packageName;
    }
    protected void generateClass() {
        ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS | ClassWriter.COMPUTE_FRAMES);
        cw.visit(V21, ACC_PUBLIC + ACC_STATIC
                , this.getCompleteName(), null
                , "java/lang/Object", null);
        this.cw = cw;
    }
    protected String getCompleteName() {
        return this.getThisClassInternalName().replace(".","/");
    }
    protected String getThisClassInternalName() {
        return String.format("%s.%s", packageName, className);
    }
}
