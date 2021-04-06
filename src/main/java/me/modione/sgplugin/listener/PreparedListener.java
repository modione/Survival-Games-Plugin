package me.modione.sgplugin.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PreparedListener implements Listener {
    public static PreparedListener INSTANCE =  new PreparedListener();

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

    }
}
