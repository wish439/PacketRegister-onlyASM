package com.wishtoday.packetregister.Visitors.MethodVisitor;

import com.wishtoday.Annotation.Handler;
import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Data.Storage.MethodStorage;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.ClassUtil;
import com.wishtoday.packetregister.Util.MethodGetter;
import com.wishtoday.packetregister.Util.PacketState;
import net.fabricmc.loader.api.FabricLoader;
import org.jetbrains.annotations.NotNull;
import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;

import java.lang.reflect.Method;

public class PacketHandlerVisitor extends MethodVisitor {
    private final String classPath;
    private final String methodName;
    private final PacketState state;
    private boolean isHandler = false;

    public PacketHandlerVisitor(@NotNull String classPath
            ,@NotNull String methodName
            ,@NotNull PacketState state) {
        super(Opcodes.ASM9);
        this.classPath = classPath;
        this.methodName = methodName;
        this.state = state;
    }

    @Override
    public AnnotationVisitor visitAnnotation(
            String descriptor
            , boolean visible) {
        if (Type.getDescriptor(Handler.class).equals(descriptor)) {
            isHandler = true;
        }
        return super.visitAnnotation(descriptor, visible);
    }

    @Override
    public void visitEnd() {
        if (!isHandler) return;
        PacketClassManager.getInstance()
                .computeIfAbsent(this.classPath, e -> new PacketClassInfo<>())
                .setMethodStorage(
                        MethodStorage.builder()
                                .classPath(this.classPath)
                                .elementName(this.methodName)
                                .args(new String[]{this.classPath,this.state.getContextClass()})
                                .build()
                );
    }
}
