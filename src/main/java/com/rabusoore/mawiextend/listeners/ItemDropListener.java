package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

public class ItemDropListener implements Listener {

    private final MawiEXTEND plugin;

    public ItemDropListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent event) {
        plugin.getSoundManager().playSound(event.getPlayer(), "item-drop");
    }
}
