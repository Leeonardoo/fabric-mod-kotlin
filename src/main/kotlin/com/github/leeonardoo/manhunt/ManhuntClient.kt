package com.github.leeonardoo.manhunt

import com.github.leeonardoo.manhunt.init.ManhuntPackets
import net.fabricmc.api.ClientModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
class ManhuntClient : ClientModInitializer {

    override fun onInitializeClient() {
        ManhuntPackets.registerClientPackets()
    }
}