package tk.t11e.murder.listener;
// Created by booky10 in Murder (13:49 18.01.20)

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import tk.t11e.murder.manager.MurderManager;

public class DamageListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER)) return;
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(MurderManager.getWorld().getName()))
            return;
        if (!MurderManager.isRunning)
            event.setCancelled(true);
        else
            event.setDamage(0);
    }

    @EventHandler
    public void onDamageByEntity(EntityDamageByEntityEvent event) {
        if (!event.getEntityType().equals(EntityType.PLAYER)) return;
        if (!event.getEntity().getWorld().getName().equalsIgnoreCase(MurderManager.getWorld().getName()))
            return;
        Player player = (Player) event.getEntity();

        if (!MurderManager.isRunning)
            event.setCancelled(true);
        else {
            if (event.getDamager().getType().equals(EntityType.ARROW)) {
                event.setDamage(0);
                MurderManager.kill(player);
                event.getDamager().remove();
                return;
            }
            if (!event.getDamager().getType().equals(EntityType.PLAYER)) return;
            Player enemy = (Player) event.getDamager();

            if (enemy.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) {
                event.setDamage(0);
                MurderManager.kill(player);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        event.getEntity().spigot().respawn();
    }
}