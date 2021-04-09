package me.modione.sgplugin.commands;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import me.modione.sgplugin.SGPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

public class SgCommand implements TabExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(SGPlugin.prefix + ChatColor.RED + "Invalid usage!");
            return true;
        }
        if(args[0].equals("start")) {
            Bukkit.getOnlinePlayers().forEach(p -> SGPlugin.INSTANCE.gameManager.joinGame(p, false));
            if(!SGPlugin.INSTANCE.gameManager.start()) sender.sendMessage(SGPlugin.prefix + ChatColor.RED + "The game is already running!");
            else sender.sendMessage(SGPlugin.prefix + ChatColor.GREEN + "The game will start soon!");
        } else if(args[0].equals("stop")) {
            if(SGPlugin.INSTANCE.gameManager.getGameState() <= -1) {
                sender.sendMessage(SGPlugin.prefix + ChatColor.RED + "No game is currently running!");
            } else {
                Bukkit.broadcastMessage(SGPlugin.prefix + ChatColor.YELLOW + sender.getName() + ChatColor.RED + " cancelled the Game!");
                SGPlugin.INSTANCE.gameManager.cancelGame();
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias,
        String[] args) {
        List<String> completions = null;
        if(args.length == 1) completions = Arrays.asList("start", "stop");
        if(completions == null) return Collections.emptyList();
        return completions.stream().filter(hit -> hit.startsWith(args[args.length - 1])).collect(
            Collectors.toList());
    }
}
