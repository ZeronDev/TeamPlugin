package com.neon.team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

object TeamData {
    val prefix = Component.text("[ ").append(
        Component.text("N").decorate(TextDecoration.BOLD).color(TextColor.color(0x01FC7A)).
    append(Component.text("E").decorate(TextDecoration.BOLD).color(TextColor.color(0x03FCBE))).
    append(Component.text("O").decorate(TextDecoration.BOLD).color(TextColor.color(0x02FAEA))).
    append(Component.text("N").decorate(TextDecoration.BOLD).color(TextColor.color(0x03DCFD))).
    append(Component.text(" §f] ")))
    val teamList: MutableList<Team> = mutableListOf()
    val alreadyRequested: MutableList<Pair<UUID, Team>> = mutableListOf()
    val noticePlayer: MutableMap<UUID, Component> = mutableMapOf()
}