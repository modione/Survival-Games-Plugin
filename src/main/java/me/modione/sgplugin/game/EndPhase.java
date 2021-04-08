package me.modione.sgplugin.game;

import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.base.GamePhase;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.meta.FireworkMeta;

public class EndPhase extends GamePhase {

    public EndPhase(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void onStart() {
        if(gameManager.getPlayers().size() != 1) {
            Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.RED + "An error occurred in this SG-Game!");
            Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, gameManager::cancelGame, 5 * 20);
            return;
        }
        Player winner = gameManager.getSpectators().get(0);
        winner.sendTitle(ChatColor.GREEN + "You won!", ChatColor.YELLOW + "You are the last person alive!", 10, 60, 15);
        for(int i = 0; i < 10; i++) {
            Firework fw = (Firework) gameManager.getWorld().spawnEntity(winner.getLocation(), EntityType.FIREWORK);
            FireworkMeta meta = fw.getFireworkMeta();
            meta.addEffect(
                FireworkEffect.builder().withFlicker().withTrail().withColor(Color.RED, Color.GREEN).withFade(Color.YELLOW).build());
            fw.setFireworkMeta(meta);
        }
        gameManager.getSpectators().forEach(player -> player.sendTitle(ChatColor.RED + "Game over!", ChatColor.GREEN + winner.getName() + ChatColor.YELLOW + " won the game!", 10, 60, 15));
    }

    @Override
    public void onEnd() {
        Player winner = gameManager.getSpectators().get(0);
        winner.setGameMode(Bukkit.getDefaultGameMode());
        winner.teleport(gameManager.getLobbyLocation());
        gameManager.getSpectators().forEach(player -> {
            player.setGameMode(Bukkit.getDefaultGameMode());
            player.teleport(gameManager.getLobbyLocation());
        });
    }

    @EventHandler
    public void omEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(gameManager.getPlayers().contains(p)) event.setCancelled(true);
        }
    }
}
