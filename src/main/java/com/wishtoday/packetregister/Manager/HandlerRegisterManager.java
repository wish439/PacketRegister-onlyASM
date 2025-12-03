package com.wishtoday.packetregister.Manager;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Generator.HandlerRegister.AbstractHandlerRegister;
import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

import static com.wishtoday.packetregister.Packetregister.log;

public class HandlerRegisterManager {
    @Getter
    private static HandlerRegisterManager instance = new HandlerRegisterManager();

    public void startRegister() {
        EnvType type = FabricLoader.getInstance().getEnvironmentType();
        PacketClassManager manager = PacketClassManager.getInstance();
        List<PacketClassInfo> infos = manager.getC2SPacketClassInfoList();

        log.info("请求了一次环境类型: {}", type.name());
        AbstractHandlerRegister register = AbstractHandlerRegister.AbstractHandlerRegisterFactory.create(type);
        register.generate();
    }
}
