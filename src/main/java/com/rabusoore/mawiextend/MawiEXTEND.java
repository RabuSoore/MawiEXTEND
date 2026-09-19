package com.rabusoore.mawiextend;

import com.rabusoore.mawiextend.commands.AutoInventoryCommand;
import com.rabusoore.mawiextend.commands.AutoToolsCommand;
import com.rabusoore.mawiextend.commands.MawiExtendCommand;
import com.rabusoore.mawiextend.listeners.*;
import com.rabusoore.mawiextend.managers.SoundManager;
import com.rabusoore.mawiextend.managers.ToggleManager;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class MawiEXTEND extends JavaPlugin {

    private SoundManager soundManager;
    private ToggleManager toggleManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.soundManager = new SoundManager(this);
        this.toggleManager = new ToggleManager(this);

        registerCommands();
        registerListeners();

        getLogger().info("MawiEXTEND v" + getPluginMeta().getVersion() + " by Rabusoore successfully enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("MawiEXTEND disabled.");
    }

    private void registerCommands() {
        AutoToolsCommand autoToolsCmd = new AutoToolsCommand(this);
        if (getCommand("autotools") != null) {
            getCommand("autotools").setExecutor(autoToolsCmd);
            getCommand("autotools").setTabCompleter(autoToolsCmd);
        }

        AutoInventoryCommand autoInvCmd = new AutoInventoryCommand(this);
        if (getCommand("autoinventory") != null) {
            getCommand("autoinventory").setExecutor(autoInvCmd);
            getCommand("autoinventory").setTabCompleter(autoInvCmd);
        }

        MawiExtendCommand mawiCmd = new MawiExtendCommand(this);
        if (getCommand("mawiextend") != null) {
            getCommand("mawiextend").setExecutor(mawiCmd);
            getCommand("mawiextend").setTabCompleter(mawiCmd);
        }
    }

    private void registerListeners() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new HotbarListener(this), this);
        pm.registerEvents(new ItemDropListener(this), this);
        pm.registerEvents(new ItemPickupListener(this), this);
        pm.registerEvents(new AutoToolsListener(this), this);
        pm.registerEvents(new AutoInventoryListener(this), this);
        pm.registerEvents(new ChatListener(this), this);
    }

    public void reloadPluginConfig() {
        reloadConfig();
    }

    public SoundManager getSoundManager() {
        return soundManager;
    }

    public ToggleManager getToggleManager() {
        return toggleManager;
    }
}
