package tk.t11e.murder.manager;
// Created by booky10 in Murder (21:32 17.01.20)

import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import tk.t11e.murder.main.Main;
import tk.t11e.murder.util.Murder;
import tk.t11e.murder.util.WorldUtils;
import tk.t11e.murder.util.ItemBuilder;

import java.util.*;


public class MurderManager {

    private static final Main main = Main.getPlugin(Main.class);
    public static final int NEEDED_PLAYERS = 2, TIME = 15;
    public static boolean isRunning = false;
    public static int starting = 0;
    private static BukkitTask task;
    public static final HashMap<UUID, Murder> roleHashMap = new HashMap<>();

    public static void joinPlayer(Player player) {
        if (!isRunning) {
            player.teleport(getLobby());
            player.getInventory().clear();
            player.setGameMode(GameMode.ADVENTURE);
            WorldUtils.sendMessageInWorld(getWorld(), Main.PREFIX + "§a" + (getWorld()
                    .getPlayerCount() + 1) + "§7/§a" + NEEDED_PLAYERS + " players are here!");
            testForStart(getWorld().getPlayerCount() + 1);
        } else
            joinSpectator(player,true);
    }

    private static void joinSpectator(Player player, Boolean sendMessage) {
        roleHashMap.remove(player.getUniqueId());
        roleHashMap.put(player.getUniqueId(),Murder.DEAD);

        player.teleport(getLobby());
        player.setGameMode(GameMode.SPECTATOR);
        if(sendMessage)
        player.sendMessage(Main.PREFIX + "You are a spectator!");
    }

    public static Location getLobby() {
        FileConfiguration config = main.getConfig();

        double x = config.getDouble("Lobby.X");
        double y = config.getDouble("Lobby.Y");
        double z = config.getDouble("Lobby.Z");

        return new Location(getWorld(), x, y, z, 0, 0);
    }

    public static void setLobby(Location location) {
        FileConfiguration config = main.getConfig();

        config.set("Lobby.X", location.getBlockX());
        config.set("Lobby.Y", location.getBlockY());
        config.set("Lobby.Z", location.getBlockZ());

        main.saveConfig();
    }

    public static void addSpawnPoint(Location location) {
        FileConfiguration config = main.getConfig();
        List<String> spawnPoints = config.getStringList("SpawnLocations");

        spawnPoints.add(location.getBlockX() + ";" + location.getBlockY() + ";" + location.getBlockZ());
        config.set("SpawnLocations", spawnPoints);

        main.saveConfig();
    }

    public static void clearSpawnPoints() {
        FileConfiguration config = main.getConfig();

        config.set("SpawnLocations", new ArrayList<String>());

        main.saveConfig();
    }

    public static void spawnPlayer(Player player) {
        FileConfiguration config = main.getConfig();
        List<String> spawnPoints = config.getStringList("SpawnLocations");
        int spawnPointAmount = spawnPoints.size();
        int randomPointNumber = new Random().nextInt(spawnPointAmount - 1);
        String randomPoint = spawnPoints.get(randomPointNumber);
        String[] randomPointList = randomPoint.split(";");
        ArrayList<Double> randomPointListDouble = new ArrayList<>();

        for (String randomPointString : randomPointList)
            randomPointListDouble.add(Double.parseDouble(randomPointString) + 0.5);

        Location randomLocation = new Location(getWorld(), randomPointListDouble.get(0),
                randomPointListDouble.get(1), randomPointListDouble.get(2),
                player.getLocation().getYaw(), player.getLocation().getPitch());
        randomLocation.add(0.0, -0.45, 0.0);
        player.teleport(randomLocation);
    }

    public static void randomizeRoles() {
        ArrayList<Player> players = WorldUtils.getPlayersInWorld(getWorld());

        int detective = new Random().nextInt(players.size());
        roleHashMap.put(players.get(detective).getUniqueId(), Murder.DETECTIVE);
        players.get(detective).sendMessage(Main.PREFIX + "§aYou are §9Detective§a!");
        players.get(detective).sendTitle("§aYou are", "§9Detective", 10, 60, 10);
        players.remove(detective);

        int murder = new Random().nextInt(players.size());
        roleHashMap.put(players.get(murder).getUniqueId(), Murder.MURDER);
        players.get(murder).sendMessage(Main.PREFIX + "§aYou are §4Murder§a!");
        players.get(murder).sendTitle("§aYou are", "§4Murder", 10, 60, 10);
        players.remove(murder);

        for (Player player : players) {
            roleHashMap.put(player.getUniqueId(), Murder.INNOCENT);
            players.get(detective).sendMessage(Main.PREFIX + "§aYou are §7Innocent§a!");
            players.get(detective).sendTitle("§aYou are", "§7Innocent", 10, 60, 10);
        }
    }

    public static void testForStart(int players) {
        if (players >= NEEDED_PLAYERS)
            startScheduler();
    }

    public static void startScheduler() {
        task = Bukkit.getScheduler().runTaskTimer(main, () -> {
            if (starting == 0 && !isRunning)
                starting = TIME;
            for (Player player : WorldUtils.getPlayersInWorld(getWorld())) {
                player.setExp(0);
                player.setLevel(starting);
            }
            switch (starting) {
                case 120:
                    WorldUtils.sendMessageInWorld(getWorld(), Main.PREFIX +
                            "§aStarting in 2 minutes!");
                    break;
                case 60:
                    WorldUtils.sendMessageInWorld(getWorld(), Main.PREFIX +
                            "§aStarting in 1 minute!");
                    break;
                case 30:
                case 15:
                case 10:
                case 5:
                case 4:
                case 3:
                case 2:
                    WorldUtils.sendMessageInWorld(getWorld(), Main.PREFIX +
                            "§aStarting in " + starting + " seconds!");
                    break;
                case 1:
                    WorldUtils.sendMessageInWorld(getWorld(), Main.PREFIX +
                            "§aStarting in " + starting + " second!");
                    Bukkit.getScheduler().runTaskLater(main, () -> {
                        isRunning = true;
                        for (Player player : WorldUtils.getPlayersInWorld(getWorld())) {
                            player.setLevel(0);
                            player.sendMessage(Main.PREFIX + "§aStarting now!");
                        }
                        start();
                    }, 20);
                    task.cancel();
                    break;
                default:
                    break;
            }
            starting--;
        }, 20, 20);
    }

    public static void start() {
        randomizeRoles();
        for (Player player : WorldUtils.getPlayersInWorld(getWorld())) {
            player.setGameMode(GameMode.ADVENTURE);
            player.setExp(0);
            player.getInventory().clear();
            spawnPlayer(player);
            setItems(player);
        }
    }

    public static void setItems(Player player) {
        if (!roleHashMap.containsKey(player.getUniqueId())) return;
        switch (roleHashMap.get(player.getUniqueId())) {
            case MURDER:
                ItemStack knife = new ItemBuilder(Material.IRON_SWORD, 1, "§4Murder Sword")
                        .setUnbreakable(true).addItemFlags(ItemFlag.values()).build();
                player.getInventory().setItem(1, knife);
                break;
            case DETECTIVE:
                ItemStack rifle = new ItemBuilder(Material.BOW, 1, "§9Detective Rifle")
                        .setUnbreakable(true).addEnchantment(Enchantment.ARROW_INFINITE, 1)
                        .addItemFlags(ItemFlag.values()).build();
                player.getInventory().setItem(1, rifle);
                player.getInventory().setItem(9, new ItemBuilder(Material.ARROW,1,
                        "§fArrow").build());
                break;
            case INNOCENT:
                ItemStack stick = new ItemBuilder(Material.STICK, 1, "§7Innocent Stick")
                        .addItemFlags(ItemFlag.values()).build();
                player.getInventory().setItem(0, stick);
                break;
            default:
                break;
        }
    }

    public static void kill(Player player) {
        if (!roleHashMap.containsKey(player.getUniqueId())) return;
        if (!isRunning) return;
        if (roleHashMap.get(player.getUniqueId()).equals(Murder.MURDER))
            testForEnd(true);
        else{
            player.sendMessage(Main.PREFIX + "§cYou died!");
            testForEnd(false);
        }
        joinSpectator(player, false);
    }

    public static void end(Murder winner) {
        switch (winner) {
            case INNOCENT:
            case DETECTIVE:
                for (Player player : WorldUtils.getPlayersInWorld(getWorld()))
                    if (roleHashMap.containsKey(player.getUniqueId()))
                        if (roleHashMap.get(player.getUniqueId()).equals(Murder.MURDER)) {
                            player.sendMessage(Main.PREFIX + "§cYou lost!");
                            player.sendTitle("§cYou lost!", "", 10, 60, 10);
                        } else {
                            player.sendMessage(Main.PREFIX + "§aCongratulations! You won!");
                            player.sendTitle("§aCongratulations!", "§aYou won!", 10, 60, 10);
                        }
                    else
                        player.sendMessage(Main.PREFIX + "§a The §7Innocents§a won!");
                break;
            case MURDER:
                for (Player player : WorldUtils.getPlayersInWorld(getWorld()))
                    if (roleHashMap.containsKey(player.getUniqueId()))
                        if (roleHashMap.get(player.getUniqueId()).equals(Murder.MURDER)) {
                            player.sendMessage(Main.PREFIX + "§aCongratulations! You won and killed " +
                                    "everybody!");
                            player.sendTitle("§aCongratulations!", "§aYou won!", 10, 60, 10);
                        } else {
                            player.sendMessage(Main.PREFIX + "§cYou lost!");
                            player.sendTitle("§cYou lost!", "", 10, 60, 10);
                        }
                    else
                        player.sendMessage(Main.PREFIX + "§a The §4Murder§a won!");
                break;
            default:
                break;
        }
        for (Player player : WorldUtils.getPlayersInWorld(getWorld()))
            player.sendMessage(Main.PREFIX + "Restarting server in 30 seconds!");

        Bukkit.getScheduler().runTaskLater(main, () -> {
            for (Player player : WorldUtils.getPlayersInWorld(getWorld())) {
                player.sendMessage(Main.PREFIX + "Restarting server...");
                if (!player.hasPermission("stay"))
                    player.chat("/lobby");

                Bukkit.getScheduler().runTaskLater(main, Bukkit::reload, 30);
            }
        }, 30 * 20);
    }

    public static void testForEnd(boolean murderDead) {
        int alive=-1;
        for (UUID uuid : roleHashMap.keySet())
            if(!roleHashMap.get(uuid).equals(Murder.DEAD))
                alive++;
        if(murderDead)
            end(Murder.INNOCENT);
        else if(alive<=1)
            end(Murder.MURDER);
    }

    public static World getWorld() {
        FileConfiguration config = main.getConfig();
        return Bukkit.getWorld(Objects.requireNonNull(config.getString("World")));
    }
}