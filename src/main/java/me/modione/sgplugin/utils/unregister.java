package me.modione.sgplugin.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import me.modione.sgplugin.SGPlugin;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class unregister {
    public unregister() {
        File path = new File(SGPlugin.INSTANCE.path);
        File chests = new File(SGPlugin.INSTANCE.path + "chests.json");
        File locations = new File(SGPlugin.INSTANCE.path + "locations.json");
        try {
            Gson gson = new Gson();
            FileConfig loot = new FileConfig("loottable.yml");
            List<JsonObject> chestss = new ArrayList<>();
            List<JsonObject> locationss = new ArrayList<>();
            for (Location location : SGPlugin.INSTANCE.locations) {
                JsonObject object = new JsonObject();
                object.addProperty("x", location.getX());
                object.addProperty("y", location.getY());
                object.addProperty("z", location.getZ());
                object.addProperty("world", Objects.requireNonNull(location.getWorld()).getName());
                locationss.add(object);
            }
            for (Location location : SGPlugin.INSTANCE.chests) {
                JsonObject object = new JsonObject();
                object.addProperty("x", location.getBlockX());
                object.addProperty("y", location.getBlockY());
                object.addProperty("z", location.getBlockZ());
                object.addProperty("world", Objects.requireNonNull(location.getWorld()).getName());
                chestss.add(object);
            }
            for (ItemStack item : LootGenerator.standartLoot) {
                loot.set(String.valueOf(LootGenerator.standartLoot.indexOf(item)), item);
            }
            loot.saveconfig();
            path.mkdirs();
            chests.createNewFile();
            locations.createNewFile();
            FileOutputStream chest = new FileOutputStream(chests);
            FileOutputStream loc = new FileOutputStream(locations);
            String chestsss = gson.toJson(chestss);
            String locationssss = gson.toJson(locationss);
            chest.write(chestsss.getBytes());
            loc.write(locationssss.getBytes());
            chest.close();
            loc.close();
        } catch (IOException e) {
            System.out.println("There was an Error while writing the files :(");
        }
    }
}