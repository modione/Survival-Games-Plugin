package me.modione.sgplugin.game;

import java.util.HashMap;
import java.util.Map;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.base.GamePhase;
import me.modione.sgplugin.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.util.BoundingBox;

public class PreparePhase extends GamePhase {
    private final Map<Player, BoundingBox> assignedSpawns = new HashMap<>();

    public PreparePhase(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public void onStart() {
        gameManager.getWorld().setPVP(false);
        gameManager.getPlayers().forEach(player -> {
            Location spawn = gameManager.getSpawnLocations().get(SGPlugin.random.nextInt(gameManager.getSpawnLocations().size()));
            assignedSpawns.put(player, BoundingBox.of(spawn, 1, 1, 1));
            player.teleport(spawn);
            player.setGameMode(GameMode.ADVENTURE);
            player.setFoodLevel(40);
            player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            player.playSound(player.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        });
        Utils.createCountdown(5, (format, seconds) -> {
            gameManager.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1, 1);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.GREEN + "The Game is starting in " + ChatColor.RED + seconds));
            });
        }, this::next);
    }

    @Override
    public void onEnd() {}

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        if(assignedSpawns.containsKey(event.getPlayer()) || event.getTo() == null) return;
        BoundingBox box = assignedSpawns.get(event.getPlayer());
        if(!box.contains(event.getFrom().toVector())) {
            event.getPlayer().teleport(box.getCenter().toLocation(gameManager.getWorld()));
        } else if(!box.contains(event.getTo().toVector())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(assignedSpawns.containsKey(event.getPlayer())) return;
        event.setCancelled(true);
    }

    @EventHandler
    public void omEntityDamage(EntityDamageEvent event) {
        if(event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            if(gameManager.getPlayers().contains(p)) event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        gameManager.getPlayers().remove(event.getPlayer());
    }
}
