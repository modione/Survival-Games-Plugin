package me.modione.sgplugin.commands;

import java.util.List;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class GameEndCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.checkPermissions("SG.end", sender, false)) return false;
        if (!onGameStart.gamestarted) {
            sender.sendMessage(SGPlugin.prefix + ChatColor.RED + "There is no game running yet!");
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
