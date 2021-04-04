package me.modione.sgplugin.commands;

import java.util.List;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.LootGenerator_old;
import me.modione.sgplugin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class AddItemCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.checkPermissions("SG.items", sender)) return false;
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItem(player.getInventory().getHeldItemSlot());
        if (item == null) {
            player.sendMessage(SGPlugin.prefix + ChatColor.RED + "You need to hold a item to use this command.");
            return false;
        }
        if (LootGenerator_old.standartLoot.contains(item)) {
            player.sendMessage(SGPlugin.prefix + ChatColor.RED + item.getType().name() + " is now duplicated.");
        } else {
            player.sendMessage(SGPlugin.prefix + ChatColor.GREEN + "You added " + item.getType().name() + " to the loot-table.");
        }
        LootGenerator_old.standartLoot.add(item);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
