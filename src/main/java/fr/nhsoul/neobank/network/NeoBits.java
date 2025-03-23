package fr.nhsoul.neobank.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record NeoBits(int amount) implements CustomPacketPayload {
    public static final CustomPacketPayload.Type<NeoBits> TYPE = new CustomPacketPayload.Type<>(ResourceLocation.fromNamespaceAndPath("neobank", "neobits"));

    public static final StreamCodec<ByteBuf, NeoBits> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT,
            NeoBits::amount,
            NeoBits::new
    );

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
