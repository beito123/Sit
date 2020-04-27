package com.gmx.mattcha.sit.command;

import com.gmx.mattcha.sit.SitAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "Run the command in game");
            return true;
        }

        Player player = (Player) sender;

        if (SitAPI.getAPI().IsSat(player)) {
            SitAPI.getAPI().standup(player);
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.YELLOW + "<Sit> 立ち上がりました！"));

            return true;
        }

        if (player.isFlying()) {
            player.sendMessage(ChatColor.YELLOW + "<Sit> 飛行中は座ることはできません。");
            return true;
        } else if (player.isSwimming()) {
            player.sendMessage(ChatColor.YELLOW + "<Sit> 泳ぎの状態で座ることはできません。");
            return true;
        } else if (player.isSleeping()) {
            player.sendMessage(ChatColor.YELLOW + "<Sit> 寝ている間に座ることはできません。");
            return true;
        }

        SitAPI.getAPI().sit(player, player.getLocation().add(0, -0.8, 0));

        // Maybe it's not shown.
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.YELLOW + "<Sit> 座りました! 左Shiftで立ち上がれます。"));

        return true;
    }
}
