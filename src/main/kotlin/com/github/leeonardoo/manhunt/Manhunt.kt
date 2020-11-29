package com.github.leeonardoo.manhunt

import com.github.leeonardoo.manhunt.config.ManhuntConfig
import com.github.leeonardoo.manhunt.init.EventListener
import com.github.leeonardoo.manhunt.init.ManhuntPackets
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import me.sargunvohra.mcmods.autoconfig1u.serializer.JanksonConfigSerializer
import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Level

class Manhunt : ModInitializer {

    companion object {
        const val MOD_ID = "manhuntkt"
        private const val MOD_NAME = "Manhunt Fabric Kotlin"
        private val LOGGER = LogManager.getLogger(MOD_NAME)
        lateinit var CONFIG: ManhuntConfig
            private set

        fun log(level: Level, msg: String) {
            LOGGER.log(level, msg)
        }
    }

    override fun onInitialize() {
        log(Level.INFO, "Starting")
        AutoConfig.register(ManhuntConfig::class.java, ::JanksonConfigSerializer)
        CONFIG = AutoConfig.getConfigHolder(ManhuntConfig::class.java).config

        ServerTickEvents.END_SERVER_TICK.register(EventListener::updateCompasses)
        ServerTickEvents.END_SERVER_TICK.register(EventListener::highlightSpeedrunner)

        CommandRegistrationCallback.EVENT.register(EventListener::registerCommands)
        ManhuntPackets.registerServerPackets()
    }
}