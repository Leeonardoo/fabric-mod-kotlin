package com.github.leeonardoo.manhunt.command

import com.github.leeonardoo.manhunt.ManhuntUtils
import com.github.leeonardoo.manhunt.init.ManhuntPackets
import com.mojang.brigadier.CommandDispatcher
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

object ClearCacheCommand {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("clearManhuntCache").executes { context ->
                ManhuntUtils.haveMod.clear()
                context.source.minecraftServer.playerManager.playerList.forEach {
                    ServerSidePacketRegistry.INSTANCE.sendToPlayer(
                        it, ManhuntPackets.SERVER_QUESTION_PACKET_ID, PacketByteBuf(Unpooled.buffer())
                    )
                }
                1
            }
        )
    }
}