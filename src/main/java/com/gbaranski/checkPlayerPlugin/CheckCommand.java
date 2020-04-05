package com.gbaranski.checkPlayerPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CheckCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("Dostepne komendy:");
                player.sendMessage("/sprawdz gracz <nick>");
                player.sendMessage("/sprawdz info <nick>");
                player.sendMessage("/sprawdz clear <nick>");
                player.sendMessage("/sprawdz ban <nick>");
                player.sendMessage("/sprawdz lista");
            } else {
                switch (args[0]) {
                    case "lista":
                        player.sendMessage("lista:");
                        CheckPlayerPlugin.broadcastArrayPlayers();
                        break;
                    case "info":
                        break;
                }
            }
        }
        return true;
    }
}

