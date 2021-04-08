package me.modione.sgplugin.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.base.GamePhase;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.BoundingBox;

public class GameManager {
    private int gameState = 0;
    private final List<Location> spawnLocations;
    private final List<Block> chests;
    private final List<Player> players = new ArrayList<>();
    private final List<Player> spectators = new ArrayList<>();
    private final World world;
    private final BoundingBox arena;
    private final Location lobbyLocation;
    private final List<GamePhase> phases;
    private GamePhase currentPhase;
    private BukkitTask nextEvent;

    public GameManager(World world, List<Location> spawnLocations, List<Block> chests, BoundingBox arena, Location lobbyLocation) {
        this.world = world;
        this.spawnLocations = spawnLocations;
        this.chests = chests;
        this.arena = arena;
        this.lobbyLocation = lobbyLocation;
        this.phases = Arrays.asList(
            new PreparePhase(this),
            new LootPhase(this),
            new PvPPhase(this),
            new EndPhase(this)
        );
    }

    public void joinGame(Player player, boolean asSpectator) {
        if(asSpectator) spectators.add(player);
        else {
            if(players.size() <= spawnLocations.size()) {
                player.sendMessage(SGPlugin.prefix + ChatColor.RED + "The game is full! You are now a spectator!");
                spectators.add(player);
                return;
            }
            players.add(player);
        }
    }

    public void next() {
        HandlerList.unregisterAll(currentPhase);
        if(phases.size() <= gameState++) {
            gameState = 0;
            return;
        }
        currentPhase = phases.get(gameState);
        Bukkit.getPluginManager().registerEvents(currentPhase, SGPlugin.INSTANCE);
        currentPhase.onStart();
    }

    public void cancelGame() {
        Bukkit.getScheduler().cancelTasks(SGPlugin.INSTANCE);
        world.setPVP(true);
        if(nextEvent != null) nextEvent.cancel();
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + "The game has been cancelled!");
        players.forEach(player -> player.sendTitle(ChatColor.RED + "Game over!", ChatColor.YELLOW + "Sorry, no winner this time!", 10, 60, 15));
        nextEvent = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, () -> {
            players.forEach(player -> {
                player.setGameMode(Bukkit.getDefaultGameMode());
                player.teleport(this.lobbyLocation);
            });
        }, 600);
    }

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getSpectators() {
        return spectators;
    }

    public World getWorld() {
        return world;
    }

    public Location getLobbyLocation() {
        return lobbyLocation;
    }
}
