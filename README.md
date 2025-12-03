**简单介绍**<br>
这是一个自动注册MinecraftFabric环境下Payload的Mod<br>
**如何使用** <br>
目前我并没有将其发布至任何Maven仓库，这意味着你只能<br>
```bash
git clone https://github.com/wish439/PacketRegister-onlyASM.git
```
后自己执行**publishing**任务将其发布到本地Maven仓库并在其他仓库中依赖它. <br>
**此Mod目前仅会扫描com.wishtoday包下的class,因为我不知道怎么开放一个API让别人正常使用**<br>
**其他版本** <br>
[ASM+反射版本](https://github.com/wish439/PacketRegister) (他拥有自动创建空Codec的功能)

**示例**:
```java
package com.wishtoday.ps.lookblock.netWorking;

import com.wishtoday.Annotation.Codec;
import com.wishtoday.Annotation.Handler;
import com.wishtoday.Annotation.ID;
import com.wishtoday.Annotation.Packet;
import com.wishtoday.packetregister.Util.PacketState;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import com.wishtoday.ps.lookblock.Lookblock;
import com.wishtoday.ps.lookblock.ServerPlayerEntityMixinAccessor;

@Packet(PacketState.C2S)
public record blockPosPacket(BlockPos pos) implements CustomPayload {
    private static final Identifier IDENTIFIER = Identifier.of(Lookblock.MOD_ID, "blockpos");
    @ID
    public static final Id<blockPosPacket> ID = new Id<>(IDENTIFIER);
    @Codec
    public static final PacketCodec<PacketByteBuf, blockPosPacket> CODEC = PacketCodec.of(blockPosPacket::encode, blockPosPacket::decode);

    @Handler
    public static void handler(blockPosPacket payload, ServerPlayNetworking.Context context) {
        context.player.sendMessage(payload.pos);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    private void encode(PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }

    private static blockPosPacket decode(PacketByteBuf buf) {
        return new blockPosPacket(buf.readBlockPos());
    }
}
```
本Mod会自动将此Payload注册并添加接收器.
