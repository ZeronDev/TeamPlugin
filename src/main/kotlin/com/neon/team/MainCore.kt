package com.neon.team

import com.neon.team.TeamKommand.teamKommand
import io.github.monun.kommand.kommand
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class MainCore : JavaPlugin() {
    companion object {
        lateinit var plugin: Plugin
    }

    override fun onEnable() {
        plugin = this
        logger.info("[ NEON ] 플러그인이 활성화 중입니다")

        kommand {
            teamKommand(this)
        }

        server.pluginManager.registerEvents(TeamListener, plugin)
    }

    override fun onDisable() {
        logger.info("[ NEON ] 플러그인이 비활성화 중입니다")
    }
}