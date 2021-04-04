package me.modione.sgplugin.listener;

import me.modione.sgplugin.SGPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;

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
}
