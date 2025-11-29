package com.wishtoday.packetregister.Generator.HandlerRegister;

public interface HandlerRegister {
    void registerHandlers();
    /*class HandlerRegisterFactory {
        public static HandlerRegister createHandlerRegister(
                PacketClassInfo classInfo
                , String packageName
                , String className) {
            return switch (classInfo.getState().getEnvType()) {
                case CLIENT -> new ClientHandlerRegister(packageName, className);
                case SERVER -> new ServerHandlerRegister(className, packageName);
                default -> null;
            };
        }
    }*/
}
