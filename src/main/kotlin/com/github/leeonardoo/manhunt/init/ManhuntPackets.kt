package com.github.leeonardoo.manhunt.init

import com.github.leeonardoo.manhunt.Manhunt
import com.github.leeonardoo.manhunt.ManhuntUtils
import io.netty.buffer.Unpooled
import net.fabricmc.fabric.api.network.ClientSidePacketRegistry
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry
import net.minecraft.network.PacketByteBuf
import net.minecraft.util.Identifier

object ManhuntPackets {

    private val SERVER_QUESTION_PACKET_ID: Identifier = Identifier(Manhunt.MOD_ID, "question")
    private val CLIENT_ANSWER_PACKET_ID: Identifier = Identifier(Manhunt.MOD_ID, "answer")

    fun registerClientPackets() {
        ClientSidePacketRegistry.INSTANCE.register(SERVER_QUESTION_PACKET_ID) { context, _ ->
            context.taskQueue.execute {
                ClientSidePacketRegistry.INSTANCE.sendToServer(
                    CLIENT_ANSWER_PACKET_ID,
                    PacketByteBuf(Unpooled.buffer())
                )
            }
        }
    }

    fun registerServerPackets() {
        ServerSidePacketRegistry.INSTANCE.register(CLIENT_ANSWER_PACKET_ID) { context, _ ->
            ManhuntUtils.haveMod.add(context.player)
        }
    }
}