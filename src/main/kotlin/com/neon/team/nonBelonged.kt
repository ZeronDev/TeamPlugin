package com.neon.team

import com.neon.team.TeamData.prefix
import com.neon.team.TeamData.teamList
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.openFrame
import net.kyori.adventure.text.Component.text
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack


fun Player.openCreateWindow(name: String, description: String) {
    val frame = frame(3, text("§9TEAM CREATION")) {
        mutableListOf(
            0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0, 8 to 0,
            0 to 1, 1 to 1, 2 to 1, 3 to 1, 5 to 1, 6 to 1, 7 to 1, 8 to 1,
            0 to 2, 1 to 2, 2 to 2, 3 to 2, 4 to 2, 5 to 2, 6 to 2, 7 to 2, 8 to 2
        ).forEach {
            item(it.first, it.second, ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1).apply {
                itemMeta = itemMeta.apply {
                    displayName(text(" "))
                    lore(listOf(text(" ")))
                }
            })

            slot(4, 1) {
                onClick { event ->
                    event.isCancelled = false
                }
            }

            onClose {
                if (item(4, 1)?.type != Material.AIR) {
                    val newTeam = Team(name, this@openCreateWindow.uniqueId, item(4, 1)!!.apply {
                        itemMeta = itemMeta.apply {
                            displayName(text(name))
                            lore(listOf(text(description)))
                        }
                    })
                    newTeam.teamMates.add(this@openCreateWindow.uniqueId)

                    this@openCreateWindow.sendMessage(prefix.append(text("${name}(팀)이 생성되었습니다")))

                    player?.inventory?.addItem(item(4, 1) ?: ItemStack(Material.AIR))

                    player?.closeInventory()

                    teamList.add(newTeam)
                } else {
                    this@openCreateWindow.sendMessage(prefix.append(text("§c서버의 아이콘 자리에 아이템을 넣어주세요")))
                }
            }

            onClickBottom { it.isCancelled = false }
        }
    }
    this.openFrame(frame)
}

fun Player.openNonTeamInv() {
    val frame = frame(3, text("§9TEAM")) {
        mutableListOf(
            0 to 0, 1 to 0, 2 to 0, 3 to 0, 4 to 0, 5 to 0, 6 to 0, 7 to 0, 8 to 0,
            0 to 1, 1 to 1, 2 to 1, 4 to 1, 6 to 1, 7 to 1, 8 to 1,
            0 to 2, 1 to 2, 2 to 2, 3 to 2, 4 to 2, 5 to 2, 6 to 2, 7 to 2, 8 to 2
        ).forEach {
            item(it.first, it.second, ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1).apply {
                itemMeta = itemMeta.apply {
                    displayName(text(" "))
                    lore(listOf(text(" ")))
                }
            })

            slot(3, 1) {
                item = ItemStack(Material.NETHERITE_PICKAXE, 1).apply {
                    itemMeta = itemMeta.apply {
                        displayName(text("§9팀 만들기"))
                        lore(listOf(text("§9클릭하여 새로운 팀을 만듭니다")))
                    }
                }
                onClick { event -> player?.sendMessage(prefix.append(KommandType.CREATE.text)) }
            }
            slot(5, 1) {
                item = ItemStack(Material.SPYGLASS, 1).apply {
                    itemMeta = itemMeta.apply {
                        displayName(text("§9팀 가입하기"))
                        lore(listOf(text("§9클릭하여 팀에 가입합니다")))
                    }
                }
                onClick { event -> Bukkit.dispatchCommand(player as CommandSender, "/team join") }
            }
        }
    }
    this@openNonTeamInv.openFrame(frame)
}