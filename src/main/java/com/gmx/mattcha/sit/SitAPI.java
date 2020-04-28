package com.gmx.mattcha.sit;

/*
	sit

 	Copyright (c) 2020 beito

	This software is released under the LGPLv3.
	https://www.gnu.org/licenses/lgpl-3.0.html
*/

import com.gmx.mattcha.sit.util.CustomMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.*;

public class SitAPI {

    public static SitAPI INSTANCE;

    protected SitAPI(JavaPlugin plugin) {
        init(plugin);
        INSTANCE = this;
    }

    public static SitAPI getAPI() {
        return INSTANCE;
    }


    protected CustomMessage msg;
    private Map<UUID, LivingEntity> chairs = new HashMap<>();

    protected void init(JavaPlugin plugin) {
        List<String> langList = Arrays.asList("en_us", "ja_jp");
        this.msg = new CustomMessage(plugin, langList, "en_us");
    }

    public CustomMessage getMessage() {
        return this.msg;
    }

    public boolean IsSat(Player player) {
        return this.chairs.containsKey(player.getUniqueId());
    }

    public Map<UUID, LivingEntity> getAllChairs() {
        return this.chairs;
    }

    public LivingEntity getChair(Player player) {
        return this.chairs.get(player.getUniqueId());
    }

    public void sit(Player player, Location pos) {
        this.standup(player);

        World world = player.getWorld();

        Bat entity = (Bat) world.spawnEntity(pos, EntityType.BAT);

        entity.setAwake(true);
        entity.setAI(false);
        entity.setInvulnerable(true);
        entity.setCollidable(false);
        entity.setSilent(true);
        entity.setGravity(false);
        entity.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,
                9999999, 1, false, false ,false));
        //entity.teleport(pos);
        entity.addPassenger(player);
        entity.setRotation(player.getLocation().getYaw(), player.getLocation().getPitch());

        this.chairs.put(player.getUniqueId(), entity);
    }

    public void standup(Player player) {
        if (!this.IsSat(player)) {
            return;
        }

        LivingEntity chair = this.getChair(player);

        this.chairs.remove(player.getUniqueId());

        chair.setInvulnerable(false);
        chair.setHealth(0);
    }

    public void closeAllChairs() {
        for (Map.Entry<UUID, LivingEntity> entry : this.chairs.entrySet()) {
            LivingEntity chair = entry.getValue();
            if (chair.isDead()) {
                return;
            }

            chair.setInvulnerable(false);
            chair.setHealth(0);
        }
    }
}
