package com.wishtoday.packetregister.Manager;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Generator.HandlerRegister.AbstractHandlerRegister;
import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;

import java.util.List;

@Log4j2
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
