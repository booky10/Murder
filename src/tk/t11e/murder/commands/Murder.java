package tk.t11e.murder.commands;
// Created by booky10 in Murder (21:19 17.01.20)

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import tk.t11e.murder.main.Main;
import tk.t11e.murder.manager.MurderManager;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("NullableProblems")
public class Murder implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("murder")) {
                if (args.length == 1)
                    if (args[0].equalsIgnoreCase("setLobby")) {
                        MurderManager.setLobby(player.getLocation());
                        player.sendMessage(Main.PREFIX + "§aSuccessfully set lobby!");
                    } else if (args[0].equalsIgnoreCase("addSpawnPoint")) {
                        MurderManager.addSpawnPoint(player.getLocation());
                        player.sendMessage(Main.PREFIX + "§aSuccessfully added spawn point!");
                    } else if (args[0].equalsIgnoreCase("clearSpawnPoints")) {
                        MurderManager.clearSpawnPoints();
                        player.sendMessage(Main.PREFIX + "§aSuccessfully cleared spawn points!");
                    } else
                        MurderManager.spawnPlayer(player);
                else
                    return false;
            } else
                player.sendMessage(Main.NO_PERMISSION);
        } else
            sender.sendMessage("You must be a player to execute this command!");
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> list = new ArrayList<>();
        if(args.length==1) {
            list.add("setLobby");
            list.add("addSpawnPoint");
            list.add("clearSpawnPoints");
        }
        return list;
    }
}