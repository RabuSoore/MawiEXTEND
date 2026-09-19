package com.rabusoore.mawiextend.commands;

import com.rabusoore.mawiextend.MawiEXTEND;
import com.rabusoore.mawiextend.utils.ColorUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class AutoInventoryCommand implements CommandExecutor, TabCompleter {

    private final MawiEXTEND plugin;

    public AutoInventoryCommand(MawiEXTEND plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("Only players can execute this command.");
            return true;
        }

        if (!player.hasPermission("mawiextend.command.autoinventory")) {
            sendConfigMessage(player, "messages.no-permission");
            return true;
        }

        boolean newState = !plugin.getToggleManager().isAutoInventoryEnabled(player);

        if (args.length > 0) {
            if (args[0].equalsIgnoreCase("on")) {
                newState = true;
            } else if (args[0].equalsIgnoreCase("off")) {
                newState = false;
            } else if (!args[0].equalsIgnoreCase("toggle")) {
                player.sendMessage(ColorUtils.parse("<red>Usage: /autoinventory <on|off|toggle></red>"));
                return true;
            }
        }

        plugin.getToggleManager().setAutoInventoryEnabled(player, newState);

        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String statusText = newState
                ? plugin.getConfig().getString("messages.status-enabled", "<green>ENABLED</green>")
                : plugin.getConfig().getString("messages.status-disabled", "<red>DISABLED</red>");

        String message = plugin.getConfig().getString("messages.auto-inventory-toggle", "%prefix%Auto Inventory: %status%")
                .replace("%prefix%", prefix)
                .replace("%status%", statusText);

        player.sendMessage(ColorUtils.parse(message));
        return true;
    }

    private void sendConfigMessage(Player player, String path) {
        String prefix = plugin.getConfig().getString("messages.prefix", "");
        String msg = plugin.getConfig().getString(path, "").replace("%prefix%", prefix);
        player.sendMessage(ColorUtils.parse(msg));
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 1) {
            List<String> options = List.of("on", "off", "toggle");
            List<String> result = new ArrayList<>();
            for (String opt : options) {
                if (opt.startsWith(args[0].toLowerCase())) {
                    result.add(opt);
                }
            }
            return result;
        }
        return List.of();
    }
}
