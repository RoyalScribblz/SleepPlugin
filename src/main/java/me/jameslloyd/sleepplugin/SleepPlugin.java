package me.jameslloyd.sleepplugin;

import me.jameslloyd.sleepplugin.events.PlayerBedListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class SleepPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new PlayerBedListener(), this);
    }
}
