package tk.t11e.murder.listener;
// Created by booky10 in Murder (13:47 18.01.20)

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import tk.t11e.murder.manager.MurderManager;

public class FoodChangeListener implements Listener {

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if(!event.getEntity().getType().equals(EntityType.PLAYER))return;
        if(!event.getEntity().getWorld().getName().equalsIgnoreCase(MurderManager.getWorld().getName()))
            return;
        event.setFoodLevel(20);
    }
}