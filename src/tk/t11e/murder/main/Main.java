package tk.t11e.murder.main;
// Created by booky10 in Murder (19:36 16.01.20)

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        long milliseconds = System.currentTimeMillis();

        milliseconds = System.currentTimeMillis() - milliseconds;
        System.out.println("[Murder] It took " + milliseconds + "ms to initialize this plugin!");
    }
}