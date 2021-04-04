package me.modione.sgplugin.listener;

import me.modione.sgplugin.SGPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class IngameListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        // TODO Read gamestate
        Player player = event.getPlayer();
        if (Bukkit.getOnlinePlayers().size() > SGPlugin.INSTANCE.locations.size()) {
            if (player.isOp()) return;
            player.kickPlayer(ChatColor.RED + "The Game is full!");
        }
    }
}
