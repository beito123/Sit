package com.gmx.mattcha.sit.task;

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
            LivingEntity entity = entry.getValue();
            List<Entity> passengers = entity.getPassengers();
            if (passengers.size() == 0) {
                return;
            }

            Entity pass = passengers.get(0);
            Location location = pass.getLocation();
            entity.setRotation(location.getYaw(), location.getPitch());
        }
    }
}
