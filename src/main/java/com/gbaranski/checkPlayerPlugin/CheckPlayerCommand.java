package com.gbaranski.checkPlayerPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CheckPlayerCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("checkPlayer.admin")) {
                if (args.length == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&c/sprawdz <nick>"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("CheckMoreCommandMessage")));
                } else {
                    Player targetPlayer = Bukkit.getPlayer(args[0]);
                    if (targetPlayer == null) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("PlayerDoesntExistMessage")));
                        return true;
                    }
                    if (CheckPlayerPlugin.checkIfPlayerInArray(targetPlayer)) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("PlayerAlreadyChecked")));
                        return true;
                    }
                    CheckPlayerPlugin.sendToJail(targetPlayer, player);
                }
            } else
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CheckPlayerPlugin.getInstance().getConfig().getString("NoPermission")));

        }
        return true;
    }
}
