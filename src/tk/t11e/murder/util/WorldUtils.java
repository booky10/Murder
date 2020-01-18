package tk.t11e.murder.util;
// Created by booky10 in Murder (22:29 17.01.20)

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class WorldUtils {

    public static ArrayList<Player> getPlayersInWorld(World world) {
        ArrayList<Player> players = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers())
            if(player.getWorld().getName().equalsIgnoreCase(world.getName()))
                players.add(player);

        return players;
    }

    public static void sendMessageInWorld(World world, String message) {
        for (Player player : getPlayersInWorld(world))
            player.sendMessage(message);
    }
}