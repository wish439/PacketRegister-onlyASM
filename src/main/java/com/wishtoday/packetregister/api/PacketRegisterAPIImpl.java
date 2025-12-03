package com.wishtoday.packetregister.api;

import com.wishtoday.packetregister.Packetregister;

public class PacketRegisterAPIImpl implements PacketRegisterAPI{
    @Override
    public void addPackage(String packageName) {
        Packetregister.addToScan(packageName);
    }
}
