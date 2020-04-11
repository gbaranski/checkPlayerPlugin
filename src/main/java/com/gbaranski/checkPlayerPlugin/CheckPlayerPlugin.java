package com.gbaranski.checkPlayerPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CheckPlayerPlugin extends JavaPlugin {
    public static ArrayList<Player> playersDuringCheck = new ArrayList<>();
    private static CheckPlayerPlugin instance;


    public static CheckPlayerPlugin getInstance() {
        return instance;
    }

    public static void sendToJail(Player targetObject, Player senderObject) {
        CheckPlayerPlugin.broadcastToAll(ChatColor.translateAlternateColorCodes('&',String.format(CheckPlayerPlugin.getInstance().getConfig().getString("BroadcastOnCheck"), targetObject.getName(), senderObject.getName())));
        targetObject.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("LineSeparator")));
        targetObject.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("MessageForPlayerToCheck")));
        targetObject.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("LineSeparator")));
        targetObject.teleport(new Location(Bukkit.getWorld("world"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("jailX"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("jailY"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("jailZ")));
        CheckPlayerPlugin.playersDuringCheck.add(targetObject);
    }

    public static void saveSpawnLocation(Player sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                CheckPlayerPlugin.getInstance().getConfig().getString("OnSpawnSave")));

        CheckPlayerPlugin.getInstance().getConfig().set("spawnX",
                sender.getLocation().getX());

        CheckPlayerPlugin.getInstance().getConfig().set("spawnY",
                sender.getLocation().getY());

        CheckPlayerPlugin.getInstance().getConfig().set("spawnZ",
                sender.getLocation().getZ());
        CheckPlayerPlugin.getInstance().saveConfig();
    }

    public static void saveJailLocation(Player sender) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&',
                CheckPlayerPlugin.getInstance().getConfig().getString("OnJailSave")));

        CheckPlayerPlugin.getInstance().getConfig().set("jailX",
                sender.getLocation().getX());

        CheckPlayerPlugin.getInstance().getConfig().set("jailY",
                sender.getLocation().getY());

        CheckPlayerPlugin.getInstance().getConfig().set("jailZ",
                sender.getLocation().getZ());
        CheckPlayerPlugin.getInstance().saveConfig();
    }

    public static boolean checkIfPlayerInArray(Player targetObject) {
        return playersDuringCheck.contains(targetObject);
    }
    public static void removePlayerFromArray(Player targetObject) {
        playersDuringCheck.remove(targetObject);
    }

    public static void printArrayOfPlayersDuringCheck(Player sender) {
        for (Player player : playersDuringCheck) {
            sender.sendMessage(player.getPlayer().getName());
        }
    }

    public static void banPlayer(Player targetObject, String banReason, String banTime) {
        CheckPlayerPlugin.broadcastToAll(ChatColor.translateAlternateColorCodes('&',String.format(CheckPlayerPlugin.getInstance().getConfig().getString("BroadcastOnBan"), targetObject.getName(), banReason)));
        targetObject.teleport(new Location(Bukkit.getWorld("world"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("spawnX"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("spawnY"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("spawnZ")));
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String commandToExecute = "ban " + targetObject.getName() + " " + banTime + " " + banReason;
        Bukkit.dispatchCommand(console, commandToExecute);
        playersDuringCheck.remove(targetObject);
    }

    public static void clearPlayer(Player targetPlayer, Player sender) {
        CheckPlayerPlugin.broadcastToAll(ChatColor.translateAlternateColorCodes('&',String.format(CheckPlayerPlugin.getInstance().getConfig().getString("BroadcastClearPlayer"), targetPlayer.getName(), sender.getName())));
        targetPlayer.teleport(new Location(Bukkit.getWorld("world"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("spawnX"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("spawnY"),
                CheckPlayerPlugin.getInstance().getConfig().getInt("spawnZ")));
        playersDuringCheck.remove(targetPlayer);
    }

    public static void broadcastToAll(String message) {
        for (Player targetPlayer : Bukkit.getOnlinePlayers()) {
            targetPlayer.sendMessage(message);
        }
    }

    public void onEnable() {
        instance = this;
        CheckPlayerPlugin.getInstance().getConfig().options().copyDefaults(true);
        CheckPlayerPlugin.getInstance().saveDefaultConfig();
        this.getCommand("sprawdzanie").setExecutor(new CheckCommand());
        this.getCommand("sprawdz").setExecutor(new CheckPlayerCommand());
        this.getCommand("czysty").setExecutor(new ClearPlayerCommand());
        this.getCommand("przyznajesie").setExecutor(new iDoCheatCommand());

        getServer().getPluginManager().registerEvents(new PrepareListener(), this);
    }
    public void onDisable() {
        instance = null;
    }
}

class PrepareListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent eventObject) {
        if (CheckPlayerPlugin.checkIfPlayerInArray(eventObject.getPlayer())) {
            CheckPlayerPlugin.banPlayer(eventObject.getPlayer(), CheckPlayerPlugin.getInstance().getConfig().getString("OnLeaveBanReason"), CheckPlayerPlugin.getInstance().getConfig().getString("BanLogout"));
            CheckPlayerPlugin.broadcastToAll(ChatColor.translateAlternateColorCodes('&',String.format(CheckPlayerPlugin.getInstance().getConfig().getString("OnLeaveBanBroadcast"), eventObject.getPlayer().getName())));
        }
    }
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent eventObject) {
        if(CheckPlayerPlugin.checkIfPlayerInArray(eventObject.getPlayer())) {
            eventObject.getPlayer().teleport(new Location(Bukkit.getWorld("world"),
                    CheckPlayerPlugin.getInstance().getConfig().getInt("jailX"),
                    CheckPlayerPlugin.getInstance().getConfig().getInt("jailY"),
                    CheckPlayerPlugin.getInstance().getConfig().getInt("jailZ")));
        }
    }
}