package com.wishtoday.packetregister.Visitors.AnnotationVisitor;

import com.wishtoday.packetregister.Visitors.ClassVisitor.PacketClassVisitor;

public abstract class AnnotationPacketClassVisitor extends AnnotationClassPathVisitor {
    protected PacketClassVisitor classVisitor;
    public AnnotationPacketClassVisitor(String classPath
            ,  PacketClassVisitor packetClassVisitor) {
        super(classPath,  null);
        this.classVisitor = packetClassVisitor;
    }
}
