package me.modione.sgplugin.commands;

import java.util.List;
import me.modione.sgplugin.utils.LootGenerator;
import me.modione.sgplugin.utils.utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ListItemsCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!utils.perms("SG.items", sender)) return false;
        Player player = (Player) sender;
        StringBuilder builder = new StringBuilder();
        for (ItemStack tem : LootGenerator.standartLoot) {
            builder.append(tem.getType().name()).append(", ");
        }
        player.sendMessage(String.valueOf(builder));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
