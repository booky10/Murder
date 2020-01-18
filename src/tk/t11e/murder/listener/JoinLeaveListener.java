package tk.t11e.murder.listener;
// Created by booky10 in Murder (21:32 17.01.20)

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import tk.t11e.murder.manager.MurderManager;

public class JoinLeaveListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("");
        MurderManager.joinPlayer(event.getPlayer());
            InteractListener.cooldown.put(event.getPlayer().getUniqueId(),false);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("");
        if(!event.getPlayer().getWorld().getName().equalsIgnoreCase(MurderManager.getWorld().getName()))
            return;
        if(!MurderManager.roleHashMap.containsKey(event.getPlayer().getUniqueId())) return;
        MurderManager.kill(event.getPlayer());
    }
}