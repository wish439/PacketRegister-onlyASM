package com.wishtoday.packetregister.api;

public interface PacketRegisterAPI {
    void addPackage(String packageName);
    static PacketRegisterAPI getInstance() {
        return new PacketRegisterAPIImpl();
    }
}
