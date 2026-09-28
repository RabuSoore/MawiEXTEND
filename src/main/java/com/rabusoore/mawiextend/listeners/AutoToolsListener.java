package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class AutoToolsListener implements Listener {

    private final MawiEXTEND plugin;

    public AutoToolsListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    // Trigger 1: Deteksi saat pertama kali klik kiri blok (Paling cepat & ramah Anti-Cheat)
    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.LEFT_CLICK_BLOCK) {
            return;
        }

        Block block = event.getClickedBlock();
        if (block == null) {
            return;
        }

        Player player = event.getPlayer();
        if (!plugin.getToggleManager().isAutoToolsEnabled(player)) {
            return;
        }

        switchBestTool(player, block.getType());
    }

    // Trigger 2: Fallback saat pemain menahan klik pada blok
    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onBlockDamage(BlockDamageEvent event) {
        Player player = event.getPlayer();
        if (!plugin.getToggleManager().isAutoToolsEnabled(player)) {
            return;
        }

        switchBestTool(player, event.getBlock().getType());
    }

    private void switchBestTool(Player player, Material blockMaterial) {
        PlayerInventory inv = player.getInventory();

        int bestSlot = findBestToolSlot(inv, blockMaterial);
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

            double speed = getToolDestroySpeed(item, blockMaterial);
            if (speed > maxSpeed) {
                maxSpeed = speed;
                bestSlot = slot;
            }
        }

        return bestSlot;
    }

    private double getToolDestroySpeed(ItemStack item, Material blockMaterial) {
        Material tool = item.getType();
        String toolName = tool.name();
        double baseSpeed = 1.0;
        boolean isCorrectTool = false;

        if (Tag.MINEABLE_PICKAXE.isTagged(blockMaterial) && toolName.endsWith("_PICKAXE")) {
            baseSpeed = getToolTierMultiplier(toolName);
            isCorrectTool = true;
        } else if (Tag.MINEABLE_AXE.isTagged(blockMaterial) && toolName.endsWith("_AXE")) {
            baseSpeed = getToolTierMultiplier(toolName);
            isCorrectTool = true;
        } else if (Tag.MINEABLE_SHOVEL.isTagged(blockMaterial) && toolName.endsWith("_SHOVEL")) {
            baseSpeed = getToolTierMultiplier(toolName);
            isCorrectTool = true;
        } else if (Tag.MINEABLE_HOE.isTagged(blockMaterial) && toolName.endsWith("_HOE")) {
            baseSpeed = getToolTierMultiplier(toolName);
            isCorrectTool = true;
        } else if (blockMaterial == Material.COBWEB && (toolName.endsWith("_SWORD") || tool == Material.SHEARS)) {
            return 15.0;
        }

        // Kalkulasi Tambahan Bonus Enchantment Efficiency
        if (isCorrectTool) {
            int effLevel = item.getEnchantmentLevel(Enchantment.EFFICIENCY);
            if (effLevel > 0) {
                baseSpeed += (effLevel * effLevel + 1);
            }
        }

        return baseSpeed;
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
