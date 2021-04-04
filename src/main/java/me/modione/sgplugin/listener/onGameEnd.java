package me.modione.sgplugin.listener;

import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.Events;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class onGameEnd {
    public static boolean end = false;
    public static int id;

    public static void EndGame() {
        onGameStart.gameprepared = false;
        onGameStart.gamestarted = false;
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendTitle(ChatColor.RED + "The game ended", ":(", 5, 20, 5);
        }
        onGameStart.world.setPVP(true);
        end = true;
        Events.Terminate();
        onGameStart.tplocs.clear();
    }

    public static void ListenforEnd() {
        id = Bukkit.getScheduler().scheduleSyncRepeatingTask(SGPlugin.INSTANCE, () -> {
            if (!onGameStart.gamestarted) return;
            if (onGameStart.playersig.size() == 1) {
                Player winner = onGameStart.playersig.get(0);
                onGameStart.playersig.clear();
                for (Player player : onGameStart.spectator) {
                    player.teleport(winner.getLocation());
                }
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.sendTitle(ChatColor.DARK_AQUA + winner.getName(), ChatColor.UNDERLINE + "won the game!", 5, 20, 5);
                }
                for (int i = 0; i < 10; i++) {
                    winner.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
                }
                if (Events.running) {
                    Events.Terminate();
                }
                Bukkit.broadcastMessage(ChatColor.RED + winner.getName() + " won the game!");
                Bukkit.getScheduler().cancelTask(onGameStart.id);
                onGameStart.gameprepared = false;
                onGameStart.gamestarted = false;
                end = true;
                onGameStart.world.setPVP(true);
                onGameStart.tplocs.clear();
            }
        }, 5, 5);
    }
}
