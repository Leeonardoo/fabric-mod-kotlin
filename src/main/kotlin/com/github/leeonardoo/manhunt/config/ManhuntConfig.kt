package com.github.leeonardoo.manhunt.config

import me.sargunvohra.mcmods.autoconfig1u.ConfigData
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.EnumHandler
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry.Gui.Tooltip
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment

@Config(name = "manhuntkt")
class ManhuntConfig : ConfigData {

    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Comment("Sets the behaviour of the compass mechanic, can be either UPDATE or USE. UPDATE = Automatically update the compass every tick. USE = Use the compass to update it (more like Dream's manhunt).")
    @Tooltip(count = 3)
    var compassBehaviour = Behaviours.Compass.USE

    @EnumHandler(option = EnumHandler.EnumDisplayOption.BUTTON)
    @Comment("Sets the behaviour of the damage mechanic. Can be either KILL or DAMAGE. KILL = The speedrunner loses when they are killed (like Dream's manhunt). DAMAGE = The speedrunner loses when they take damage (like dream's assassin).")
    @Tooltip(count = 3)
    var damageBehaviour = Behaviours.Damage.KILL

    @Comment("If true, gives players a compass when added to the hunters list.")
    @Tooltip
    var giveCompassWhenSettingHunters = true

    // A new list because auto config will try to append the values in the Json file
    @Comment("Dimensions that the compass won't work in.")
    @Tooltip
    var disabledDimensions = arrayListOf<String>()

    @Comment("Weather to apply the glowing effect to the speedrunner, similar to Dream's Survivalist")
    @Tooltip
    var highlightSpeedrunner = false
}