package com.wishtoday.packetregister.Visitors.AnnotationVisitor.Class;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.PacketState;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.AnnotationPacketClassVisitor;
import com.wishtoday.packetregister.Visitors.ClassVisitor.PacketClassVisitor;

public class PacketVisitor extends AnnotationPacketClassVisitor {
    public PacketVisitor(String classPath
            , PacketClassVisitor classVisitor) {
        super(classPath ,classVisitor);
    }
    private PacketState state;

    @Override
    public void visitEnum(String name
            , String descriptor
            , String value) {
        if (!"value".equals(name)) return;
        this.state = PacketState.valueOf(value);
        this.classVisitor.setState(state);
    }

    @Override
    public void visitEnd() {
        PacketClassManager.getInstance()
                .computeIfAbsent(this.classPath, s -> new PacketClassInfo<>())
                .setState(this.state);
    }
}
