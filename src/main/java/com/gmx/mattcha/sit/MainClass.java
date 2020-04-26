package com.gmx.mattcha.sit;

import com.gmx.mattcha.sit.command.SitCommand;
import com.gmx.mattcha.sit.task.SitTask;
import org.bukkit.plugin.java.JavaPlugin;

public class MainClass extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getCommand("sit").setExecutor(new SitCommand());

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new SitTask(), 0, 20 * 1);
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        SitAPI.getAPI().closeAllChairs();
    }
}
