package me.modione.sgplugin.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.BoundingBox;

public class ArenaConfig {
    public List<ItemStack> loot;
    public List<Location> spawnLocations;
    public List<Block> chests;
    public String world;
    public BoundingBox arena;
    public Location lobbyLocation;

    public static ArenaConfig getDefault() {
        ArenaConfig config = new ArenaConfig();
        config.loot = new ArrayList<>(Collections.singletonList(
            new ItemBuilder(Material.DIRT).displayname(ChatColor.RED + "Test-Dirt")
                .lore("Imagine having dirt!").build()));
        config.spawnLocations = new ArrayList<>(Collections.singletonList(Bukkit.getWorlds().get(0).getSpawnLocation()));
        config.chests = new ArrayList<>(Collections.singletonList(Bukkit.getWorlds().get(0).getSpawnLocation())).stream().map(Location::getBlock).collect(
            Collectors.toList());
        config.world = Bukkit.getWorlds().get(0).getName();
        config.arena = new BoundingBox(-1, -1, -1, 1, 1, 1);
        config.lobbyLocation = Bukkit.getWorlds().get(0).getSpawnLocation();
        return config;
    }
}
