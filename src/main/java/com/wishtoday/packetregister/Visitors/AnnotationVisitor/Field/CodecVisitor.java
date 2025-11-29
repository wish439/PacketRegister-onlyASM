package com.wishtoday.packetregister.Visitors.AnnotationVisitor.Field;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Data.RegisterInfo;
import com.wishtoday.packetregister.Data.Storage.FieldStorage;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.AnnotationClassPathVisitor;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class CodecVisitor extends AnnotationClassPathVisitor {

    public CodecVisitor(
            String path,
            String elementName) {
        super(path,  elementName);
    }

    @Override
    public void visitEnd() {
        RegisterInfo info = PacketClassManager.getInstance()
                .computeIfAbsent(this.classPath, e -> new PacketClassInfo())
                .getRegisterInfo();
        info.setCODEC(FieldStorage.builder()
                .classPath(this.classPath)
                .elementName(this.elementName)
                .build());
        super.visitEnd();
    }
}
