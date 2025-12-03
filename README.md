**简单介绍**<br>
这是一个自动注册MinecraftFabric环境下Payload的Mod<br>
**如何使用** <br>
目前我并没有将其发布至任何Maven仓库，这意味着你只能<br>
```bash
git clone https://github.com/wish439/PacketRegister-onlyASM.git
cd PacketRegister-onlyASM
./gradlew publishToMavenLocal
```
并在你的项目中
```groovy
repositories {
    mavenLocal()
}
```
并在dependencies中添加
```groovy
dependencies {
    compileOnly 'com.wishtoday:PacketRegisterASM:{version}'
}
```
Gradle加载完成后，请在您的主类设置
```java
@Override
public void onInitialize() {
    PacketRegisterAPI.getInstance().addPackage("your package");
}
```
**其他版本** <br>
[ASM+反射版本](https://github.com/wish439/PacketRegister) (他拥有自动创建空Codec的功能)

**示例**:
```java
@Packet(PacketState.C2S)
public record blockPosPacket(BlockPos pos) implements CustomPayload {
    private static final Identifier IDENTIFIER = Identifier.of(Lookblock.MOD_ID, "blockpos");
    @ID
    public static final Id<blockPosPacket> ID = new Id<>(IDENTIFIER);
    @Codec
    public static final PacketCodec<PacketByteBuf, blockPosPacket> CODEC = PacketCodec.of(blockPosPacket::encode, blockPosPacket::decode);

    @Handler
    public static void handler(blockPosPacket payload, ServerPlayNetworking.Context context) {
        context.player().sendMessage(payload.pos);
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

这是一个S2C包示例
```java
@Packet(PacketState.S2C)
public record TestPayload(int a) implements CustomPayload {
      @ID
      public static final CustomPayload.Id<TestPayload> ID = new Id<>(Identifier.of("yourmodid", "test"));
      @Codec
      public static final PacketCodec<PacketByteBuf, TestPayload> CODEC = PacketCodec.of((value, buf) -> buf.writeInt(value.a), buf -> new TestPayload(buf.readInt()));

      @Handler
      public static void handler(TestPayload payload
              , ClientPlayNetworking.Context context) {
           context.client().execute(() -> {
            context.player().sendMessage(Text.of("Hello" + payload.a));
        });
      }

     @Override
     public Id<? extends CustomPayload> getId() {
         return ID;
     }
}
```
