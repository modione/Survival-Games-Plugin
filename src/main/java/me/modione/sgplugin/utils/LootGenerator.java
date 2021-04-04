package me.modione.sgplugin.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LootGenerator {
    public static List<ItemStack> standartLoot = new ArrayList<>();
    public static Random random = new Random();

    public static void generateChestLoot(Chest chest) {
        Inventory inv = chest.getInventory();
        List<ItemStack> standardLoot = new ArrayList<>(standartLoot);
        for (int i = 0; i < inv.getSize(); i++) {
            int r = random.nextInt(standardLoot.size()+50);
            switch (r) {
                case 1:
                    if (standardLoot.isEmpty()) continue;
                    ItemStack item = standardLoot.get(random.nextInt(standardLoot.size()));
                    standardLoot.remove(item);
                    inv.setItem(i, item);
                default:
                    continue;
            }
        }
    }
}
