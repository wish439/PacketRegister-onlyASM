package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.Data.PacketClassInfo;

import java.util.List;

public class ClientHandlerRegister extends AbstractHandlerRegister{
    protected ClientHandlerRegister(
            String className
            , String packageName
            , List<PacketClassInfo> packetClassInfoList
            , String registerMethodName) {
        super(className, packageName, packetClassInfoList, registerMethodName);
    }

    @Override
    protected void generateLambdaClasses() {

    }

    @Override
    protected String getLambdaMethodDesc(PacketClassInfo info) {
        return "";
    }

    @Override
    protected String getRegisterMethodDesc() {
        return "";
    }

    @Override
    public void registerHandlers() {

    }
}
