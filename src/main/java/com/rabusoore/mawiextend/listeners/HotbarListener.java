package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;

public class HotbarListener implements Listener {

    private final MawiEXTEND plugin;

    public HotbarListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onItemHeldChange(PlayerItemHeldEvent event) {
        if (event.getPreviousSlot() != event.getNewSlot()) {
            plugin.getSoundManager().playSound(event.getPlayer(), "hotbar");
        }
    }
}
