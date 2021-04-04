package me.modione.sgplugin.commands;

import me.modione.sgplugin.SGPlugin;
import me.modione.sgplugin.utils.utils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.List;

public class AddLocationCommand implements TabExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!utils.perms("SG.addlocation", sender)) return false;
        Player player = (Player) sender;
        Location l = player.getLocation();
        if ((l.getWorld().getEnvironment() != World.Environment.NORMAL)) {
            player.sendMessage(ChatColor.RED + "You can only add locations that are in the overworld.");
            return false;
        }
        int x = (int) l.getX();
        int y = (int) l.getY();
        int z = (int) l.getZ();
        Location location = new Location(player.getWorld(), x, y, z);
        SGPlugin.INSTANCE.locations.add(location);
        player.sendMessage(ChatColor.GREEN + "Successfully added the location to the spawn locations");
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return null;
    }
}
