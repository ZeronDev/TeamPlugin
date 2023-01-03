package com.neon.team

import com.neon.team.TeamData.alreadyRequested
import com.neon.team.TeamData.prefix
import com.neon.team.TeamData.teamList
import io.github.monun.invfx.InvFX.frame
import io.github.monun.invfx.frame.InvFrame
import io.github.monun.kommand.PluginKommand
import io.github.monun.kommand.StringType
import io.github.monun.kommand.getValue
import net.kyori.adventure.text.Component.text
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

object TeamKommand {
    fun teamKommand(kommand: PluginKommand) {
        kommand.register("team") {
            requires { sender is Player }
            executes {
                if (!playerHasTeam(player)) {
                    player.openNonTeamInv()
                } else {
                    getTeam(player)?.openManager(player)
                }
            }

            then("create") {
                requires { !playerHasTeam(player) }
                then("teamName" to string(StringType.GREEDY_PHRASE)) {
                    then("teamDescription" to string(StringType.GREEDY_PHRASE)) {
                        executes {
                            val teamName: String = it["teamName"]
                            val teamDescription: String = it["teamDescription"]
                            if (!teamNameContains(teamName)) {
                                teamName.replace("&", "§")
                                teamDescription.replace("&", "§")

                                player.openCreateWindow(teamName, teamDescription)
                            } else {
                                player.sendMessage(prefix.append(text("§c이미 존재하는 팀 이름입니다")))
                            }
                        }
                    }
                }
            }

            then("members") {
                requires { playerHasTeam(player) }
                executes {
                    getTeam(player)?.playerList(player)
                }
            }

            then("joinList") {
                requires { getTeam(player)?.leader == player.uniqueId }
                executes {
                    getTeam(player)!!.joinList(player)
                }
            }

            then("join") {
                requires { !playerHasTeam(player) }
                executes {

                }
                then("teamName" to string(StringType.GREEDY_PHRASE).apply { suggests { suggest(teamList.map { "\"${it.name}\"" }) } }) {
                    executes {
                        val teamName: String by it
                        if (teamNameContains(teamName)) {
                            var already = false
                            var team: Team? = null
                            alreadyRequested.forEach {
                                if (it.first == player.uniqueId) {
                                    already = true
                                    team = it.second
                                }

                            }
                            if (!already) {
                                if (getTeamByName(teamName)!!.requestedPlayers.size < 54) {
                                    getTeamByName(teamName)!!.joinPlayer(player.uniqueId)
                                    player.sendMessage(prefix.append(text("${teamName}(팀)에 가입 요청을 보냈습니다").append(KommandType.CANCEL_JOIN.text)))
                                    if (Bukkit.getOfflinePlayer(getTeamByName(teamName)!!.leader).isOnline) {
                                        Bukkit.getPlayer(getTeamByName(teamName)!!.leader)!!.sendMessage(prefix.append(
                                            text("${player.name}이 가입 요청을 보냈습니다").append(
                                                GsonComponentSerializer.gson().deserialize("{\"text\":\" [ 수락 ] \",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/team joinAccept ${player.name}\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 가입 요청을 수락합니다\",\"bold\":true,\"color\":\"blue\"}]}}")),
                                            ).append(
                                            GsonComponentSerializer.gson().deserialize("{\"text\":\"》 [ 거부 ]\",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/team joinDeny ${player.name}\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 가입 요청을 거부합니다\",\"bold\":true,\"color\":\"blue\"}]}}")
                                            ))
                                    }
                                } else {
                                    player.sendMessage(prefix.append(text("§c팀의 가입 요청 인원이 정원입니다")))
                                }
                            } else {
                                player.sendMessage(prefix.append(text("§c${team!!.name}(팀)에 이미 가입 요청을 보냈습니다")))
                            }
                        } else {
                            player.sendMessage(prefix.append(text("§c존재하지 않는 팀입니다")))
                        }
                    }
                }
            }
            then("joinAccept") {
                requires { playerHasTeam(player) && getTeam(player)?.leader == player.uniqueId }
                then("playerName" to string().apply { suggests { suggest(Bukkit.getOnlinePlayers().toMutableList().map { it.name }) } }) {
                    executes {
                        val playerName: String by it
                        if (getTeam(player)!!.requestedPlayers.contains(Bukkit.getOfflinePlayer(playerName).uniqueId)) {
                            getTeam(player)!!.acceptPlayer(Bukkit.getOfflinePlayer(playerName).uniqueId)
                            player.sendMessage(prefix.append(text("${playerName}의 가입 요청을 수락하였습니다")))
                        } else {
                            player.sendMessage(prefix.append(text("§c가입 요청하지 않은 플레이어 입니다")))
                        }
                    }
                }
            }

            then("joinDeny") {
                requires { playerHasTeam(player) && getTeam(player)?.leader == player.uniqueId }
                then("playerName" to string().apply { suggests { suggest(Bukkit.getOnlinePlayers().toMutableList().map { it.name }) } }) {
                    executes {
                        val playerName: String by it
                        if (getTeam(player)!!.requestedPlayers.contains(Bukkit.getOfflinePlayer(playerName).uniqueId)) {
                            getTeam(player)!!.denyPlayer(Bukkit.getOfflinePlayer(playerName).uniqueId)
                            player.sendMessage(prefix.append(text("${playerName}의 가입 요청을 거부하였습니다")))
                        } else {
                            player.sendMessage(prefix.append(text("§c가입 요청하지 않은 플레이어 입니다")))
                        }
                    }
                }
            }

            then("joinCancel") {
                requires { !playerHasTeam(player) }
                executes {
                    teamList.forEach {
                        if (it.requestedPlayers.contains(player.uniqueId)) {
                            player.sendMessage(prefix.append(text("가입 요청이 취소되었습니다")))
                        } else {
                            player.sendMessage(prefix.append(text("§c가입 요청을 한 적이 없습니다")))
                        }
                    }
                }
            }
        }
    }

    fun teamNameContains(name: String): Boolean {
        teamList.forEach {
            if (it.name == name) {
                return true
            }
        }
        return false
    }

    fun playerHasTeam(p: Player): Boolean {
        teamList.forEach {
            if (it.teamMates.contains(p.uniqueId)) {
                return true
            }
        }
        return false
    }

    fun getTeam(p: Player): Team? {
        teamList.forEach {
            if (it.teamMates.contains(p.uniqueId)) return it
        }
        return null
    }

    fun getTeamByName(str: String): Team? {
        teamList.forEach {
            if (it.name == str) return it
        }
        return null
    }

    fun indexFrame(title: String, list: MutableList<ItemStack>): InvFrame {
        val inv = frame(6, text(title)) {

        }
        return inv
    }
}