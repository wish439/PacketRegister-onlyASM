package com.wishtoday.packetregister.Manager;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import lombok.Getter;
import net.minecraft.network.packet.CustomPayload;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

@SuppressWarnings({"FieldMayBeFinal"})
public class PacketClassManager {
    @Getter
    private static PacketClassManager instance = new PacketClassManager();
    //key:a class path; ASM friendly: Example: "java/lang/String"
    private Map<String, PacketClassInfo> packetClassInfoMap;
    @Getter
    private List<PacketClassInfo> C2SPacketClassInfoList;
    @Getter
    private List<PacketClassInfo> S2CPacketClassInfoList;
    private PacketClassManager() {
        this.packetClassInfoMap = new Object2ObjectOpenHashMap<>();
        this.C2SPacketClassInfoList = new ArrayList<>();
        this.S2CPacketClassInfoList = new ArrayList<>();
    }
    public void putPacketClassInfo(
            String id
            , PacketClassInfo packetClassInfo) {
        id = processPath(id);
        if (packetClassInfoMap.containsKey(id)) throw new IllegalArgumentException(id + "class payload ID already exists!");
        packetClassInfoMap.put(id, packetClassInfo);
    }
    public PacketClassInfo getPacketClassInfo(
            String id) {
        id = processPath(id);
        return packetClassInfoMap.get(id);
    }
    public PacketClassInfo computeIfAbsent(
            String key
            , @NotNull Function<? super String
                    , ? extends PacketClassInfo> mappingFunction) {
        key = processPath(key);
        return packetClassInfoMap.computeIfAbsent(key, mappingFunction);
    }
    public Collection<PacketClassInfo> getAllPacketClassInfo() {
        return packetClassInfoMap.values();
    }
    /**
     * A check classpath format and auto process method
     * @param path The classpath
     * @return process's classpath
     */
    @NotNull
    private String processPath(@NotNull String path) {
        if (path.contains("."))
            return path.replace(".", "/");
        return path;
    }
}
