package me.modione.sgplugin.listener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.Events;
import me.modione.sgplugin.utils.LootGenerator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.BlockInventoryHolder;

public class onGameStart implements Listener {
    public static boolean gamestarted;
    public static boolean gameprepared;
    public static Random random = new Random();
    public static List<Player> playersig = new ArrayList<>();
    public static List<Player> spectator = new ArrayList<>();
    public static HashMap<Location, Material> oldnewblock = new HashMap<>();
    public static HashMap<Player, Location> tplocs = new HashMap<>();
    public static World world;
    public static int id;

    public static void PrepareGame() {
        gameprepared = true;
        List<String> sprueche = Arrays.asList("Go away :(", "Game is full!", "Imagine not being able to play", "this server is too small for us two", "Niemand mag dich", "BigMac bannt dich weg", "You are banned from this server", "https://www.youtube.com/watch?v=dQw4w9WgXcQ");

        world = SGPlugin.INSTANCE.locations.get(0).getWorld();
        assert world != null;
        world.setPVP(false);
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            // Goes through all online players and kicks them if there are less locations than online players
            Collection<? extends Player> online = Bukkit.getOnlinePlayers();
            if (!onlinePlayer.isOp() && online.size() > SGPlugin.INSTANCE.locations.size()) {
                onlinePlayer.kickPlayer(sprueche.get(random.nextInt(sprueche.size())));
            } else if (onlinePlayer.isOp() && online.size() > SGPlugin.INSTANCE.locations.size()) {
                onlinePlayer.sendMessage(ChatColor.RED + sprueche.get(random.nextInt(sprueche.size())));
            } else {
                //Gets executed when a players "joins" the game
                Location loc = SGPlugin.INSTANCE.locations.get(random.nextInt(SGPlugin.INSTANCE.locations.size()));
                while (tplocs.containsValue(loc)) {
                    loc = SGPlugin.INSTANCE.locations.get(random.nextInt(SGPlugin.INSTANCE.locations.size()));
                }
                Location loc2place = loc.clone().add(0, -2, 0);
                tplocs.put(onlinePlayer, loc);
                onlinePlayer.teleport(loc);
                onlinePlayer.getInventory().clear();
                oldnewblock.put(loc2place, loc2place.getBlock().getType());
                loc2place.getBlock().setType(Material.GLASS);
                onlinePlayer.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
                onlinePlayer.setGameMode(GameMode.SURVIVAL);
                //noinspection deprecation
                onlinePlayer.setHealth(onlinePlayer.getMaxHealth());
                onlinePlayer.setFoodLevel(40);
            }
        }
        playersig.addAll(tplocs.keySet());
        AtomicInteger count = new AtomicInteger(5);
        for (Location location : SGPlugin.INSTANCE.chests) {
            Block block = Objects.requireNonNull(location.getWorld()).getBlockAt(location);
            if (block.getType() != Material.CHEST) block.setType(Material.CHEST);
            LootGenerator.generateChestLoot((Chest) block.getState());
        }
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(SGPlugin.INSTANCE, () -> {
            for (Player player : playersig) {
                BaseComponent component = new TextComponent(ChatColor.GREEN + "Starting Game in: " + ChatColor.RED + count.get());
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, component);
            }
            count.getAndDecrement();
        }, 20, 20);
        Bukkit.getScheduler().scheduleSyncDelayedTask(SGPlugin.INSTANCE, () -> {
            StartGame();
            Bukkit.getScheduler().cancelTask(id);
            for (Location loc : oldnewblock.keySet()) {
                loc.getBlock().setType(oldnewblock.get(loc));
            }
            Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + "PVP will be enabled in 1 minute");
        }, 120);
    }

    public static void StartGame() {
        gamestarted = true;
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + "A Game has started!");
        onGameEnd.end = false;
        onGameEnd.ListenforEnd();
        Events.startEvents();
    }

    @EventHandler
    public void on_Break(BlockBreakEvent event) {
        if (playersig.contains(event.getPlayer()) && gameprepared) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void on_Move(PlayerMoveEvent event) {
        if (gameprepared && !gamestarted && playersig.contains(event.getPlayer())) {
            if(event.getTo() == null) return;
            Location spawn = tplocs.get(event.getPlayer());
            double distX = spawn.getX() - event.getTo().getX();
            double distY = spawn.getY() - event.getTo().getY();
            double distZ = spawn.getZ() - event.getTo().getZ();
            if(-0.5 > distX || distX > 0.5 || -2.0 > distY || distY < 2.0 || -0.5 > distZ || distZ > 0.5) {
                event.getPlayer().teleport(spawn);
            }
        }
    }

    @EventHandler
    public void oninv_open(InventoryOpenEvent event) {
        if (gameprepared && !gamestarted && playersig.contains(event.getPlayer()) && event.getInventory().getHolder() instanceof BlockInventoryHolder)
            event.setCancelled(true);
    }

    @EventHandler
    public void on_place(BlockPlaceEvent event) {
        if (gamestarted && playersig.contains(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void interact(PlayerInteractEvent event) {
        if (gameprepared && !gamestarted && playersig.contains(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void ondmg(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (gameprepared && !gamestarted) {
            event.setCancelled(true);
        }
        if ((event.getDamage()>=((Player) event.getEntity()).getHealth())) return;
        Player player = (Player) event.getEntity();
        if (gamestarted && playersig.contains(player)) {
            playersig.remove(player);
            player.spigot().respawn();
            player.setGameMode(GameMode.SPECTATOR);
            spectator.add(player);
            Bukkit.broadcastMessage(ChatColor.GREEN+event.getDamager().getName()+ChatColor.RED+" killed "+ChatColor.BLUE+event.getEntity().getName());
        }
    }
    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if (onGameStart.gamestarted&&playersig.contains(event.getEntity())) {
            event.setDeathMessage(null);
        }
    }
    @EventHandler
    public void onSpawn(EntitySpawnEvent event) {
        if (!(event.getEntity() instanceof LivingEntity)) return;
        if (gameprepared) {
            event.setCancelled(true);
        }
    }
}
