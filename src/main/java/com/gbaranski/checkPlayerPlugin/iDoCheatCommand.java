package com.gbaranski.checkPlayerPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class iDoCheatCommand implements CommandExecutor {
    ArrayList<Player> playersWantsBan = new ArrayList<>();
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if(CheckPlayerPlugin.checkIfPlayerInArray(player)) {
                if(playersWantsBan.contains(player)) {
                    CheckPlayerPlugin.removePlayerFromArray(player);
                    CheckPlayerPlugin.banPlayer(player, CheckPlayerPlugin.getInstance().getConfig().getString("AdmissionBanReason"), CheckPlayerPlugin.getInstance().getConfig().getString("BanAdmission"));
                    CheckPlayerPlugin.broadcastToAll(String.format(CheckPlayerPlugin.getInstance().getConfig().getString("AdmissionBanBroadcast"), player.getName()));
                    playersWantsBan.remove(player);
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("AreYouSureMessage")));
                playersWantsBan.add(player);
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&',CheckPlayerPlugin.getInstance().getConfig().getString("PlayerNotDuringCheckMessage")));
            }
        }
        return true;
    }
}
