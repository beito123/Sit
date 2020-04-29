package com.gmx.mattcha.sit;

/*
	sit

 	Copyright (c) 2020 beito

	This software is released under the LGPLv3.
	https://www.gnu.org/licenses/lgpl-3.0.html
*/

import com.gmx.mattcha.sit.command.SitCommand;
import com.gmx.mattcha.sit.task.SitTask;
import com.gmx.mattcha.sit.util.CustomMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MainClass extends JavaPlugin {

    public final static int LANG_VERSION = 1;

    private final Map<String, Object> defaultConfig = new LinkedHashMap<String, Object>(){
        {
            put("default-lang", "en_us");
            put("command-lang", "ja_jp");
            put("lang-version", LANG_VERSION);
        }
    };

    @Override
    public void onEnable() {
        // Config
        FileConfiguration config = initConfig();

        String defLang = config.getString("default-lang");
        String cmdLang = config.getString("command-lang");
        int langVersion = config.getInt("lang-version");

        // Localize
        List<String> langList = Arrays.asList("en_us", "ja_jp");

        boolean isOldLang = (langVersion < LANG_VERSION);

        SitAPI.getAPI().setMessage(new CustomMessage(this, langList, "en_us", isOldLang));
        SitAPI.getAPI().getMessage().setDefaultLang(defLang);

        if (isOldLang) {
            config.set("lang-version", LANG_VERSION);
            this.saveConfig();

            Bukkit.getServer().getConsoleSender().sendMessage(
                    SitAPI.getAPI().getMessage().translateInto(cmdLang, "system.localization.updated",
                            new File(this.getDataFolder(), "/lang/").toString()));
        }

        // Command

        PluginCommand cmd =  this.getCommand("sit");
        cmd.setExecutor(new SitCommand());
        SitAPI.getAPI().getMessage().translateCommand(cmdLang, cmd);

        // Init

        this.getServer().getScheduler().scheduleSyncRepeatingTask(this, new SitTask(), 0, 20 * 1);
        this.getServer().getPluginManager().registerEvents(new EventListener(), this);
    }

    @Override
    public void onDisable() {
        SitAPI.getAPI().closeAllChairs();
    }

    public FileConfiguration initConfig() {
        boolean needSave = false;
        File configFile = new File(this.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            needSave = true;
        }

        FileConfiguration config = this.getConfig();
        for (Map.Entry<String, Object> e : defaultConfig.entrySet()) {
            if (!config.contains(e.getKey())) {
                config.set(e.getKey(), e.getValue());
                needSave = true;
            }
        }

        if (needSave) {
            this.saveConfig();
        }

        return config;
    }
}
