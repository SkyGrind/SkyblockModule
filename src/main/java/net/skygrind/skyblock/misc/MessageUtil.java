package net.skygrind.skyblock.misc;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-26.
 */
public class MessageUtil {

    public static void sendUrgent(Player player, String msg) {
        player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.RED + msg);
    }

    public static void sendInfo(Player player, String msg) {
        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[!] " + ChatColor.GOLD + msg);
    }

    public static void sendGood(Player player, String msg) {
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GREEN + msg);
    }

    public static void sendServerTheme(Player player, String msg) {
        player.sendMessage(ChatColor.AQUA + ChatColor.BOLD.toString() + "Sky" + ChatColor.WHITE + ChatColor.BOLD + "Paradise " + ChatColor.GREEN + "\u00BB " + ChatColor.RESET + msg);
    }
}
