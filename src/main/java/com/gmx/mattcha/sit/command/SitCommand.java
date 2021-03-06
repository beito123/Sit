package com.gmx.mattcha.sit.command;

/*
	sit

 	Copyright (c) 2020 beito

	This software is released under the LGPLv3.
	https://www.gnu.org/licenses/lgpl-3.0.html
*/

import com.gmx.mattcha.sit.SitAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SitCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            SitAPI.getAPI().getMessage().send(sender, "command.sit.ingame");
            return true;
        }

        Player player = (Player) sender;

        if (SitAPI.getAPI().IsSat(player)) {
            SitAPI.getAPI().standup(player);
            SitAPI.getAPI().getMessage().sendActionBar(player, "command.sit.standup");

            return true;
        }

        if (player.isFlying()) {
            SitAPI.getAPI().getMessage().send(player, "command.sit.flying");
            return true;
        } else if (player.isSwimming()) {
            SitAPI.getAPI().getMessage().send(player, "command.sit.swimming");
            return true;
        } else if (player.isSleeping()) {
            SitAPI.getAPI().getMessage().send(player, "command.sit.sleeping");
            return true;
        }

        SitAPI.getAPI().sit(player, player.getLocation().add(0, -0.8, 0));

        // Maybe it's not shown.
        SitAPI.getAPI().getMessage().sendActionBar(player, "command.sit.ok");
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(net.md_5.bungee.api.ChatColor.YELLOW + "<Sit> 座りました! 左Shiftで立ち上がれます。"));

        return true;
    }
}
