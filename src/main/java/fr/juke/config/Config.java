package fr.juke.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.logging.LogUtils;
import net.neoforged.fml.loading.FMLPaths;
import org.slf4j.Logger;

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
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final String FILE_NAME = "fabric-tab.json";

    public static final Config INSTANCE = new Config();

    public int configVersion; // v0
    public String header; // null
    public String footer; // null
    public String motd; // null
    public int refreshTickInterval; // 0
    public boolean enabled; // false

    public void setDefaults() {
        if (this.configVersion < 1) {
            this.configVersion = 1;
            this.header = "§7§l§oCOOL#N      SERVER       #N#N§6very cool§f#N";
            this.footer = "#N§b#TPS tps - #MSPT mspt - #PLAYERCOUNT players - #UPTIME uptime#N";
            this.motd = "§7§oWelcome to the server.§f";
            saveQuietly();
            LOGGER.info("Migrated configuration file to v1");
        }

        if (this.configVersion < 2) {
            this.configVersion = 2;
            this.enabled = true;
            this.refreshTickInterval = 20;
            saveQuietly();
            LOGGER.info("Migrated configuration file to v2");
        }
    }

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
                setDefaults();
                return;
            }

            Config read;
            try (Reader reader = Files.newBufferedReader(config, StandardCharsets.UTF_8)) {
                read = GSON.fromJson(reader, Config.class);
            }
            if (read == null) {
                throw new IOException("Configuration file is empty");
            }

            this.configVersion = read.configVersion;
            this.header = read.header;
            this.footer = read.footer;
            this.motd = read.motd;
            this.refreshTickInterval = read.refreshTickInterval;
            this.enabled = read.enabled;

            setDefaults();
            if (this.refreshTickInterval <= 0) {
                this.refreshTickInterval = 20;
            }
        } catch (Exception e) {
            LOGGER.error("Could not load {}, using defaults", config, e);
            backup(config);

            this.configVersion = 0;
            this.header = null;
            this.footer = null;
            this.motd = null;
            this.refreshTickInterval = 0;
            this.enabled = false;
            setDefaults();
        }
    }

    private static Path path() {
        return FMLPaths.CONFIGDIR.get().resolve(FILE_NAME);
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
