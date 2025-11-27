package com.wishtoday.packetregister.Data;

import com.wishtoday.packetregister.Data.Storage.MethodStorage;
import com.wishtoday.packetregister.Util.PacketState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.minecraft.network.packet.CustomPayload;

@Getter
@Setter
@NoArgsConstructor
public class PacketClassInfo<T extends CustomPayload> {
    private RegisterInfo registerInfo;
    private MethodStorage methodStorage;
    private PacketState state;
    private Class<? extends CustomPayload> clazz;

    public RegisterInfo getRegisterInfo() {
        checkRegisterInfo();
        return this.registerInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PacketClassInfo<?> that)) return false;
        return this.registerInfo.equals(that.registerInfo);
    }

    @Override
    public int hashCode() {
        return this.registerInfo.hashCode();
    }

    public boolean hasEmpty() {
        return this.registerInfo.getCODEC() == null ||
                this.registerInfo.getID() == null ||
                this.clazz == null ||
                this.methodStorage.hasEmpty() ||
                this.state == null;
    }
    private void checkRegisterInfo() {
        if (this.registerInfo == null) this.registerInfo = new RegisterInfo();
    }
}
