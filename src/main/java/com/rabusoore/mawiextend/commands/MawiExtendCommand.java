package com.rabusoore.mawiextend.commands;

import com.rabusoore.mawiextend.MawiEXTEND;
import com.rabusoore.mawiextend.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MawiExtendCommand implements CommandExecutor, TabCompleter {

    private final MawiEXTEND plugin;

    public MawiExtendCommand(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("mawiextend.admin")) {
            String prefix = plugin.getConfig().getString("messages.prefix", "");
            String msg = plugin.getConfig().getString("messages.no-permission", "").replace("%prefix%", prefix);
            sender.sendMessage(ColorUtils.parse(msg));
            return true;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("reload")) {
            plugin.reloadPluginConfig();
            String prefix = plugin.getConfig().getString("messages.prefix", "");
            String msg = plugin.getConfig().getString("messages.reload-success", "%prefix%Reloaded!").replace("%prefix%", prefix);
            sender.sendMessage(ColorUtils.parse(msg));
            return true;
        }

        sender.sendMessage(ColorUtils.parse("<gradient:#00FFAA:#00AAFF>MawiEXTEND v" + plugin.getPluginMeta().getVersion() + " by Rabusoore</gradient>"));
        sender.sendMessage(ColorUtils.parse("<gray>Usage: /mawiextend reload</gray>"));
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            return List.of("reload");
        }
        return List.of();
    }
}
