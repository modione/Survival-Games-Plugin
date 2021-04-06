package me.modione.sgplugin;

import java.util.List;
import java.util.Random;
import me.modione.sgplugin.game.GameManager;
import me.modione.sgplugin.utils.register;
import me.modione.sgplugin.utils.unregister;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

public final class SGPlugin extends JavaPlugin {
    public static final Random random = new Random();
    public static SGPlugin INSTANCE;
    public static final String prefix = (ChatColor.GOLD+"["+ChatColor.DARK_AQUA+"SG"+ChatColor.GOLD+"] ");
    public String path = "plugins/SG-Plugin/";
    public Boolean startable;
    public List<Location> chests;
    public List<Location> locations;
    public GameManager gameManager;

    public SGPlugin() {
        INSTANCE = this;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        new register();
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        new unregister();
    }

    public void log(String text) {
        Bukkit.getConsoleSender().sendMessage(text);
    }
}
