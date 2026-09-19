package com.rabusoore.mawiextend.listeners;

import com.rabusoore.mawiextend.MawiEXTEND;
import com.rabusoore.mawiextend.utils.ColorUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.title.Title;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import java.time.Duration;

public class BlockBreakListener implements Listener {

    private final MawiEXTEND plugin;

    public BlockBreakListener(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        Player player = event.getPlayer();

        if (!plugin.getConfig().getBoolean("title-block-break.enabled", true)) {
            return;
        }

        Block block = event.getBlock();
        String blockName = formatMaterialName(block.getType().name());

        String titleRaw = plugin.getConfig().getString("title-block-break.title", "")
                .replace("%block_name%", blockName);

        String subtitleRaw = plugin.getConfig().getString("title-block-break.subtitle", "")
                .replace("%block_name%", blockName);

        Component titleComp = ColorUtils.parse(titleRaw);
        Component subtitleComp = ColorUtils.parse(subtitleRaw);

        int fadeIn = plugin.getConfig().getInt("title-block-break.fade-in-ms", 100);
        int stay = plugin.getConfig().getInt("title-block-break.stay-ms", 800);
        int fadeOut = plugin.getConfig().getInt("title-block-break.fade-out-ms", 200);

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
