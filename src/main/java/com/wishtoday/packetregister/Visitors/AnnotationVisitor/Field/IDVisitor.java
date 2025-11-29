package com.wishtoday.packetregister.Visitors.AnnotationVisitor.Field;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Data.RegisterInfo;
import com.wishtoday.packetregister.Data.Storage.FieldStorage;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.AnnotationClassPathVisitor;
import net.minecraft.network.packet.CustomPayload;

public class IDVisitor extends AnnotationClassPathVisitor {
    public IDVisitor(String classPath,
            String elementName) {
        super(classPath, elementName);
    }

    @Override
    public void visitEnd() {
        RegisterInfo info = PacketClassManager
                .getInstance()
                .computeIfAbsent(classPath, s -> new PacketClassInfo())
                .getRegisterInfo();
        info.setID(FieldStorage.builder()
                .classPath(this.classPath)
                .elementName(this.elementName)
                .build());
        super.visitEnd();
    }
}
