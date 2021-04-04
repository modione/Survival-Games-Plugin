package me.modione.sgplugin.commands;

import java.util.List;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.listener.onGameStart;
import me.modione.sgplugin.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class StartGameCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!utils.perms("SG.start", sender, false)) return false;
        if (!SGPlugin.INSTANCE.startable) {
            sender.sendMessage(ChatColor.RED + "An error occurred! Cannot start the game.");
            return false;
        }
        if (onGameStart.gameprepared) {
            if (onGameStart.gamestarted) {
                sender.sendMessage(ChatColor.RED + "A game is already running!");
                return false;
            }
            onGameStart.StartGame();
            sender.sendMessage(ChatColor.GREEN + "The Game has been started");
            return false;
        }
        onGameStart.PrepareGame();
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
