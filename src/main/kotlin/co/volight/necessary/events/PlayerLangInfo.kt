package co.volight.necessary.events

import co.volight.necessary.Nec
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents
import net.minecraft.server.MinecraftServer
import net.minecraft.server.network.ServerPlayerEntity
import java.util.*

object PlayerLangInfo {
    val playerLanguage = mutableMapOf<UUID, String>()

    fun get(id: UUID): String? = playerLanguage[id]
    fun get(player: ServerPlayerEntity): String {
        val r = get(player.uuid)
        if (r == null) {
            Nec.LOGGER.warn("Can't find the language information of player ${player.entityName} ")
            return "en_us"
        }
        return r
    }

    private fun gcTick(server: MinecraftServer) {
        val manager = server.playerManager
        playerLanguage.entries.removeIf {
            manager.getPlayer(it.key) == null
        }
    }

    private fun regGcTick() {
        val gcTickTime = 600000
        var lastTime = System.currentTimeMillis()
        ServerTickEvents.END_SERVER_TICK.register(ServerTickEvents.EndTick {
            val now = System.currentTimeMillis()
            if (gcTickTime + lastTime < System.currentTimeMillis()) return@EndTick
            lastTime = now
            gcTick(it)
        })
        Nec.LOGGER.info("PlayerLangInfo gcTick registered")
    }

    fun regEvent() {
        regGcTick()
    }

}