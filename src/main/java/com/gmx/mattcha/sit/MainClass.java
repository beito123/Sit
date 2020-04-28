package com.gmx.mattcha.sit;

/*
	sit

 	Copyright (c) 2020 beito

	This software is released under the LGPLv3.
	https://www.gnu.org/licenses/lgpl-3.0.html
*/

import com.gmx.mattcha.sit.command.SitCommand;
import com.gmx.mattcha.sit.task.SitTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainClass extends JavaPlugin {

    private final Map<String, Object> defaultConfig = new LinkedHashMap<String, Object>(){
        {
            put("default-lang", "en_us");
            put("command-lang", "ja_jp");
        }
    };

    @Override
    public void onEnable() {
        new SitAPI(this); // init

        // Config
        FileConfiguration config = initConfig();

        String defLang = config.getString("default-lang");
        String cmdLang = config.getString("command-lang");

        SitAPI.getAPI().getMessage().defaultLang = defLang;

        // Command

        PluginCommand cmd =  this.getCommand("sit");
        cmd.setExecutor(new SitCommand());
        cmd.setDescription(SitAPI.getAPI().getMessage().getMessage(cmdLang, "command.sit.description", null));

        // Init

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new SitTask(), 0, 20 * 1);
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        SitAPI.getAPI().closeAllChairs();
    }

    public FileConfiguration initConfig() {
        boolean needSaved = false;
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            needSaved = true;
        }

        FileConfiguration config = this.getConfig();
        for (Map.Entry<String, Object> e : defaultConfig.entrySet()) {
            if (!config.contains(e.getKey())) {
                config.set(e.getKey(), e.getValue());
            }
        }

        if (needSaved) {
            this.saveConfig();
        }

        return config;
    }
}
