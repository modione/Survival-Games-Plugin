package me.modione.sgplugin;

import me.modione.sgplugin.utils.register;
import me.modione.sgplugin.utils.unregister;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class SGPlugin extends JavaPlugin {
    public static SGPlugin INSTANCE;
    public String path = "plugins/SG-Plugin/";
    public Boolean startable;
    public List<Location> chests;
    public List<Location> locations;

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
