package com.gmx.mattcha.sit.util;

import com.gmx.mattcha.util.BaseCustomMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

public class CustomMessage {

    public String defaultLang;

    protected String unknownKeyMsg = "";
    protected Map<String, BaseCustomMessage> langMap = new HashMap<>();

    public CustomMessage(JavaPlugin plugin, List<String> langList, String defLang) {
        this.defaultLang = defLang;

        for (String langName : langList) {
            load(plugin, langName, "lang/" + langName + ".yml");
        }

        if (!this.langMap.containsKey(defaultLang)) {
            Bukkit.getServer().getLogger().warning("Unable to recognize the default language. " +
                    "Default:" + defaultLang + ", Loaded lang:" + langMap.keySet().toString());
        }
    }

    public void load(JavaPlugin plugin, String langName, String path) {
        YamlConfiguration loader = new YamlConfiguration();
        try {
            InputStream is = plugin.getResource(path);
            if (is == null) {
                Bukkit.getServer().getLogger().warning("Failed to load lang file. File:" + path);

                return;
            }

            loader.load(new InputStreamReader(is, StandardCharsets.UTF_8));
        } catch (IOException | InvalidConfigurationException e) {
            Bukkit.getServer().getLogger().log(Level.SEVERE, "Couldn't load lang file.", e);
        }

        Map<String, String> msgs = toSSMap(loader.getValues(true));

        this.langMap.put(langName, new BaseCustomMessage(msgs));
    }

    protected Map<String, String> toSSMap(Map<String, Object> oldMap) {
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, Object> e : oldMap.entrySet()) {
            newMap.put(e.getKey(), e.getValue().toString());
        }

        return newMap;
    }

    public BaseCustomMessage getLang(String langName) {
        return this.langMap.get(langName);
    }

    public boolean hasMessage(String key) {
        return this.hasMessage(this.defaultLang, key);
    }

    public boolean hasMessage(String langName, String key) {
        return this.getLang(langName).hasMessage(key);
    }

    public String getMessage(String key, String ...args) {
        return this.getMessage(this.defaultLang, key, args);
    }

    public String getMessage(String langName, String key, String ...args) {
        BaseCustomMessage lang = this.getLang(langName);
        if (lang == null) {
            if (langName.equals(this.defaultLang)) {
                Bukkit.getServer().getLogger().warning("Unable to recognize the default language. " +
                        "Default:" + defaultLang + ", Loaded lang:" + langMap.keySet().toString());
                return "Unable to recognize the default language.";
            }

            return this.getMessage(this.defaultLang, key, args);
        }

        return ChatColor.RESET + lang.getMessage(key ,args) + ChatColor.RESET;
    }

    public void send(CommandSender target, String key, String ...args) {
        String langName = this.defaultLang;
        if (target instanceof Player) {
            Player player = (Player) target;
            langName = player.getLocale();
        }

        this.send(langName, target, key, args);
    }

    public void send(String langName, CommandSender target, String key, String ...args) {
        target.sendMessage(this.getMessage(langName, key, args));
    }

    public void sendActionBar(Player target, String key, String ...args) {
        String langName = target.getLocale();

        sendActionBar(langName, target, key, args);
    }

    public void sendActionBar(String langName, Player target, String key, String ...args) {
        String msg = this.getMessage(langName, key, args);
        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }
}
