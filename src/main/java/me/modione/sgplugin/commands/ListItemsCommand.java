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

public class ListItemsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.checkPermissions("SG.items", sender)) return false;
        Player player = (Player) sender;
        StringBuilder builder = new StringBuilder();
        builder.append(SGPlugin.prefix);
        for (ItemStack tem : LootGenerator_old.standartLoot) {
            builder.append(ChatColor.AQUA + tem.getType().name()).append(ChatColor.GREEN +", ");
        }
        player.sendMessage(String.valueOf(builder));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
