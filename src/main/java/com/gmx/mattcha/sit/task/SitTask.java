package com.gmx.mattcha.sit.task;

/*
	sit

 	Copyright (c) 2020 beito

	This software is released under the LGPLv3.
	https://www.gnu.org/licenses/lgpl-3.0.html
*/

import com.gmx.mattcha.sit.SitAPI;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.UUID;

public class SitTask extends TimerTask {
    @Override
    public void run() {
        Map<UUID, LivingEntity> chairs = SitAPI.getAPI().getAllChairs();

        for (Map.Entry<UUID, LivingEntity> entry : chairs.entrySet()){
            LivingEntity chair = entry.getValue();
            List<Entity> passengers = chair.getPassengers();
            if (passengers.size() == 0) {
                return;
            }

            Entity pass = passengers.get(0);
            Location passPos = pass.getLocation();
            float yaw = passPos.getYaw();

            float diffYaw = yaw - chair.getLocation().getYaw();
            if (Math.abs(diffYaw) < 60) {
                return;
            }

            chair.setRotation(passPos.getYaw(), passPos.getPitch());
        }
    }
}
