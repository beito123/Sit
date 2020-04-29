package com.gmx.mattcha.sit.util;

import com.gmx.mattcha.util.BaseCustomMessage;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomMessage {

    protected String defaultLang;

    protected Map<String, BaseCustomMessage> langMap = new HashMap<>();

    public CustomMessage() {
    }

    public CustomMessage(JavaPlugin plugin, List<String> langList, String defLang, boolean replace) {
        this.defaultLang = defLang;

        for (String langName : langList) {
            File file = new File(plugin.getDataFolder(), "lang/" + langName + ".yml");
            if (!file.exists() || replace) {
                plugin.saveResource("lang/" + langName + ".yml", replace);
            }

            this.load(langName, file);
        }

        if (!this.langMap.containsKey(defaultLang)) {
            Bukkit.getServer().getLogger().warning("Unable to recognize the default language. " +
                    "Default:" + defaultLang + ", Loaded lang:" + langMap.keySet().toString());
        }
    }

    public void load(String langName, File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        Map<String, String> msgs = toSSMap(config.getValues(true));

        this.langMap.put(langName, new BaseCustomMessage(msgs));
    }

    protected static Map<String, String> toSSMap(Map<String, Object> oldMap) {
        Map<String, String> newMap = new HashMap<>();
        for (Map.Entry<String, Object> e : oldMap.entrySet()) {
            newMap.put(e.getKey(), e.getValue().toString());
        }

        return newMap;
    }

    public String getDefaultLang() {
        return defaultLang;
    }

    public void setDefaultLang(String defaultLang) {
        this.defaultLang = defaultLang;
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

    public String translate(String key, String ...args) {
        return this.translateInto(this.defaultLang, key, args);
    }

    public String translateInto(String langName, String key, String ...args) {
        BaseCustomMessage lang = this.getLang(langName);
        if (lang == null) {
            if (langName.equals(this.defaultLang)) {
                Bukkit.getServer().getLogger().warning("Unable to recognize the default language. " +
                        "Default:" + defaultLang + ", Loaded lang:" + langMap.keySet().toString());
                return "Unable to recognize the default language.";
            }

            return this.translateInto(this.defaultLang, key, args);
        }

        return ChatColor.RESET + lang.getMessage(key, args) + ChatColor.RESET;
    }

    public void translateCommand(Command cmd, String ...args) {
        this.translateCommand(this.defaultLang, cmd, args);
    }

    public void translateCommand(String langName, Command cmd, String ...args) {
        String desKey = "command." + cmd.getName() + ".description";
        if (this.hasMessage(desKey)) {
            cmd.setDescription(this.translateInto(langName, desKey, args));
        }

        String usageKey = "command." + cmd.getName() + ".usage";
        if (this.hasMessage(desKey)) {
            cmd.setUsage(this.translateInto(langName, usageKey, args));
        }
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
        target.sendMessage(this.translateInto(langName, key, args));
    }

    public void sendActionBar(Player target, String key, String ...args) {
        String langName = target.getLocale();

        sendActionBar(langName, target, key, args);
    }

    public void sendActionBar(String langName, Player target, String key, String ...args) {
        String msg = this.translateInto(langName, key, args);
        target.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(msg));
    }
}
