package com.rabusoore.mawiextend.managers;

import com.rabusoore.mawiextend.MawiEXTEND;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class SoundManager {

    private final MawiEXTEND plugin;

    public SoundManager(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    public void playSound(Player player, String configPath) {
        FileConfiguration config = plugin.getConfig();
        if (!config.getBoolean("sounds." + configPath + ".enabled", true)) {
            return;
        }

        String soundKey = config.getString("sounds." + configPath + ".sound", "");
        if (soundKey.isBlank()) return;

        float volume = (float) config.getDouble("sounds." + configPath + ".volume", 1.0);
        float pitch = (float) config.getDouble("sounds." + configPath + ".pitch", 1.0);

        try {
            Sound sound = Sound.sound(Key.key(soundKey.toLowerCase()), Sound.Source.PLAYER, volume, pitch);
            player.playSound(sound);
        } catch (Exception e) {
            plugin.getLogger().warning("Failed to play sound '" + soundKey + "' for config path: " + configPath);
        }
    }
}
