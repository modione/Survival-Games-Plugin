package me.modione.sgplugin.commands;

import me.modione.sgplugin.utils.LootGenerator;
import me.modione.sgplugin.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class AddItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!utils.perms("SG.items", sender)) return false;
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (item == null) {
            player.sendMessage(ChatColor.RED + "You need to hold a item to use this command.");
            return false;
        }
        if (LootGenerator.standartLoot.contains(item)) {
            player.sendMessage(ChatColor.RED + item.getType().name() + " is now duplicated.");
        } else {
            player.sendMessage(ChatColor.GREEN + "You added " + item.getType().name() + " to the loot-table.");
        }
        LootGenerator.standartLoot.add(item);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
