package com.gbaranski.checkPlayerPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class CheckPlayerPlugin extends JavaPlugin {
    public static ArrayList<Player> playersDuringCheck = new ArrayList<>();

    public static void sendToJail(Player targetObject, Player senderObject) {
        targetObject.sendMessage("Jestes sprawdzany, wejdz na ts.mctc.pl");
        targetObject.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        String broadcastMsg = "Gracz " + targetObject.getName() + " jest sprawdzany przez " + senderObject.getName();
        Bukkit.broadcastMessage(broadcastMsg);
        CheckPlayerPlugin.playersDuringCheck.add(targetObject);
    }

    public static boolean checkIfPlayerInArray(Player targetObject) {
        return playersDuringCheck.contains(targetObject);
    }

    public static void broadcastArrayPlayers() {
        for (Player player : playersDuringCheck) {
            Bukkit.broadcastMessage(player.getPlayer().getName());
        }
    }

    public static void banPlayer(Player targetObject, String banReason) {
        Bukkit.broadcastMessage(targetObject.getName() + "dostal bana za " + banReason);
        ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
        String commandToExecute = "ban " + targetObject.getName() + " " + banReason;
        Bukkit.broadcastMessage(commandToExecute);
        Bukkit.dispatchCommand(console, commandToExecute);
        playersDuringCheck.remove(targetObject);
    }

    public static void clearPlayer(Player targetPlayer, Player sender) {
        Bukkit.broadcastMessage(targetPlayer.getName() + " uznany jako czysty przez" + sender.getName());
        playersDuringCheck.remove(targetPlayer);
    }

    public void onEnable() {
        this.getCommand("sprawdzanie").setExecutor(new CheckCommand());
        this.getCommand("sprawdz").setExecutor(new CheckPlayerCommand());
        this.getCommand("czysty").setExecutor(new ClearPlayerCommand());
        getServer().getPluginManager().registerEvents(new PrepareListener(), this);
    }
}

class PrepareListener implements Listener {
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent eventObject) {
        if (CheckPlayerPlugin.checkIfPlayerInArray(eventObject.getPlayer())) {
            CheckPlayerPlugin.banPlayer(eventObject.getPlayer(), "Wyjscie podczas sprawdzania");
            Bukkit.broadcastMessage(eventObject.getPlayer().getName() + " wyszedl i dostal bana");
        }
    }
}