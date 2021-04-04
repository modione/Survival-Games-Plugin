package me.modione.sgplugin.commands;

import java.util.List;
import me.modione.sgplugin.listener.onGameEnd;
import me.modione.sgplugin.listener.onGameStart;
import me.modione.sgplugin.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class GameEndCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!utils.perms("SG.end", sender, false)) return false;
        if (!onGameStart.gamestarted) {
            sender.sendMessage(ChatColor.RED + "There is no game running yet!");
            return false;
        }
        onGameEnd.EndGame();
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
