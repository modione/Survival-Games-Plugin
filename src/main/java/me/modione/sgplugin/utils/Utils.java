package me.modione.sgplugin.utils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import me.modione.sgplugin.SGPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

public class Utils {

    public static boolean checkPermissions(String permission, CommandSender sender, Boolean playerOnly) {
        if (playerOnly) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(ChatColor.RED + "Command only usable as Player!");
                return false;
            }
        }
        if (!sender.hasPermission(permission)) {
            sender.sendMessage(ChatColor.RED + "You don't have permission to use this command!");
            return false;
        } else {
            return true;
        }
    }

    public static boolean checkPermissions(String permission, CommandSender sender) {
        return checkPermissions(permission, sender, true);
    }

    public static BukkitTask createCountdown(int time, BiConsumer<String, Integer> callback, Runnable then) {
        AtomicReference<BukkitTask> task = new AtomicReference<>();
        AtomicInteger countdown = new AtomicInteger(time);
        task.set(Bukkit.getScheduler().runTaskTimer(SGPlugin.INSTANCE, () -> {
            int seconds = countdown.decrementAndGet();
            if(seconds == 0) {
                BukkitTask t = task.get();
                t.cancel();
                then.run();
            }
            String timer = String.format("%02d:%02d", seconds / 3600, (seconds % 3600) / 20);
            callback.accept(timer, seconds);
        }, 20, 20));
        return task.get();
    }
}
