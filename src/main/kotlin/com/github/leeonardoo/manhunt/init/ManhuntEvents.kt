package com.github.leeonardoo.manhunt.init

import com.github.leeonardoo.manhunt.Manhunt
import com.github.leeonardoo.manhunt.ManhuntUtils
import com.github.leeonardoo.manhunt.ManhuntUtils.CLIENT_ANSWER_PACKET_ID
import com.github.leeonardoo.manhunt.ManhuntUtils.SERVER_QUESTION_PACKET_ID
import com.github.leeonardoo.manhunt.ManhuntUtils.fromServer
import com.github.leeonardoo.manhunt.api.event.SpeedrunnerGlowCallback
import com.github.leeonardoo.manhunt.command.ClearCacheCommand
import com.github.leeonardoo.manhunt.command.HuntersCommand
import com.github.leeonardoo.manhunt.command.SpeedrunnerCommand
import com.github.leeonardoo.manhunt.config.Behaviours
import com.mojang.brigadier.CommandDispatcher
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource

object ManhuntEvents {

    fun registerCommonEvents() {
        //Registering events to every server tick
        ServerTickEvents.END_SERVER_TICK.register(ManhuntEvents::updateCompasses)
        ServerTickEvents.END_SERVER_TICK.register(ManhuntEvents::highlightSpeedrunner)

        //Registering commands
        CommandRegistrationCallback.EVENT.register(ManhuntEvents::registerCommands)

        ServerPlayNetworking.registerGlobalReceiver(CLIENT_ANSWER_PACKET_ID) { server, player, _, _, _ ->
            server.execute {
                if (!ManhuntUtils.haveMod.contains(player)) player?.let { ManhuntUtils.haveMod.add(player) }
            }
        }
    }

    private fun updateCompasses(server: MinecraftServer) {
        ManhuntUtils.hunters.forEach { hunterUUID ->
            //Only continue if player isn't null
            if (fromServer(server, hunterUUID) == null) return

            //Alright
            var stack = fromServer(server, hunterUUID)?.inventory?.getStack(8)

            if (stack == null || stack.isEmpty || stack.item != Items.COMPASS) {
                if (Manhunt.CONFIG.giveCompassWhenSettingHunters) {
                    fromServer(server, hunterUUID)?.equip(8, ItemStack(Items.COMPASS))
                    stack = fromServer(server, hunterUUID)?.inventory?.getStack(8)
                } else if (stack == null) return
            }

            if (Manhunt.CONFIG.compassBehaviour == Behaviours.Compass.UPDATE) {
                //Set compass NBT
                if (stack?.item == Items.COMPASS) {
                    fromServer(server, hunterUUID)?.equip(
                        8, stack?.let { ManhuntUtils.updateCompass(it, fromServer(server, ManhuntUtils.speedrunner)) }
                    )
                }
            }
        }
    }

    private fun highlightSpeedrunner(server: MinecraftServer) {
        //Only continue if player isn't null
        if (fromServer(server, ManhuntUtils.speedrunner) == null) return
        if (Manhunt.CONFIG.highlightSpeedrunner) {
            val willCancel =
                SpeedrunnerGlowCallback.EVENT.invoker().onSpeedrunnerGlow(fromServer(server, ManhuntUtils.speedrunner))

            if (!willCancel) fromServer(server, ManhuntUtils.speedrunner)?.let {
                ManhuntUtils.applyStatusEffectToPlayer(it, StatusEffects.GLOWING)
            }
        }
    }

    private fun registerCommands(commandDispatcher: CommandDispatcher<ServerCommandSource>, b: Boolean) {
        SpeedrunnerCommand.register(commandDispatcher)
        HuntersCommand.register(commandDispatcher)
        ClearCacheCommand.register(commandDispatcher)
    }

    fun registerClientSideEvents() {
        ClientPlayNetworking.registerGlobalReceiver(SERVER_QUESTION_PACKET_ID) { client, _, _, _ ->
            client.execute {
                ClientPlayNetworking.send(CLIENT_ANSWER_PACKET_ID, PacketByteBufs.empty())
            }
        }
    }
}