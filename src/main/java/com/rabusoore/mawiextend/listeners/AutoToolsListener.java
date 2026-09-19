package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AutoToolsListener implements Listener {

    private final MawiEXTEND plugin;

    public AutoToolsListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getToggleManager().isAutoToolsEnabled(player)) {
            return;
        }

        Block block = event.getBlock();
        PlayerInventory inv = player.getInventory();

        int bestSlot = findBestToolSlot(inv, block.getType());
        if (bestSlot != -1 && bestSlot != inv.getHeldItemSlot()) {
            inv.setHeldItemSlot(bestSlot);
        }
    }

    private int findBestToolSlot(PlayerInventory inv, Material blockMaterial) {
        int bestSlot = -1;
        double maxSpeed = 1.0;

        for (int slot = 0; slot < 9; slot++) {
            ItemStack item = inv.getItem(slot);
            if (item == null || item.getType().isAir()) continue;

            double speed = getToolDestroySpeed(item.getType(), blockMaterial);
            if (speed > maxSpeed) {
                maxSpeed = speed;
                bestSlot = slot;
            }
        }

        return bestSlot;
    }

    private double getToolDestroySpeed(Material tool, Material block) {
        String toolName = tool.name();

        if (Tag.MINEABLE_WITH_PICKAXE.isTagged(block) && toolName.endsWith("_PICKAXE")) {
            return getToolTierMultiplier(toolName);
        }
        if (Tag.MINEABLE_WITH_AXE.isTagged(block) && toolName.endsWith("_AXE")) {
            return getToolTierMultiplier(toolName);
        }
        if (Tag.MINEABLE_WITH_SHOVEL.isTagged(block) && toolName.endsWith("_SHOVEL")) {
            return getToolTierMultiplier(toolName);
        }
        if (Tag.MINEABLE_WITH_HOE.isTagged(block) && toolName.endsWith("_HOE")) {
            return getToolTierMultiplier(toolName);
        }
        if (block == Material.COBWEB && (toolName.endsWith("_SWORD") || tool == Material.SHEARS)) {
            return 15.0;
        }

        return 1.0;
    }

    private double getToolTierMultiplier(String toolName) {
        if (toolName.startsWith("NETHERITE_")) return 9.0;
        if (toolName.startsWith("DIAMOND_")) return 8.0;
        if (toolName.startsWith("IRON_")) return 6.0;
        if (toolName.startsWith("STONE_")) return 4.0;
        if (toolName.startsWith("GOLDEN_")) return 12.0;
        if (toolName.startsWith("WOODEN_")) return 2.0;
        return 1.5;
    }
}
