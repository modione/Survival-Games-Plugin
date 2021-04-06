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
    private final Random random = new Random();
    private final BoundingBox arena;
    private final Location lobbyLocation;
    private BukkitTask nextEvent;

    public GameManager(World world, List<Location> spawnLocations, List<Block> chests, BoundingBox arena, Location lobbyLocation) {
        this.world = world;
        this.spawnLocations = spawnLocations;
        this.chests = chests;
        this.arena = arena;
        this.lobbyLocation = lobbyLocation;
    }

    public void joinGame(Player player, boolean asSpectator) {
        if(asSpectator) spectators.add(player);
        else players.add(player);
    }

    private void startGame() {
        this.state = GameState.LOOT;

        nextEvent = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, () -> {

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

    public List<Location> getSpawnLocations() {
        return spawnLocations;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public World getWorld() {
        return world;
    }

    public void skipToState(GameState state) {
        this.state = state;
    }

    public enum GameState {
        OFF, PREPARED, LOOT, PVP, END
    }
}
