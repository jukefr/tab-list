package fr.juke;

import fr.juke.commands.util.TablistVariables;
import fr.juke.commands.util.TextFormatter;
import fr.juke.config.Config;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.network.packet.s2c.play.PlayerListHeaderS2CPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main implements ModInitializer {
    public static final Logger LOGGER = LogManager.getLogger(Main.class);

    @Override
    public void onInitialize() {
        Config.INSTANCE.load();

        ServerTickEvents.END_SERVER_TICK.register(Main::onServerTick);
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            String motd = Config.INSTANCE.motd;
            if (motd != null && !motd.isEmpty()) {
                handler.player.sendMessage(Text.literal(motd), false);
            }
        });

        LOGGER.info("Loaded fabric-tab");
    }

    private static void onServerTick(MinecraftServer server) {
        TablistVariables.onTick(server);
        server.getPlayerManager().sendToAll(new PlayerListHeaderS2CPacket(
                Text.literal(TextFormatter.tablistChars(orEmpty(Config.INSTANCE.header))),
                Text.literal(TextFormatter.tablistChars(orEmpty(Config.INSTANCE.footer)))));
    }

    private static String orEmpty(String value) {
        return value == null ? "" : value;
    }
}
