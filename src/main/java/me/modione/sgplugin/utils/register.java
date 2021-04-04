package me.modione.sgplugin.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.commands.AddChestCommand;
import me.modione.sgplugin.commands.AddItemCommand;
import me.modione.sgplugin.commands.AddLocationCommand;
import me.modione.sgplugin.commands.GameEndCommand;
import me.modione.sgplugin.commands.ListItemsCommand;
import me.modione.sgplugin.commands.RemoveItemCommand;
import me.modione.sgplugin.commands.StartGameCommand;
import me.modione.sgplugin.listener.Listener;
import me.modione.sgplugin.listener.onGameEnd;
import me.modione.sgplugin.listener.onGameStart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.plugin.PluginManager;

public class register {
    public register() {
        PluginManager manager = SGPlugin.INSTANCE.getServer().getPluginManager();
        File chests = new File(SGPlugin.INSTANCE.path + "chests.json");
        File locations = new File(SGPlugin.INSTANCE.path + "locations.json");
        try {
            LootGenerator_old.standartLoot = new ArrayList<>();
            FileConfig loot = new FileConfig("loottable.yml");
            for (int i = 0; i < 1000; i++) {
                if (!loot.contains(String.valueOf(i))) break;
                LootGenerator_old.standartLoot.add(loot.getItemStack(String.valueOf(i)));
            }
            Gson gson = new Gson();
            List<JsonObject> chestss = gson.fromJson(new FileReader(chests), new TypeToken<List<JsonObject>>() {
            }.getType());
            SGPlugin.INSTANCE.locations = new ArrayList<>();
            SGPlugin.INSTANCE.chests = new ArrayList<>();
            for (JsonObject o : chestss) {
                Location location = new Location(Bukkit.getWorld(o.get("world").getAsString())
                        , o.get("x").getAsInt(), o.get("y").getAsInt(), o.get("z").getAsInt());
                SGPlugin.INSTANCE.chests.add(location);
            }
            List<JsonObject> locationss = gson.fromJson(new FileReader(locations), new TypeToken<List<JsonObject>>() {
            }.getType());
            for (JsonObject o : locationss) {
                Location location = new Location(Bukkit.getWorld(o.get("world").getAsString())
                        , o.get("x").getAsInt(), o.get("y").getAsInt(), o.get("z").getAsInt());
                SGPlugin.INSTANCE.locations.add(location);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            SGPlugin.INSTANCE.log(SGPlugin.prefix + ChatColor.RED + "An error occurred while reading the files.");
            SGPlugin.INSTANCE.locations = new ArrayList<>();
            SGPlugin.INSTANCE.chests = new ArrayList<>();
            LootGenerator_old.standartLoot = new ArrayList<>();
        }
        SGPlugin.INSTANCE.startable = chests.exists() && locations.exists() && !SGPlugin.INSTANCE.chests.isEmpty() && !SGPlugin.INSTANCE.locations.isEmpty() && !LootGenerator_old.standartLoot.isEmpty();
        manager.registerEvents(new Listener(), SGPlugin.INSTANCE);
        manager.registerEvents(new onGameStart(), SGPlugin.INSTANCE);
        manager.registerEvents(new Events(), SGPlugin.INSTANCE);
        Objects.requireNonNull(Bukkit.getPluginCommand("addchest")).setExecutor(new AddChestCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("addlocation")).setExecutor(new AddLocationCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("start")).setExecutor(new StartGameCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("gameend")).setExecutor(new GameEndCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("additem")).setExecutor(new AddItemCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("removeitem")).setExecutor(new RemoveItemCommand());
        Objects.requireNonNull(Bukkit.getPluginCommand("listitems")).setExecutor(new ListItemsCommand());
        Bukkit.getScheduler().scheduleSyncRepeatingTask(SGPlugin.INSTANCE, () -> {
            SGPlugin.INSTANCE.startable = !SGPlugin.INSTANCE.chests.isEmpty() && !SGPlugin.INSTANCE.locations.isEmpty() && !LootGenerator_old.standartLoot.isEmpty();
            if (!onGameEnd.end) return;
            onGameEnd.end = false;
            Bukkit.getScheduler().cancelTask(onGameEnd.id);
        }, 10, 5);
        if (!SGPlugin.INSTANCE.startable) {
            SGPlugin.INSTANCE.log(SGPlugin.prefix + ChatColor.RED + "[ERROR] you can't play this plugin due to missing information about chests, loot-table or locations");
            System.out.println("Locations are available: " + !SGPlugin.INSTANCE.locations.isEmpty());
            System.out.println("Positions are available: " + !SGPlugin.INSTANCE.locations.isEmpty());
            System.out.println("Loot-Table items are available: " + !LootGenerator_old.standartLoot.isEmpty());
        }
    }
}
