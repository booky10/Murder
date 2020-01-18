package tk.t11e.murder.listener;
// Created by booky10 in Murder (14:44 18.01.20)

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import tk.t11e.murder.main.Main;
import tk.t11e.murder.manager.MurderManager;
import tk.t11e.murder.util.ItemBuilder;

import java.util.HashMap;
import java.util.UUID;

public class InteractListener implements Listener {

    private BukkitTask task;
    private final Main main = Main.getPlugin(Main.class);
    public static final HashMap<UUID, Boolean> cooldown = new HashMap<>();
    public static final Integer SWORD_COOLDOWN = 2;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        if (!event.getPlayer().getWorld().getName().equalsIgnoreCase(MurderManager.getWorld().getName()))
            return;
        if (!(event.getAction().equals(Action.RIGHT_CLICK_BLOCK) || event.getAction()
                .equals(Action.RIGHT_CLICK_AIR))) return;
        if (!player.getInventory().getItemInMainHand().getType().equals(Material.IRON_SWORD)) return;
        if (cooldown.get(player.getUniqueId())) return;

        cooldown.put(player.getUniqueId(), true);
        Bukkit.getScheduler().runTaskLater(main, () -> cooldown.put(player.getUniqueId(), false),
                SWORD_COOLDOWN * 20);

        Vector direction = player.getEyeLocation().getDirection();
        ArmorStand armorStand = player.getWorld().spawn(player.getLocation()
                .add(0.0, 0.5, 0.0).add(direction), ArmorStand.class);

        armorStand.setItem(EquipmentSlot.HAND, new ItemBuilder(Material.IRON_SWORD, 1,
                "§4Murder Sword").setUnbreakable(true).addItemFlags(ItemFlag.values()).build());
        armorStand.setInvulnerable(true);
        armorStand.setVisible(false);
        armorStand.setArms(true);
        armorStand.addScoreboardTag("knife");
        armorStand.setBasePlate(false);
        armorStand.setRightArmPose(new EulerAngle(-0.1, 0, 4.7));
        armorStand.setVelocity(direction.normalize().multiply(1.1));

        task = Bukkit.getScheduler().runTaskTimer(main, () -> {
            armorStand.setVelocity(direction.normalize().multiply(1.1));
            Location location = armorStand.getEyeLocation();
            Block futureBlock = location.add(location.getDirection()).getBlock();
            if (!futureBlock.getType().equals(Material.AIR)) {
                armorStand.remove();
                player.playSound(player.getEyeLocation(), Sound.ENTITY_ITEM_BREAK, 1, 1);
                task.cancel();
            } else
                for (Entity entity : armorStand.getNearbyEntities(0.5, 0.5, 0.5))
                    if (!entity.getUniqueId().equals(player.getUniqueId())) {
                        if (entity.getType().equals(EntityType.PLAYER)) {
                            Player player2 = (Player) entity;
                            if (MurderManager.roleHashMap.containsKey(player2.getUniqueId()))
                                MurderManager.kill(player2);
                            else
                                player2.setHealth(0);
                        } else if (entity instanceof LivingEntity)
                            ((LivingEntity) entity).setHealth(0);
                        else
                            break;
                        armorStand.remove();
                        player.playSound(player.getEyeLocation(), Sound.ENTITY_PLAYER_DEATH, 1, 1);
                        task.cancel();
                    }
        }, 1, 1);
    }
}