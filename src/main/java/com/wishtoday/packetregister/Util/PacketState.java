package com.wishtoday.packetregister.Util;

import lombok.Getter;
import net.fabricmc.api.EnvType;
import org.jetbrains.annotations.NotNull;

public enum PacketState {
    S2C("net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking$Context"
            , EnvType.CLIENT, "net/fabricmc/fabric/api/client/networking/v1/ClientPlayNetworking"),
    C2S("net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking$Context"
            , EnvType.SERVER, "net/fabricmc/fabric/api/networking/v1/ServerPlayNetworking");
    @Getter
    @NotNull
    private final String contextClass;
    @Getter
    private final EnvType envType;
    @Getter
    @NotNull
    private final String classDesc;
    @Getter
    private final String playNetworkDesc;

    PacketState(@NotNull String contextClass, EnvType envType, String playNetworkDesc) {
        this.contextClass = contextClass;
        this.envType = envType;
        this.playNetworkDesc = playNetworkDesc;
        this.classDesc = DescUtils.checkDesc(contextClass);
    }
}
