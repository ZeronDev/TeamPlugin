package com.neon.team

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

enum class KommandType(val text: Component) {
    CREATE(GsonComponentSerializer.gson().deserialize("{\"text\":\"》 [ 팀 만들기 ] 《\",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/team create \"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 팀을 만듭니다\",\"bold\":true,\"color\":\"blue\"}]}}")),
    NAME(GsonComponentSerializer.gson().deserialize("{\"text\":\"》 [ 이름 바꾸기 ] 《\",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/team name \"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 팀의 이름을 바꿉니다\",\"bold\":true,\"color\":\"blue\"}]}}")),
    LORE(GsonComponentSerializer.gson().deserialize("{\"text\":\"》 [ 설명 바꾸기 ] 《\",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"suggest_command\",\"value\":\"/team description \"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 팀의 이름을 바꿉니다\",\"bold\":true,\"color\":\"blue\"}]}}")),
    JOIN(GsonComponentSerializer.gson().deserialize("{\"text\":\"》 [ 확인하기 ] 《\",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/team joinList\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 가입 요청을 확인합니다\",\"bold\":true,\"color\":\"blue\"}]}}")),
    CANCEL_JOIN(GsonComponentSerializer.gson().deserialize("{\"text\":\"》 [ 취소하기 ] 《\",\"bold\":true,\"color\":\"#545FFC\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/team joinCancel\"},\"hoverEvent\":{\"action\":\"show_text\",\"contents\":[{\"text\":\"클릭하여 가입 요청을 취소합니다\",\"bold\":true,\"color\":\"blue\"}]}}")),
}