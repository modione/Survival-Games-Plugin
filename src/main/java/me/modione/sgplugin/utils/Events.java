package me.modione.sgplugin.utils;

import static me.modione.sgplugin.listener.onGameStart.playersig;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.listener.onGameStart;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.BlockInventoryHolder;

public class Events implements Listener {
    public static boolean running = false;
    private static int id;
    private static int id2;
    private static int id3;
    private static final HashMap<Block, ArmorStand> refills = new HashMap<>();

     public static void startEvents() {
        running = true;
        AtomicInteger atomicInteger = new AtomicInteger(10);
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(SGPlugin.INSTANCE, () -> {
            for (Player player : playersig) {
                BaseComponent component = new TextComponent(ChatColor.BLUE + "PVP will be enabled in: " + atomicInteger.get());
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }
            atomicInteger.getAndDecrement();
        }, 1000, 20);
        id2 = Bukkit.getScheduler().scheduleSyncDelayedTask(SGPlugin.INSTANCE, () -> {
            Bukkit.broadcastMessage(ChatColor.RED + "PVP is now enabled");
            onGameStart.world.setPVP(true);
            Bukkit.getScheduler().cancelTask(id);
        }, 1200);
        AtomicInteger integer = new AtomicInteger(120);
        id3 = Bukkit.getScheduler().scheduleSyncRepeatingTask(SGPlugin.INSTANCE, () -> {
            if (integer.get() == 0) {
                for (Block block : refills.keySet()) {
                    Chest chest = (Chest) block.getState();
                    LootGenerator_old.generateChestLoot(chest);
                }
                for (ArmorStand stand : refills.values()) {
                    stand.remove();
                }
            }
            String count;
            if (integer.get() == 120) {
                count = "2:00";
            } else if (integer.get() >= 60) {
                count = ("1:" + (integer.get() - 60));
            } else {
                count = String.valueOf(integer.get());
            }
            for (ArmorStand stand : refills.values()) {
                stand.setCustomName(count);
            }
            integer.getAndDecrement();
        }, 0, 20);
    }

    public static void Terminate() {
        running = false;
        Bukkit.getScheduler().cancelTask(id);
        Bukkit.getScheduler().cancelTask(id2);
        Bukkit.getScheduler().cancelTask(id3);
        for (ArmorStand value : refills.values()) {
            value.remove();
        }
        refills.clear();
    }

    @EventHandler
    public void on_Open(InventoryOpenEvent event) {
        if (!(event.getInventory().getHolder() instanceof BlockInventoryHolder)) return;
        if (running && playersig.contains(event.getPlayer())) {
            Chest chest = (Chest) ((BlockInventoryHolder) event.getInventory().getHolder()).getBlock().getState();
            ArmorStand stand = (ArmorStand) chest.getWorld().spawnEntity(chest.getLocation(), EntityType.ARMOR_STAND);
            stand.setInvulnerable(true);
            stand.setCollidable(false);
            stand.setGravity(false);
            stand.setInvisible(true);
            refills.put(chest.getBlock(), stand);
        }
    }

    @EventHandler
    public void on_Stop(PluginDisableEvent event) {
        if (event.getPlugin() == SGPlugin.INSTANCE) {
            for (ArmorStand stand : refills.values()) {
                stand.remove();
            }
        }
    }
}
