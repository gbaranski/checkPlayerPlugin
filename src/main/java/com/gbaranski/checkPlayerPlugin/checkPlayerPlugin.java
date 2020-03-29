package com.gbaranski.checkPlayerPlugin;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class checkPlayerPlugin extends JavaPlugin {
    public void onEnable() {
        this.getCommand("sprawdz").setExecutor(new CheckCommand());
    }
}

class ManageJail {
    public boolean sendToJail(Player targetObject, Player senderObject) {
        targetObject.sendMessage("123");
        targetObject.teleport(new Location(Bukkit.getWorld("world"), 0, 100, 0));
        String broadcastMsg = "Gracz " + targetObject.getName() + " jest sprawdzany przez " + senderObject.getName();
        Bukkit.broadcastMessage(broadcastMsg);
        return true;
    }
}

class CheckCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player player = (Player) sender;
            if(args.length == 0) {
                player.sendMessage("Dostepne komendy:");
                player.sendMessage("/sprawdz gracz <nick>");
                player.sendMessage("/sprawdz info <nick>");
                player.sendMessage("/sprawdz clear <nick>");
                player.sendMessage("/sprawdz ban <nick>");
            }else {
                switch(args[0]) {
                    case "gracz":
                        try {
                            boolean doesPlayerExist = false;
                            for (Player onlinePlayer : Bukkit.getServer().getOnlinePlayers()) {
                                if(onlinePlayer.getName().equals(args[1])) {
                                    player.sendMessage("Found player");
                                    doesPlayerExist = true;
                                    ManageJail manageJail = new ManageJail();
                                    manageJail.sendToJail(onlinePlayer, player);
                                    break;
                                }
                            }
                            if(!doesPlayerExist) {
                                player.sendMessage("Player doesn't exist");
                            }
                        } catch (Exception e) {
                            player.sendMessage(e.toString());
                            player.sendMessage("Error, check if Your syntax is good");
                        }
                        break;
                        }
                }
            }
        return true;
    }
}