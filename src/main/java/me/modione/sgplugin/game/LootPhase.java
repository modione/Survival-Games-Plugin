package me.modione.sgplugin.game;

import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.base.GamePhase;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LootPhase extends GamePhase {

    public LootPhase(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void onStart() {
        if(gameManager.getPlayers().size() <= 1) {
            Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + "You need at least 2 Players in Order to play a game!");
            gameManager.cancelGame();
            return;
        }
        gameManager.getWorld().setPVP(false);
        gameManager.getPlayers().forEach(player -> {
            player.setGameMode(GameMode.SURVIVAL);
            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_AMBIENT, 1, 1);
            player.sendTitle(ChatColor.GREEN + "Go!!!", ChatColor.YELLOW + "Let the Survival Games Begin!", 10, 60, 15);
        });
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.GREEN + "The Game has been started!");
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.YELLOW + "PVP will be enabled in 1 Minute!");
        Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, this::next, 1100);
    }

    @Override
    public void onEnd() {}

    @EventHandler
    public void omEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(gameManager.getPlayers().contains(p)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(gameManager.getPlayers().contains(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if(gameManager.getPlayers().contains(event.getPlayer())) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + event.getPlayer().getName() + ChatColor.AQUA + " left the Game!");
        gameManager.getPlayers().remove(event.getPlayer());
        if(gameManager.getPlayers().size() <= 1) {
            next();
        }
    }
}
