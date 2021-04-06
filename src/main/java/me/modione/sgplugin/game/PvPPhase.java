package me.modione.sgplugin.game;

import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.base.GamePhase;
import me.modione.sgplugin.game.GameManager.GameState;
import me.modione.sgplugin.utils.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.scheduler.BukkitTask;

public class PvPPhase extends GamePhase {
    private BukkitTask task;

    public PvPPhase(GameManager gameManager) {
        super(gameManager);
    }

    @Override
    public GameState getState() {
        return GameState.PVP;
    }

    @Override
    public void onStart() {
        gameManager.getPlayers().forEach(player -> player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_IRON_XYLOPHONE, 1, 1));
        task = Utils.createCountdown(10, (format, seconds) -> {
            gameManager.getPlayers().forEach(player -> {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(
                    ChatColor.BLUE + "PvP in " + ChatColor.YELLOW + seconds + ChatColor.BLUE + " seconds!"));
            });
        }, () -> {
            gameManager.getWorld().setPVP(true);
            Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.GREEN + "PvP is now enabled!");
            gameManager.getPlayers().forEach(player -> {
                player.playSound(player.getLocation(), Sound.ENTITY_WITHER_SPAWN, 1, 1);
                player.sendTitle(ChatColor.AQUA + "Fight!", ChatColor.BLUE + "PvP is now enabled!", 10, 60, 15);
            });
            task = Bukkit.getScheduler().runTaskLater(SGPlugin.INSTANCE, this::next, 12000);
        });
    }

    @Override
    public void onEnd() {}

    @Override
    public void onCancel() {
        if(task != null) task.cancel();
    }
}
