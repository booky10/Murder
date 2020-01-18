package tk.t11e.murder.main;
// Created by booky10 in Murder (19:36 16.01.20)

import org.bukkit.Bukkit;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import tk.t11e.murder.commands.Murder;
import tk.t11e.murder.listener.DamageListener;
import tk.t11e.murder.listener.FoodChangeListener;
import tk.t11e.murder.listener.InteractListener;
import tk.t11e.murder.listener.JoinLeaveListener;
import tk.t11e.murder.manager.MurderManager;
import tk.t11e.murder.util.WorldUtils;

import java.util.Objects;

public class Main extends JavaPlugin {

    public static final String PREFIX = "§7[§bT11E§7]§c ", NO_PERMISSION = PREFIX + "You don't" +
            "the permissions for this!";

    @Override
    public void onEnable() {
        long milliseconds = System.currentTimeMillis();

        saveDefaultConfig();
        initCommands();
        initListener(Bukkit.getPluginManager());
        for (Player player : Bukkit.getOnlinePlayers())
            InteractListener.cooldown.put(player.getUniqueId(),false);
        MurderManager.testForStart(MurderManager.getWorld().getPlayerCount());

        milliseconds = System.currentTimeMillis() - milliseconds;
        System.out.println("[Murder] It took " + milliseconds + "ms to initialize this plugin!");
    }

    @Override
    public void onDisable() {
        for (Entity entity : MurderManager.getWorld().getEntitiesByClasses(ArmorStand.class))
            if(entity.getScoreboardTags().contains("knife"))
                entity.remove();
    }

    private void initListener(PluginManager pluginManager) {
        pluginManager.registerEvents(new JoinLeaveListener(), this);
        pluginManager.registerEvents(new FoodChangeListener(), this);
        pluginManager.registerEvents(new DamageListener(), this);
        pluginManager.registerEvents(new InteractListener(), this);
    }

    private void initCommands() {
        Objects.requireNonNull(getCommand("murder")).setExecutor(new Murder());
    }
}