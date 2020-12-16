package com.github.leeonardoo.manhunt.command

import com.github.leeonardoo.manhunt.ManhuntUtils
import com.github.leeonardoo.manhunt.ManhuntUtils.SERVER_QUESTION_PACKET_ID
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.command.CommandManager.literal
import net.minecraft.server.command.ServerCommandSource

object ClearCacheCommand {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            literal("clearManhuntCache").executes { context ->
                ManhuntUtils.haveMod.clear()
                context.source.minecraftServer.playerManager.playerList.forEach {
                    ServerPlayNetworking.send(it, SERVER_QUESTION_PACKET_ID, PacketByteBufs.empty())
                }
                1
            }
        )
    }
}