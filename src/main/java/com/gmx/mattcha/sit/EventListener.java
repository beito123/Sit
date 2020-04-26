package com.gmx.mattcha.sit;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Slab;
import org.bukkit.block.data.type.Stairs;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.event.vehicle.VehicleExitEvent;


public class EventListener implements Listener {

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (!SitAPI.getAPI().IsSat(player)) {
            return;
        }

        SitAPI.getAPI().standup(player);
    }

    @EventHandler
    public void onBedEnter(PlayerBedEnterEvent event) {
        if (!SitAPI.getAPI().IsSat(event.getPlayer())) {
            return;
        }

        Player player = event.getPlayer();

        SitAPI.getAPI().standup(player);
    }

    @EventHandler
    public void on2(PlayerMoveEvent event) {
        if (!SitAPI.getAPI().IsSat(event.getPlayer())) {
            return;
        }

        Player player = event.getPlayer();

        SitAPI.getAPI().standup(player);
    }

    @EventHandler
    public void onStandup(VehicleEnterEvent event) {
        Entity entity = event.getEntered();
        if (!(entity instanceof Player)) {
            return;
        }

        SitAPI.getAPI().standup((Player) entity);
    }

    @EventHandler
    public void onStandup(VehicleExitEvent event) {
        Entity entity = event.getExited();
        if (!(entity instanceof Player)) {
            return;
        }

        SitAPI.getAPI().standup((Player) entity);
    }

    @EventHandler
    public void onTap(PlayerInteractEvent event) {
        Player player = event.getPlayer();

        if (!SitAPI.getAPI().IsSat(player)) {
            return;
        }

        Block block = event.getClickedBlock();
        BlockData blockData = block.getBlockData();

        if (blockData instanceof Stairs) { // Bad codes
            if (((Stairs) blockData).getHalf() != Bisected.Half.BOTTOM) {
                return;
            }

            double distance = block.getLocation().add(0.5, 0, 0.5).distance(player.getLocation());
            if (distance > 3.5) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.YELLOW + "<Sit> ブロックが遠すぎます。"));
                return;
            }

            SitAPI.getAPI().sit(player, block.getLocation().add(0.5, -0.34, 0.5));
        } else if (blockData instanceof Slab) {
            if (((Slab) blockData).getType() != Slab.Type.BOTTOM) {
                return;
            }

            double distance = block.getLocation().add(0.5, 0, 0.5).distance(player.getLocation());
            if (distance > 3.5) {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                        TextComponent.fromLegacyText(ChatColor.YELLOW + "<Sit> ブロックが遠すぎます。"));
                return;
            }

            SitAPI.getAPI().sit(player, block.getLocation().add(0.5, -0.34, 0.5));
        }
    }
}
