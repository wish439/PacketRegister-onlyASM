package com.wishtoday.packetregister.client;

import com.wishtoday.packetregister.Packetregister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;

public class PacketregisterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientPlayConnectionEvents.JOIN.register((handler, sender, client) -> {
            sender.sendPacket(new Packetregister.TestPayload2());
        });
    }
}
