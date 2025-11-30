package com.wishtoday.packetregister.Generator.HandlerRegister;

import com.wishtoday.packetregister.Data.PacketClassInfo;
import com.wishtoday.packetregister.Util.PacketState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import org.objectweb.asm.Type;

import java.util.List;

public class ServerHandlerRegister extends AbstractHandlerRegister {
    protected ServerHandlerRegister(
            String className
            , String packageName
            , List<PacketClassInfo> packetClassInfoList
            , String registerMethodName) {
        super(className, packageName, packetClassInfoList, registerMethodName);
    }

    @Override
    protected PacketState getProcessState() {
        return PacketState.C2S;
    }

    @Override
    protected String getRegisterMethodDesc() {
        return String.format("(%s%s)Z"
                , Type.getDescriptor(CustomPayload.Id.class)
                , Type.getDescriptor(ServerPlayNetworking.PlayPayloadHandler.class));
    }
}
