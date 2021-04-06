package me.modione.sgplugin.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

public class GameManager {
    private GameState state = GameState.OFF;
    private final List<Location> spawnLocations;
    private final List<Block> chests;
    private final List<Player> players = new ArrayList<>();
    private final List<Player> spectators = new ArrayList<>();
    private final World world;
    private final Map<Player, BoundingBox> assignedSpawns = new HashMap<>();
    private final Random random = new Random();
    private BukkitTask nextEvent;

    public GameManager(World world, List<Location> spawnLocations, List<Block> chests) {
        this.world = world;
        this.spawnLocations = spawnLocations;
        this.chests = chests;
    }

    public void joinGame(Player player, boolean asSpectator) {
        if(asSpectator) spectators.add(player);
        else players.add(player);
    }

    public void prepareGame() {
        this.state = GameState.PREPARED;
        players.forEach(player -> {
            Location spawn = spawnLocations.get(random.nextInt(spawnLocations.size()));
            assignedSpawns.put(player, BoundingBox.of(spawn, 1, 1, 1));
            player.teleport(spawn);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        });
        nextEvent = Utils.createCountdown(5, (format, seconds) -> {
            players.forEach(player -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "The Game is starting in " + ChatColor.RED + seconds));
            });
        }, this::startGame);
    }

    private void startGame() {
        this.state = GameState.LOOT;
        world.setPVP(false);
        players.forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
            player.sendTitle(ChatColor.GREEN + "Go!!!", ChatColor.YELLOW + "Let the Survival Games Begin!", 10, 60, 15);
        });
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.GREEN + "The Game has been started!");
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.YELLOW + "PVP will be enabled in 1 Minute!");
        nextEvent = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, () -> {
            players.forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1, 1));
            nextEvent = Utils.createCountdown(10, (format, seconds) -> {
                players.forEach(player -> {
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.BLUE + "PvP in " + ChatColor.YELLOW + seconds + ChatColor.BLUE + " seconds!"));
                });
            }, () -> {
                state = GameState.PVP;
                world.setPVP(true);
                Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.GREEN + "PvP is now enabled!");
                players.forEach(player -> {
                    player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
                    player.sendTitle(ChatColor.AQUA + "Fight!", ChatColor.BLUE + "PvP is now enabled!", 10, 60, 15);
                });
                nextEvent = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, this::cancelGame, 12000);
            });
        }, 1100);
    }

    public void cancelGame() {
        world.setPVP(true);
        if(nextEvent != null) nextEvent.cancel();
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + "The game has been cancelled!");
        players.forEach(player -> player.sendTitle(ChatColor.RED + "Game over!", ChatColor.YELLOW + "Sorry, no winner this time!", 10, 60, 15));
        nextEvent = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, () -> {
            players.forEach(player -> {
                player.setGameMode(Bukkit.getDefaultGameMode());
                player.teleport(assignedSpawns.get(player).getCenter().toLocation(world));
            });
        }, 600);
    }

    public void endGame(Player winner) {
        state = GameState.END;
        winner.sendTitle(ChatColor.GREEN + "You won!", ChatColor.YELLOW + "You are the last person alive!", 10, 60, 15);
        for(int i = 0; i < 10; i++) {
            Firework fw = (Firework) world.spawnEntity(winner.getLocation(), EntityType.FIREWORK);
            FireworkMeta meta = fw.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().withFlicker().withTrail().withColor(Color.RED, Color.GREEN).withFade(Color.YELLOW).build());
            fw.setFireworkMeta(meta);
        }
        players.forEach(player -> player.sendTitle(ChatColor.RED + "Game over!", ChatColor.YELLOW + "You didn't win :/!", 10, 60, 15));
        this.cancelGame();
    }

    public GameState getGameState() {
        return state;
    }

    public Map<Player, BoundingBox> getAssignedSpawns() {
        return assignedSpawns;
    }

    public enum GameState {
        OFF, PREPARED, LOOT, PVP, END
    }
}
