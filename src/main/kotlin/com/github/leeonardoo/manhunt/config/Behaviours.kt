package com.github.leeonardoo.manhunt.config

object Behaviours {

    enum class Compass {
        // Gets updated every tick
        UPDATE,

        // The compass needs to be used to get updated
        USE
    }

    enum class Damage {
        DAMAGE,
        KILL
    }
}