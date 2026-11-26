package fr.juke.mixins;

import fr.juke.config.Config;
import fr.juke.util.TextFormatter;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerList.class)
public abstract class PlayerManagerMixin {
    public int customizationUpdateTimer;

    @Shadow
    public abstract void broadcastAll(Packet<?> packet);

    @Inject(at = @At("HEAD"), method = "tick")
    public void updatePlayerLatency(CallbackInfo ci) {
        if (
                Config.INSTANCE.enabled
                        && ++this.customizationUpdateTimer >
                        (Config.INSTANCE.refreshTickInterval == 0
                                ? 20 // fallback if unset in config file
                                : Config.INSTANCE.refreshTickInterval)
        ) {
            this.broadcastAll(new ClientboundTabListPacket(
                    Component.literal(TextFormatter.tablistChars(
                            Config.INSTANCE.header != null
                                    ? Config.INSTANCE.header
                                    : "" // fallback if unset in config file
                    )),
                    Component.literal(TextFormatter.tablistChars(
                            Config.INSTANCE.footer != null
                                    ? Config.INSTANCE.footer
                                    : "" // fallback if unset in config file
                    ))));
            this.customizationUpdateTimer = 0;
        }
    }

    @Inject(at = @At("TAIL"), method = "placeNewPlayer")
    public void onPlayerConnect(Connection connection, ServerPlayer player, CommonListenerCookie clientData, CallbackInfo ci) {
        if (
                Config.INSTANCE.enabled
                        && Config.INSTANCE.motd != null // check if set in config file
                        && !Config.INSTANCE.motd.isEmpty())
            player.sendSystemMessage(
                    Component.literal(Config.INSTANCE.motd));
    }
}
