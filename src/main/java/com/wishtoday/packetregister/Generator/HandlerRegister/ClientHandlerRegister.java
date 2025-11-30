package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Util.PacketState;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import org.objectweb.asm.Type;

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
    protected PacketState getProcessState() {
        return PacketState.S2C;
    }

    @Override
    protected String getRegisterMethodDesc() {
        return String.format("(%s%s)Z"
                , Type.getDescriptor(CustomPayload.Id.class)
                , Type.getDescriptor(ClientPlayNetworking.PlayPayloadHandler.class));
    }
}
