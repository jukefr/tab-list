package fr.juke;

import com.mojang.logging.LogUtils;
import fr.juke.config.Config;
import fr.juke.util.TablistVariables;
import fr.juke.util.TextFormatter;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundTabListPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

@Mod(FabricTab.MOD_ID)
public class FabricTab {
    public static final String MOD_ID = "fabric_tab";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static int updateTimer;

    public FabricTab() {
        Config.INSTANCE.load();

        NeoForge.EVENT_BUS.addListener(this::onServerTick);
        NeoForge.EVENT_BUS.addListener(this::onPlayerLogin);

        LOGGER.info("Loaded fabric-tab");
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        MinecraftServer server = event.getServer();
        TablistVariables.onTick(server);

        if (!Config.INSTANCE.enabled) {
            return;
        }

        int interval = Config.INSTANCE.refreshTickInterval <= 0
                ? 20
                : Config.INSTANCE.refreshTickInterval;

        if (++updateTimer < interval) {
            return;
        }
        updateTimer = 0;

        server.getPlayerList().broadcastAll(new ClientboundTabListPacket(
                Component.literal(TextFormatter.tablistChars(orEmpty(Config.INSTANCE.header))),
                Component.literal(TextFormatter.tablistChars(orEmpty(Config.INSTANCE.footer)))));
    }

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!Config.INSTANCE.enabled || !(event.getEntity() instanceof ServerPlayer player)) {
            return;
        }

        String motd = Config.INSTANCE.motd;
        if (motd != null && !motd.isEmpty()) {
            player.sendSystemMessage(Component.literal(motd));
        }
    }

    private static String orEmpty(String value) {
        return value == null ? "" : value;
    }
}
