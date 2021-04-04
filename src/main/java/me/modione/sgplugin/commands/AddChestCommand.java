package me.modione.sgplugin.commands;

import java.util.List;
import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.Utils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

public class AddChestCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!Utils.checkPermissions("SG.addchest", sender)) return false;
        Player player = (Player) sender;
        Block targetblock = player.getTargetBlock(null, 100);
        if ((targetblock.getWorld().getEnvironment() != World.Environment.NORMAL)) {
            player.sendMessage(SGPlugin.prefix + ChatColor.RED + "You can only add chests that are in the overworld.");
            return false;
        }
        if (targetblock.getType() != Material.CHEST) {
            player.sendMessage(SGPlugin.prefix + ChatColor.RED + "You are not looking at a chest");
            return false;
        } else {
            SGPlugin.INSTANCE.chests.add(targetblock.getLocation());
            player.sendMessage(SGPlugin.prefix + ChatColor.GREEN + "Added the chest to the chests in the game!");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
