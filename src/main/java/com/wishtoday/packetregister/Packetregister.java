package com.wishtoday.packetregister;

import com.google.common.collect.Lists;
import com.wishtoday.Annotation.*;
import com.wishtoday.packetregister.Exception.ClassLoadException;
import com.wishtoday.packetregister.Generator.RegisterClassGenerator;
import com.wishtoday.packetregister.Manager.HandlerRegisterManager;
import com.wishtoday.packetregister.Util.IdentifierCreator;
import com.wishtoday.packetregister.Visitors.ClassVisitor.PacketClassVisitor;
import io.github.classgraph.*;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class Packetregister implements ModInitializer {
    private static final List<String> scanPackages = Lists.newArrayList("com.wishtoday");

    public static final Logger log = LogManager.getLogger(Packetregister.class);

    @Override
    public void onInitialize() {
        IdentifierCreator.setNameSpace("pctr");
        ScanResult scan = new ClassGraph()
                .acceptPackages(scanPackages.toArray(new String[0]))
                .enableAllInfo()
                .scan();
        ClassInfoList list = scan.getClassesWithAnnotation(Packet.class);
        log.debug("Found {} packets", list.size());
        list.forEach(log::debug);
        ClassInfoList initList = scan.getClassesWithAnnotation(Initialize.class);
        list.forEach(Packetregister::registerClassImpl);
        initList.forEach(Packetregister::initializeImpl);
        new RegisterClassGenerator("GeneratePacketRegister", "com.wishtoday").generate();
        HandlerRegisterManager.getInstance().startRegister();
        scan.close();
/*
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            sender.sendPacket(new TestPayload(10));
        });
*/
    }

    private static void initializeImpl(ClassInfo classInfo) {
        try {
            Class.forName(classInfo.getName());
        } catch (ClassNotFoundException e) {
            log.error("class {} not found {}", classInfo.getName(), e.toString());
            throw new ClassLoadException("class " + classInfo.getName() + " not found " +  e);
        }
    }

    private static void registerClassImpl(ClassInfo info) {
        Resource resource = info.getResource();
        try (InputStream open = resource.open()) {
            ClassReader reader = new ClassReader(open);
            reader.accept(new PacketClassVisitor(), 0);
        } catch (IOException e) {
            log.error("asm exception {}", e.toString());
            throw new ClassLoadException("asm exception " + e);
        }
    }

   /* @Packet(PacketState.S2C)
    public record TestPayload(int a) implements CustomPayload {
        @ID
        public static final CustomPayload.Id<TestPayload> ID = new Id<>(Identifier.of("pctr", "test"));
        @Codec
        public static final PacketCodec<PacketByteBuf, TestPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeInt(value.a), buf -> new TestPayload(buf.readInt()));

        @Handler
        public static void handler(TestPayload payload
                , ClientPlayNetworking.Context context) {
            context.client().execute(() -> {
                context.player().sendMessage(Text.of("Hello" + payload.a));
            });
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }
    @Packet(PacketState.C2S)
//    @EmptyCodec
    public record TestPayload2() implements CustomPayload {
        @ID
        public static final CustomPayload.Id<TestPayload2> ID = new Id<>(Identifier.of("pctr", "test2"));

        @Codec
        public static final PacketCodec<PacketByteBuf, TestPayload2> CODEC = PacketCodec.unit(new TestPayload2());
        @Handler
        public static void handler(TestPayload2 payload
                , ServerPlayNetworking.Context context) {
            context.server().execute(() -> {
                context.player().sendMessage(Text.of("Hello this is TestPayload2!"));
            });
        }

        @Override
        public Id<? extends CustomPayload> getId() {
            return ID;
        }
    }*/
}
