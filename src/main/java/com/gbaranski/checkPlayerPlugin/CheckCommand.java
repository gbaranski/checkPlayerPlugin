package com.gbaranski.checkPlayerPlugin;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class CheckCommand implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(player.hasPermission("checkPlayer.admin")) {

                if (args.length == 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("LineSeparator")));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&a/sprawdz <nick>\n/czysty <nick>\n/sprawdzanie info <nick>\n/sprawdzanie lista\n/sprawdzanie setSpawn\n/sprawdzanie setJail\n/sprawdzanie autor\n/sprawdzanie reload"));
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("LineSeparator")));
                } else {
                    switch (args[0].toLowerCase()) {
                        case "lista":
                            player.sendMessage("lista:");
                            CheckPlayerPlugin.printArrayOfPlayersDuringCheck((Player) sender);
                            break;
                        case "info":
                            player.sendMessage("Niedlugo");
                            break;
                        case "setjail":
                            CheckPlayerPlugin.saveJailLocation((Player) sender);
                            break;
                        case "setspawn":
                            CheckPlayerPlugin.saveSpawnLocation((Player) sender);
                            break;
                        case "autor":
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&',"&aAutorem jest &c&lgrzegorz19"));
                            player.sendMessage("https://github.com/gbaranski/checkPlayerPlugin");
                            break;
                        case "reload":
                            CheckPlayerPlugin.getInstance().reloadConfig();
                            player.sendMessage("Reloaded");
                            break;
                    }
                }
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', CheckPlayerPlugin.getInstance().getConfig().getString("NoPermission")) );
                return true;
            }
            }
        return true;
    }
}

