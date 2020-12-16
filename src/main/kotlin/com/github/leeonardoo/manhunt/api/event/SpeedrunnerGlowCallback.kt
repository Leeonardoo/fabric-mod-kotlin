package com.github.leeonardoo.manhunt.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.player.PlayerEntity

/**
 * Called when the glowing effect is applied to the speedrunner.
 *
 * @author YTG1234
 * Adapted to Kotlin by Leeonardoo
 */
interface SpeedrunnerGlowCallback {

    companion object {
        val EVENT: Event<SpeedrunnerGlowCallback>
            get() = EventFactory.createArrayBacked(SpeedrunnerGlowCallback::class.java) { listeners ->
                object : SpeedrunnerGlowCallback {
                    override fun onSpeedrunnerGlow(speedrunner: PlayerEntity?): Boolean {
                        for (listener in listeners) {
                            val result = listener.onSpeedrunnerGlow(speedrunner)
                            if (result) return true
                        }
                        return false
                    }
                }
            }
    }

    /**
     * Receives the [speedrunner][PlayerEntity] that is glowing and
     * determines whether to cancel the glow.
     *
     * @param speedrunner the speedrunner that is glowing
     *
     * @return Whether to cancel the glowing.
     */
    fun onSpeedrunnerGlow(speedrunner: PlayerEntity?): Boolean
}