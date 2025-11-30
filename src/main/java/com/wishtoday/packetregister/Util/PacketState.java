package com.wishtoday.packetregister.Util;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import org.objectweb.asm.Type;

public enum PacketState {
    S2C("net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking$Context", EnvType.CLIENT),
    C2S("net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking$Context", EnvType.SERVER);
    @Getter
    private final String contextClass;
    @Getter
    private final EnvType envType;
    @Getter
    private final String classDesc;
    PacketState(String contextClass, EnvType envType) {
        this.contextClass = contextClass;
        this.envType = envType;
        this.classDesc = DescUtils.checkDesc(contextClass);
    }
}
