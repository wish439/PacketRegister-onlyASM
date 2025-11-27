/*
package com.wishtoday.packetregister.Visitors.AnnotationVisitor.Class;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.ClassUtil;
import com.wishtoday.packetregister.Visitors.AnnotationVisitor.AnnotationClassPathVisitor;
import lombok.extern.log4j.Log4j2;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

@Log4j2
public class EmptyCodecVisitor extends AnnotationClassPathVisitor {
    public EmptyCodecVisitor(String classPath) {
        super(classPath);
    }

    @Override
    public void visitEnd() {
        PacketCodec<PacketByteBuf, CustomPayload> codec = getCodecFromClazz();
        if (codec == null) {
            log.warn("{} register emptyCodec failed", this.classPath);
            return;
        }
        PacketClassManager.getInstance()
                .computeIfAbsent(this.classPath, s -> new PacketClassInfo<>())
                .setCODEC(codec);
    }
    @SuppressWarnings("unchecked")
    @Nullable
    private PacketCodec<PacketByteBuf, CustomPayload> getCodecFromClazz() {
        Class<? extends CustomPayload> clazz = (Class<? extends CustomPayload>) ClassUtil.getClass(classPath);
        if (clazz == null) return null;
        Constructor<? extends CustomPayload> constructor;
        try {
            constructor = clazz.getConstructor();
            return PacketCodec.unit(constructor.newInstance());
        } catch (NoSuchMethodException e) {
            log.error("class {} has no no args constructor {}", classPath, e);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            log.error("class {} create instance throw {}", classPath, e);
        }
        return null;
    }
}
*/
