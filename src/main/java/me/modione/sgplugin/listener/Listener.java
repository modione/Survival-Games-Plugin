package me.modione.sgplugin.listener;

import static me.modione.sgplugin.listener.onGameStart.playersig;

import me.modione.sgplugin.SGPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listener implements org.bukkit.event.Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (!onGameStart.gameprepared) return;
        Player player = event.getPlayer();
        if (Bukkit.getOnlinePlayers().size() > SGPlugin.INSTANCE.locations.size()) {
            if (player.isOp()) return;
            player.kickPlayer(ChatColor.RED + "The Game is full!");
        }
    }
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (onGameStart.gamestarted&&playersig.contains(event.getPlayer())) playersig.remove(event.getPlayer());
    }
}
