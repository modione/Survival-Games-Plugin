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
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Note;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class GameManager {
    private GameState state = GameState.OFF;
    private List<Location> spawnLocations;
    private List<Block> chests;
    private List<Player> players = new ArrayList<>();
    private List<Player> spectators = new ArrayList<>();
    private BukkitTask task;
    private World world;
    private Map<Player, Location> assignedSpawns = new HashMap<>();
    private Random random = new Random();

    public GameManager(World world) {

    }

    public void joinGame(Player player, boolean asSpecator) {
        if(asSpecator) spectators.add(player);
        else players.add(player);
    }

    public void prepareGame() {
        this.state = GameState.PREPARED;
        players.forEach(player -> {
            Location spawn = spawnLocations.get(random.nextInt(spawnLocations.size()));
            assignedSpawns.put(player, spawn);
            player.teleport(spawn);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        });
        Utils.createCountdown(5, (format, seconds) -> {
            players.forEach(player -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GREEN + "The Game is starting in " + ChatColor.RED + seconds));
            });
        }, this::startGame);
        world.setPVP(false);
    }

    private void startGame() {
        this.state = GameState.PREP;
        players.forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
            player.sendTitle(ChatColor.GREEN + "Go!!!", ChatColor.YELLOW + "Let the Survival Games Begin!", 10, 60, 15);
        });
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.GREEN + "The Game has been started!");
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.YELLOW + "PVP will be enabled in 1 Minute!");
        this.task = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, () -> {
            players.forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1, 1));
            Utils.createCountdown(10, (format, seconds) -> {
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
            });
        }, 1100);
    }

    public GameState getGameState() {
        return state;
    }

    public enum GameState {
        OFF, PREPARED, PREP, PVP, END
    }
}
