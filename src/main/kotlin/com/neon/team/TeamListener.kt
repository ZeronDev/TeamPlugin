package com.neon.team

import com.neon.team.TeamData.prefix
import com.neon.team.TeamKommand.getTeam
import com.neon.team.TeamKommand.playerHasTeam
import net.kyori.adventure.text.Component.text
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object TeamListener : Listener {
    @EventHandler
    fun onJoin(e: PlayerJoinEvent) {
        if (playerHasTeam(e.player)) {
            if (getTeam(e.player)?.leader == e.player.uniqueId) {
                val team = getTeam(e.player)!!
                if (team.requestedPlayers.size >= 1) {
                    e.player.sendMessage(prefix.append(text("가입요청이 ${team.requestedPlayers.size}개 왔습니다")))
                }
            }
        }
    }
}