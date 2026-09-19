package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDropItemEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class AutoInventoryListener implements Listener {

    private final MawiEXTEND plugin;

    public AutoInventoryListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDropItem(BlockDropItemEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getToggleManager().isAutoInventoryEnabled(player)) {
            return;
        }

        List<Item> items = event.getItems();
        for (Item itemEntity : items) {
            ItemStack stack = itemEntity.getItemStack();
            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(stack);

            if (leftover.isEmpty()) {
                itemEntity.remove();
            } else {
                itemEntity.setItemStack(leftover.get(0));
            }
        }
    }
}
