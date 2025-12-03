package com.wishtoday.packetregister.Manager;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Generator.HandlerRegister.AbstractHandlerRegister;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

public class HandlerRegisterManager {
    @Getter
    private static HandlerRegisterManager instance = new HandlerRegisterManager();

    public void startRegister(EnvType type) {
        //EnvType type = FabricLoader.getInstance().getEnvironmentType();
        PacketClassManager manager = PacketClassManager.getInstance();
        List<PacketClassInfo> infos = manager.getC2SPacketClassInfoList();

        AbstractHandlerRegister register = AbstractHandlerRegister.AbstractHandlerRegisterFactory.create(type);
        register.generate();
    }
}
