package me.modione.sgplugin.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class utils {

    public static boolean perms(String name, CommandSender sender, Boolean kickconsole) {
        if (kickconsole) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Command only usable as Player!");
                return false;
            }
        }
        if (!sender.hasPermission(name)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return false;
        } else {
            return true;
        }
    }

    public static boolean perms(String name, CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Command only usable as Player!");
            return false;
        }
        if (!sender.hasPermission(name)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return false;
        } else {
            return true;
        }

    }
}
