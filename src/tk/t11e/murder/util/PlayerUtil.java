package tk.t11e.murder.util;
// Created by booky10 in knockIt (19:28 14.01.20)

import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;

import java.util.Objects;

public class PlayerUtil {

    public static double getMaxHealth(Player player) {
        return Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getValue();
    }

    public static void setMaxHealth(Player player, double value) {
        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(value);
    }
}