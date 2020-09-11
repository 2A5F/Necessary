package co.volight.necessary.mixin

import co.volight.necessary.events.PlayerLangInfo
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket
import net.minecraft.server.network.ServerPlayNetworkHandler
import net.minecraft.server.network.ServerPlayerEntity
import org.spongepowered.asm.mixin.Mixin
import org.spongepowered.asm.mixin.gen.Accessor
import org.spongepowered.asm.mixin.injection.At
import org.spongepowered.asm.mixin.injection.Inject
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo

@Mixin(ServerPlayNetworkHandler::class)
abstract class LangGetMixin {
    @Inject(at = [At("HEAD")], method = ["Lnet/minecraft/server/network/ServerPlayNetworkHandler;onClientSettings(Lnet/minecraft/network/packet/c2s/play/ClientSettingsC2SPacket;)V"])
    private fun onClientSettings(clientSettingsC2SPacket: ClientSettingsC2SPacket?, info: CallbackInfo) {
        if (clientSettingsC2SPacket == null || clientSettingsC2SPacket !is ClientSettingsC2SPacketWithLanguage) return
        val language = clientSettingsC2SPacket.getLanguage()
        val player = (this as ServerPlayNetworkHandler).player
        PlayerLangInfo.playerLanguage[player.uuid] = language.toLowerCase()
    }
}

@Mixin(ClientSettingsC2SPacket::class)
interface ClientSettingsC2SPacketWithLanguage {
    @Accessor("Lnet/minecraft/network/packet/c2s/play/ClientSettingsC2SPacket;language:Ljava/lang/String;")
    fun getLanguage(): String
}