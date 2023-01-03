package com.neon.team

import com.neon.team.TeamData.noticePlayer
import com.neon.team.TeamData.prefix
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

class Team(var name: String, var leader: UUID, var icon: ItemStack) {
    val teamMates = mutableListOf<UUID>()
    val requestedPlayers = mutableListOf<UUID>()
    val teamChatPlayers = mutableListOf<UUID>()

    fun openManager(player: Player) {
        if (player.uniqueId == leader) {
            val frame = frame(5, text(name)) {

                mutableListOf(
                    1 to 1, 2 to 1, 3 to 1, 4 to 1, 5 to 1, 6 to 1, 7 to 1, 8 to 1, 9 to 1,
                    2 to 1, 2 to 3, 2 to 5, 2 to 7, 2 to 9,
                    3 to 1, 3 to 2, 3 to 3, 3 to 4, 3 to 5, 3 to 6, 3 to 7, 3 to 8, 3 to 9,
                    4 to 1, 4 to 3, 4 to 5, 4 to 7, 4 to 9,
                    5 to 1, 5 to 2, 5 to 3, 5 to 4, 5 to 5, 5 to 6, 5 to 7, 5 to 8, 5 to 9
                ).forEach {
                    item(it.first, it.second, ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1).apply {
                        itemMeta = itemMeta.apply {
                            displayName(text(" "))
                            lore(listOf(text(" ")))
                        }
                    })
                }
                slot(2, 2) {
                    item = ItemStack(Material.NAME_TAG) 
                }
            }
        } else {
            //팀원
        }
    }

    fun banPlayer(p_uuid: UUID) {

    }

    fun entrust(p_uuid: UUID) {

    }

    fun joinPlayer(p_uuid: UUID) {
        requestedPlayers.add(p_uuid)
    }

    fun playerList(p: Player) {
        val invframe = frame(6, text("§9팀원 리스트")) {
            for (index in 0..teamMates.size) {
                val ySlot = index/9
                val xSlot = index-ySlot

                item(xSlot, ySlot, ItemStack(Material.PLAYER_HEAD, 1).apply {
                    itemMeta = itemMeta.apply {
                        val meta = this as SkullMeta
                        meta.owningPlayer = Bukkit.getOfflinePlayer(teamMates[index])

                        meta.displayName(text(Bukkit.getOfflinePlayer(teamMates[index]).name ?: "§cERROR"))
                        if (teamMates[index] == leader) {
                            meta.lore(listOf(text("§6§l★리더★")))
                        }
                    }
                })
            }
        }
        p.openFrame(invframe)
    }

    fun joinList(p: Player) {
        val invframe = frame(6, text("§9가입 요청")) {
            for (index in 0..requestedPlayers.size) {
                val ySlot = index/9
                val xSlot = index-ySlot

                item(xSlot, ySlot, ItemStack(Material.PLAYER_HEAD, 1).apply {
                    itemMeta = itemMeta.apply {
                        val meta = this as SkullMeta
                        meta.owningPlayer = Bukkit.getOfflinePlayer(requestedPlayers[index])

                        meta.displayName(text(Bukkit.getOfflinePlayer(requestedPlayers[index]).name ?: "§cERROR"))
                        lore(listOf(text("왼쪽 마우스 버튼 클릭으로 거절, 오른쪽 마우스 버튼 클릭으로 수락합니다")))
                    }
                })

                onClick {x: Int, y: Int, event: InventoryClickEvent ->
                    item(x, y)?.itemMeta.let {
                        if (event.click == ClickType.LEFT) {
                            denyPlayer((it as SkullMeta).owningPlayer!!.player!!.uniqueId)
                        } else if (event.click == ClickType.RIGHT) {
                            acceptPlayer((it as SkullMeta).owningPlayer!!.player!!.uniqueId)
                        }
                    }
                }
            }
        }
        p.openFrame(invframe)
    }

    fun requestPlayer(p_uuid: UUID) {

    }

    fun acceptPlayer(p_uuid: UUID) {
        requestedPlayers.remove(p_uuid)
        teamMates.forEach {
            if (Bukkit.getOfflinePlayer(it).isOnline) Bukkit.getPlayer(it)!!.sendMessage(prefix.append(text("${Bukkit.getOfflinePlayer(p_uuid).name}이 팀에 합류하였습니다")))
            else noticePlayer.put(it, prefix.append(text("${Bukkit.getOfflinePlayer(p_uuid).name}이 팀에 합류하였습니다")))
        }
        teamMates.add(p_uuid)

        if (Bukkit.getOfflinePlayer(p_uuid).isOnline) Bukkit.getPlayer(p_uuid)!!.sendMessage(prefix.append(text("${name}에 보낸 가입 요청이 수락되었습니다")))
        noticePlayer[p_uuid] = prefix.append(text("${name}에 보낸 가입 요청이 수락되었습니다"))
    }

    fun denyPlayer(p_uuid: UUID) {
        requestedPlayers.remove(p_uuid)
        if (Bukkit.getOfflinePlayer(p_uuid).isOnline) Bukkit.getPlayer(p_uuid)!!.sendMessage(prefix.append(text("${name}에 보낸 가입 요청이 수락되었습니다")))
        noticePlayer[p_uuid] = prefix.append(text("${name}에 보낸 가입 요청이 거부되었습니다"))
    }


}