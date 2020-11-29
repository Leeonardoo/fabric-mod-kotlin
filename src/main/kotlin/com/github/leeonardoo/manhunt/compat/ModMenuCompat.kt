package com.github.leeonardoo.manhunt.compat

import com.github.leeonardoo.manhunt.config.ManhuntConfig
import io.github.prospector.modmenu.api.ConfigScreenFactory
import io.github.prospector.modmenu.api.ModMenuApi
import me.sargunvohra.mcmods.autoconfig1u.AutoConfig
import net.fabricmc.api.EnvType
import net.fabricmc.api.Environment

@Environment(EnvType.CLIENT)
class ModMenuCompat : ModMenuApi {

    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent -> AutoConfig.getConfigScreen(ManhuntConfig::class.java, parent).get() }
    }
}