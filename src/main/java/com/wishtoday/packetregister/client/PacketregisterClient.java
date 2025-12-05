package com.wishtoday.packetregister.client;

import com.wishtoday.packetregister.Packetregister;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;

public class PacketregisterClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        ClientLifecycleEvents.CLIENT_STARTED.register((client) -> {
            Packetregister.startScan(EnvType.CLIENT);
        });
    }
}
