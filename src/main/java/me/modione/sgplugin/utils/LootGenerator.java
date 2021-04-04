package me.modione.sgplugin.utils;

import java.util.List;
import java.util.Random;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class LootGenerator {
    private final List<ItemStack> pool;
    private final Random random;
    private final double itemChance;

    public LootGenerator(List<ItemStack> pool) {
        this(pool, new Random(), 0.2);
    }

    public LootGenerator(List<ItemStack> pool, Random random, double itemChance) {
        this.pool = pool;
        this.random = random;
        this.itemChance = itemChance;
    }

    public void generateLoot(Inventory target) {
        for(int i = 0; i < target.getSize(); i++) {
            if(random.nextDouble() <= itemChance) {
                 ItemStack item = pool.get(random.nextInt(pool.size())).clone();
                 item.setAmount(random.nextInt(item.getMaxStackSize() - 1) + 1);
                 target.setItem(i, item);
            }
        }
    }
}
