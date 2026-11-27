package fr.juke.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;

public class Config {
    private static final Logger LOGGER = LogManager.getLogger("fabric-tab");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "fabric-tab.json";

    public static final Config INSTANCE = new Config();

    public int configVersion = 1;
    public String header = "§7§l§oCOOL#N      SERVER       #N#N§6very cool§f#N";
    public String footer = "#N§b#TPS tps - #MSPT mspt - #PLAYERCOUNT players - #UPTIME uptime#N";
    public String motd = "§7§oWelcome to the server.§f";

    public void save() throws IOException {
        Path config = path();
        Files.createDirectories(config.getParent());

        Path temp = config.resolveSibling(FILE_NAME + ".tmp");
        try (Writer writer = Files.newBufferedWriter(temp, StandardCharsets.UTF_8)) {
            GSON.toJson(this, writer);
        }

        try {
            Files.move(temp, config, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException e) {
            Files.move(temp, config, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    public void load() {
        Path config = path();
        try {
            if (Files.isDirectory(config)) {
                Files.delete(config);
            }

            if (Files.notExists(config)) {
                save();
                return;
            }

            Config read;
            try (Reader reader = Files.newBufferedReader(config, StandardCharsets.UTF_8)) {
                read = GSON.fromJson(reader, Config.class);
            }
            if (read == null) {
                throw new IOException("Configuration file is empty");
            }

            this.header = read.header != null ? read.header : this.header;
            this.footer = read.footer != null ? read.footer : this.footer;
            this.motd = read.motd != null ? read.motd : this.motd;

            if (read.configVersion != this.configVersion) {
                this.configVersion = read.configVersion;
                save();
            }
        } catch (Exception e) {
            LOGGER.error("Could not load {}, using defaults", config, e);
            backup(config);

            this.configVersion = 1;
            this.header = "§7§l§oCOOL#N      SERVER       #N#N§6very cool§f#N";
            this.footer = "#N§b#TPS tps - #MSPT mspt - #PLAYERCOUNT players - #UPTIME uptime#N";
            this.motd = "§7§oWelcome to the server.§f";
            saveQuietly();
        }
    }

    private static Path path() {
        return FabricLoader.getInstance().getConfigDirectory().toPath().resolve(FILE_NAME);
    }

    private void saveQuietly() {
        try {
            save();
        } catch (IOException e) {
            LOGGER.error("Could not save configuration file", e);
        }
    }

    private static void backup(Path config) {
        try {
            Path backup = config.resolveSibling(FILE_NAME + ".broken-" + Instant.now().toEpochMilli());
            Files.move(config, backup, StandardCopyOption.REPLACE_EXISTING);
            LOGGER.warn("Moved unreadable configuration file to {}", backup);
        } catch (IOException e) {
            LOGGER.warn("Could not back up unreadable configuration file", e);
        }
    }
}
