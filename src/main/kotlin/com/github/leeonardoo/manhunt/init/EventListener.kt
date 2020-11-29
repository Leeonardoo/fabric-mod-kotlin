package com.github.leeonardoo.manhunt.init

import com.github.leeonardoo.manhunt.Manhunt
import com.github.leeonardoo.manhunt.ManhuntUtils
import com.github.leeonardoo.manhunt.ManhuntUtils.fromServer
import com.github.leeonardoo.manhunt.command.ClearCacheCommand
import com.github.leeonardoo.manhunt.command.HuntersCommand
import com.github.leeonardoo.manhunt.command.SpeedrunnerCommand
import com.github.leeonardoo.manhunt.config.Behaviours
import com.mojang.brigadier.CommandDispatcher
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.MinecraftServer
import net.minecraft.server.command.ServerCommandSource

object EventListener {

    fun updateCompasses(server: MinecraftServer) {
        ManhuntUtils.hunters.forEach { hunterUUID ->
            if (fromServer(server, hunterUUID) == null) return

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

    fun highlightSpeedrunner(server: MinecraftServer) {
        if (fromServer(server, ManhuntUtils.speedrunner) == null) return
        if (Manhunt.CONFIG.highlightSpeedrunner) {
            fromServer(server, ManhuntUtils.speedrunner)?.let {
                ManhuntUtils.applyStatusEffectToPlayer(it, StatusEffects.GLOWING)
            }
        }
    }

    fun registerCommands(commandDispatcher: CommandDispatcher<ServerCommandSource>, b: Boolean) {
        commandDispatcher.apply {
            SpeedrunnerCommand.register(this)
            HuntersCommand.register(this)
            ClearCacheCommand.register(this)
        }
    }
}