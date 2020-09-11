package co.volight.necessary.mixin;

import co.volight.necessary.events.PlayerLangInfo;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.c2s.play.ClientSettingsC2SPacket;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public abstract class LangGetMixin implements ServerPlayPacketListener {
    @Inject(at = @At("HEAD"), method = "onClientSettings(Lnet/minecraft/network/packet/c2s/play/ClientSettingsC2SPacket;)V")
    public void onClientSettings(ClientSettingsC2SPacket packet, CallbackInfo info) {
        String language = ((ClientSettingsC2SPacketWithLanguage)packet).getLanguage();
        ServerPlayerEntity player = ((ServerPlayNetworkHandler)(Object)this).player;
        PlayerLangInfo.INSTANCE.getPlayerLanguage().put(player.getUuid(), language.toLowerCase());
    }
}
