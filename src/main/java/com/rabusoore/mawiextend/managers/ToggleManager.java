package com.rabusoore.mawiextend.managers;

import com.rabusoore.mawiextend.MawiEXTEND;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class ToggleManager {

    private final MawiEXTEND plugin;
    private final NamespacedKey autoToolsKey;
    private final NamespacedKey autoInventoryKey;

    public ToggleManager(MawiEXTEND plugin) {
        this.plugin = plugin;
        this.autoToolsKey = new NamespacedKey(plugin, "auto_tools");
        this.autoInventoryKey = new NamespacedKey(plugin, "auto_inventory");
    }

    public boolean isAutoToolsEnabled(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (!pdc.has(autoToolsKey, PersistentDataType.BYTE)) {
            return plugin.getConfig().getBoolean("defaults.auto-tools", true);
        }
        Byte val = pdc.get(autoToolsKey, PersistentDataType.BYTE);
        return val != null && val == (byte) 1;
    }

    public void setAutoToolsEnabled(Player player, boolean enabled) {
        player.getPersistentDataContainer().set(autoToolsKey, PersistentDataType.BYTE, (byte) (enabled ? 1 : 0));
    }

    public boolean isAutoInventoryEnabled(Player player) {
        PersistentDataContainer pdc = player.getPersistentDataContainer();
        if (!pdc.has(autoInventoryKey, PersistentDataType.BYTE)) {
            return plugin.getConfig().getBoolean("defaults.auto-inventory", true);
        }
        Byte val = pdc.get(autoInventoryKey, PersistentDataType.BYTE);
        return val != null && val == (byte) 1;
    }

    public void setAutoInventoryEnabled(Player player, boolean enabled) {
        player.getPersistentDataContainer().set(autoInventoryKey, PersistentDataType.BYTE, (byte) (enabled ? 1 : 0));
    }
}
