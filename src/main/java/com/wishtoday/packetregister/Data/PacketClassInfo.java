package com.wishtoday.packetregister.Data;

import com.wishtoday.packetregister.Data.Storage.MethodStorage;
import com.wishtoday.packetregister.Manager.PacketClassManager;
import com.wishtoday.packetregister.Util.PacketState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.network.packet.CustomPayload;

@Getter
@Setter
@NoArgsConstructor
public class PacketClassInfo {
    private RegisterInfo registerInfo;
    private MethodStorage methodStorage;
    private PacketState state;
    private Class<? extends CustomPayload> clazz;

    public RegisterInfo getRegisterInfo() {
        checkRegisterInfo();
        return this.registerInfo;
    }

    public void setState(PacketState state) {
        this.state = state;
        PacketClassManager manager = PacketClassManager.getInstance();
        if (state == PacketState.C2S) {
            manager.getC2SPacketClassInfoList().add(this);
            return;
        }
        if (state == PacketState.S2C) {
            manager.getS2CPacketClassInfoList().add(this);
            return;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PacketClassInfo that)) return false;
        return this.registerInfo.equals(that.registerInfo);
    }

    @Override
    public int hashCode() {
        return this.registerInfo.hashCode();
    }

    public boolean hasEmpty() {
        return this.registerInfo.getCODEC() == null ||
                this.registerInfo.getID() == null ||
                this.methodStorage.hasEmpty() ||
                this.state == null;
    }
    private void checkRegisterInfo() {
        if (this.registerInfo == null) this.registerInfo = new RegisterInfo();
    }
}
