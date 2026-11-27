package fr.juke;

import fr.juke.config.Config;
import fr.juke.util.TablistVariables;
import fr.juke.util.TextFormatter;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FabricTab implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(FabricTab.class);

    private static int updateTimer;

    @Override
    public void onInitialize() {
        Config.INSTANCE.load();

        ServerTickEvents.END_SERVER_TICK.register(FabricTab::onServerTick);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            if (!Config.INSTANCE.enabled) {
                return;
            }

            String motd = Config.INSTANCE.motd;
            if (motd != null && !motd.isEmpty()) {
                handler.player.sendMessage(Text.literal(motd), false);
            }
        });

        LOGGER.info("Loaded fabric-tab");
    }

    private static void onServerTick(MinecraftServer server) {
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

        server.getPlayerManager().sendToAll(new PlayerListHeaderS2CPacket(
                Text.literal(TextFormatter.tablistChars(orEmpty(Config.INSTANCE.header))),
                Text.literal(TextFormatter.tablistChars(orEmpty(Config.INSTANCE.footer)))));
    }

    private static String orEmpty(String value) {
        return value == null ? "" : value;
    }
}
