package com.github.leeonardoo.manhunt.command

import com.github.leeonardoo.manhunt.ManhuntUtils
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.exceptions.CommandSyntaxException
import net.minecraft.command.argument.EntityArgumentType
import net.minecraft.server.command.CommandManager
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.LiteralText
import net.minecraft.text.TranslatableText

object SpeedrunnerCommand {

    fun register(dispatcher: CommandDispatcher<ServerCommandSource>) {
        dispatcher.register(
            CommandManager.literal("speedrunner").requires { it.hasPermissionLevel(2) }
                .then(CommandManager.literal("set")
                    .then(CommandManager.argument("target", EntityArgumentType.player())
                        .executes { context -> executeSet(context) }
                    )
                )
                .then(CommandManager.literal("get").executes { context -> executeGet(context) })
                .then(CommandManager.literal("clear").executes { context -> executeClear(context) })
        )
    }

    @Throws(CommandSyntaxException::class)
    private fun executeSet(context: CommandContext<ServerCommandSource>): Int {
        val playerHasMod = ManhuntUtils.playerHasMod(context)
        val target = EntityArgumentType.getPlayer(context, "target")

        if (ManhuntUtils.hunters.contains(target.uuid)) {
            if (playerHasMod) {
                context.source.sendError(
                    TranslatableText("text.manhuntkt.command.speedrunner.error.hunter", target.displayName)
                )
            } else {
                context.source.sendError(
                    LiteralText("Cannot set speedrunner to ")
                        .append(target.displayName)
                        .append(LiteralText(" because they are a hunter!"))
                )
            }
            return 1
        }

        ManhuntUtils.speedrunner = target.uuid
        if (playerHasMod) {
            ManhuntUtils.speedrunner?.let { uuid ->
                context.source.sendFeedback(
                    TranslatableText(
                        "text.manhuntkt.command.speedrunner.set",
                        ManhuntUtils.fromCmdContext(context, uuid)?.displayName
                    ), true
                )
            }
        } else {
            context.source.sendFeedback(
                LiteralText("Set speedrunner to ").append(target.displayName).append(LiteralText("!")),
                true
            )
        }
        return 1
    }

    @Throws(CommandSyntaxException::class)
    private fun executeGet(context: CommandContext<ServerCommandSource>): Int {
        val playerHasMod = ManhuntUtils.playerHasMod(context)
        if (ManhuntUtils.speedrunner == null) return 1

        ManhuntUtils.speedrunner?.let {
            if (playerHasMod) {
                context.source.sendFeedback(
                    TranslatableText(
                        "text.manhuntkt.command.speedrunner.get", ManhuntUtils.fromCmdContext(context, it)?.displayName
                    ), false
                )
            } else {
                context.source.sendFeedback(
                    LiteralText("Speedrunner is currently: ")
                        .append(ManhuntUtils.fromCmdContext(context, it)?.displayName), true
                )
            }
        }

        return 1
    }

    @Throws(CommandSyntaxException::class)
    private fun executeClear(context: CommandContext<ServerCommandSource>): Int {
        val playerHasMod = ManhuntUtils.playerHasMod(context)
        ManhuntUtils.speedrunner = null

        if (playerHasMod)
            context.source.sendFeedback(TranslatableText("text.manhuntkt.command.speedrunner.clear"), true)
        else
            context.source.sendFeedback(LiteralText("Speedrunner Cleared!"), true)

        return 1
    }
}