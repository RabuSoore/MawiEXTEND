package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import com.rabusoore.mawiextend.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import net.kyori.adventure.title.Title;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;

import java.time.Duration;

public class ItemPickupListener implements Listener {

    private final MawiEXTEND plugin;

    public ItemPickupListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onItemPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        plugin.getSoundManager().playSound(player, "item-pickup");

        if (!plugin.getConfig().getBoolean("title-pickup.enabled", true)) {
            return;
        }

        InventoryType topType = player.getOpenInventory().getTopInventory().getType();
        if (topType != InventoryType.CRAFTING && topType != InventoryType.CREATIVE) {
            return;
        }

        Item itemEntity = event.getItem();
        ItemStack itemStack = itemEntity.getItemStack();

        String itemName;
        if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            Component displayNameComp = itemStack.getItemMeta().displayName();
            itemName = PlainTextComponentSerializer.plainText().serialize(displayNameComp);
        } else {
            itemName = formatMaterialName(itemStack.getType().name());
        }

        int amount = itemStack.getAmount();

        String titleRaw = plugin.getConfig().getString("title-pickup.title", "")
                .replace("%item_name%", itemName)
                .replace("%amount%", String.valueOf(amount));

        String subtitleRaw = plugin.getConfig().getString("title-pickup.subtitle", "")
                .replace("%item_name%", itemName)
                .replace("%amount%", String.valueOf(amount));

        Component titleComp = ColorUtils.parse(titleRaw);
        Component subtitleComp = ColorUtils.parse(subtitleRaw);

        int fadeIn = plugin.getConfig().getInt("title-pickup.fade-in-ms", 100);
        int stay = plugin.getConfig().getInt("title-pickup.stay-ms", 800);
        int fadeOut = plugin.getConfig().getInt("title-pickup.fade-out-ms", 200);

        Title title = Title.title(
                titleComp,
                subtitleComp,
                Title.Times.times(Duration.ofMillis(fadeIn), Duration.ofMillis(stay), Duration.ofMillis(fadeOut))
        );

        player.showTitle(title);
    }

    private String formatMaterialName(String materialName) {
        String[] words = materialName.toLowerCase().split("_");
        StringBuilder sb = new StringBuilder();
        for (String word : words) {
            if (!word.isEmpty()) {
                sb.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
            }
        }
        return sb.toString().trim();
    }
}
