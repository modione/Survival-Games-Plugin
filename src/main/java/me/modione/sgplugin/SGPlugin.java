package me.modione.sgplugin;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.logging.Level;
import me.modione.sgplugin.game.GameManager;
import me.modione.sgplugin.utils.ArenaConfig;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public final class SGPlugin extends JavaPlugin {
    public static final Random random = new Random();
    public static SGPlugin INSTANCE;
    public static final String prefix = (ChatColor.GOLD+"["+ChatColor.DARK_AQUA+"SG"+ChatColor.GOLD+"] ");
    public GameManager gameManager;

    public SGPlugin() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        File dataFolder = getDataFolder();
        dataFolder.mkdirs();
        File configFile = dataFolder.toPath().resolve("config.yml").toFile();
        ArenaConfig cfg = null;
        if(configFile.exists()) {
            try {
                FileConfiguration config = getConfig();
                config.load(configFile);
                cfg = config.getObject("arena", ArenaConfig.class);
                assert cfg != null;
            } catch(Exception e) {
                getLogger().log(Level.SEVERE, "An error occurred while loading the config! Please check config.yml for Errors or report the it on the Bug page!", e);
            }
        }
        if(cfg == null) {
            cfg = ArenaConfig.getDefault();
            try {
                configFile.createNewFile();
                FileConfiguration config = getConfig();
                config.set("arena", cfg);
                config.save(configFile);
            } catch (IOException e) {
                getLogger().log(Level.SEVERE, "An error occurred while loading the config! Please check config.yml for Errors or report the it on the Bug page!", e);
            }
        }
        gameManager = new GameManager(Bukkit.getWorld(cfg.world), cfg.spawnLocations, cfg.chests, cfg.loot, cfg.arena, cfg.lobbyLocation);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(text);
    }
}
