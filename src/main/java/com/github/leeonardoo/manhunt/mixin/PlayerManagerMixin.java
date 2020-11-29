package com.github.leeonardoo.manhunt.mixin;

import com.github.leeonardoo.manhunt.Manhunt;
import com.github.leeonardoo.manhunt.init.ManhuntPackets;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.apache.logging.log4j.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public abstract class PlayerManagerMixin {

    @Inject(method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V", at = @At("RETURN"), cancellable = false)
    public void onPlayerLogin(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
        PacketByteBuf passedData = new PacketByteBuf(Unpooled.buffer());
        ServerSidePacketRegistry.INSTANCE.sendToPlayer(player, ManhuntPackets.INSTANCE.getSERVER_QUESTION_PACKET_ID(), passedData);
        Manhunt.Companion.log(Level.DEBUG, "Asked Question");
    }
}
