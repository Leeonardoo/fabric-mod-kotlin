package com.github.leeonardoo.manhunt.command

import com.github.leeonardoo.manhunt.Manhunt
import com.github.leeonardoo.manhunt.ManhuntUtils
import com.github.leeonardoo.manhunt.ManhuntUtils.fromCmdContext
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText
import java.util.*

object HuntersCommand {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(CommandManager.literal("hunters").requires { it.hasPermissionLevel(2) }
            .then(CommandManager.literal("clear").executes { context -> executeClear(context) })
            .then(
                CommandManager.literal("add")
                    .then(CommandManager.argument("target", EntityArgumentType.player()).executes { context ->
                        executeAdd(context)
                    })
            )
            .then(CommandManager.literal("get").executes { context -> executeGet(context) })
        )
    }

    @Throws(CommandSyntaxException::class)
    private fun executeClear(context: CommandContext<ServerCommandSource>): Int {
        val playerHasMod = ManhuntUtils.playerHasMod(context)
        ManhuntUtils.hunters.clear()

        if (playerHasMod) {
            context.source.sendFeedback(TranslatableText("text.manhunt.command.hunters.clear"), true)
        } else {
            context.source.sendFeedback(LiteralText("Cleared hunter list!"), false)
        }
        return 1
    }

    @Throws(CommandSyntaxException::class)
    private fun executeAdd(context: CommandContext<ServerCommandSource>): Int {
        val target = EntityArgumentType.getPlayer(context, "target")
        val playerHasMod = ManhuntUtils.playerHasMod(context)

        // Target is speedrunner
        if (target.uuid == ManhuntUtils.speedrunner) {
            // We check if the source is a player and the player has the mod
            if (playerHasMod) {
                // And then send error message using TranslatableText
                context.source.sendError(
                    TranslatableText("text.manhunt.command.hunters.error.speedrunner", target.displayName)
                )

                // Source is not player or doesn't have mod, sending error message using LiteralText
            } else {
                context.source.sendError(
                    LiteralText("Cannot add ")
                        .append(target.displayName)
                        .append(LiteralText(" as a hunter because they are the speedrunner!"))
                )
            }
            return 1
        }

        // Check if target is already a hunter
        if (ManhuntUtils.hunters.contains(target.uuid)) {
            if (playerHasMod) {
                context.source.sendError(
                    TranslatableText("text.manhunt.command.hunters.error.hunter", target.displayName)
                )
            } else {
                context.source.sendError(
                    LiteralText("Cannot add ")
                        .append(target.displayName)
                        .append(LiteralText(" as a hunter because they are already a hunter!"))
                )
            }
            return 1
        }

        if (Manhunt.CONFIG.giveCompassWhenSettingHunters)
            fromCmdContext(context, target.uuid)?.equip(8, ItemStack(Items.COMPASS, 1))

        ManhuntUtils.hunters.add(target.uuid)

        if (playerHasMod) {
            context.source.sendFeedback(TranslatableText("text.manhunt.command.hunters.add", target.displayName), true)
        } else {
            context.source.sendFeedback(
                LiteralText("Added ")
                    .append(target.displayName)
                    .append(LiteralText(" to the hunters list!")), true
            )
        }
        return 1
    }

    @Throws(CommandSyntaxException::class)
    private fun executeGet(context: CommandContext<ServerCommandSource>): Int {
        val playerHasMod = ManhuntUtils.playerHasMod(context)
        if (ManhuntUtils.hunters.isEmpty()) return 1
        val hunterNames = mutableListOf<String>()

        ManhuntUtils.hunters.forEach { hunter ->
            fromCmdContext(context, hunter)?.displayName?.asString()?.let { hunterNames.add(it) }
        }
        if (playerHasMod) {
            context.source.sendFeedback(
                TranslatableText(
                    "text.manhunt.command.hunters.get",
                    java.lang.String.join(", ", hunterNames)
                ), false
            )
        } else {
            context.source.sendFeedback(LiteralText("Hunters are: " + java.lang.String.join(", ", hunterNames)), false)
        }
        return 1
    }
}