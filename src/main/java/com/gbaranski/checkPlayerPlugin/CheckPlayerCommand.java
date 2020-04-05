package com.gbaranski.checkPlayerPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayerCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("/sprawdz <nick>");
                player.sendMessage("Po wiecej /sprawdzanie");
            } else {
                Player targetPlayer = Bukkit.getPlayer(args[0]);
                if (targetPlayer == null) {
                    player.sendMessage("Player doesn't exist");
                    return true;
                }
                if (CheckPlayerPlugin.checkIfPlayerInArray(targetPlayer)) {
                    player.sendMessage("This player is already checked");
                    return true;
                }
                CheckPlayerPlugin.sendToJail(targetPlayer, player);
            }
        }
        return true;
    }
}
