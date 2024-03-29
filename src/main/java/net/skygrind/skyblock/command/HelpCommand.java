package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

public class HelpCommand extends BukkitCommand {
    public HelpCommand() {
        super("help");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Island help: " + ChatColor.WHITE + "/is help");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Shop: " + ChatColor.WHITE + "/shop or visit the Shop NPC");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Auction House: " + ChatColor.WHITE + "/ah or visit the Auction House NPC");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Custom Enchants: " + ChatColor.WHITE + "/ce or visit the enchanter NPC");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Website: " + ChatColor.WHITE + "http://skyparadise-mc.com");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Discord: " + ChatColor.WHITE + "https://discord.gg/q8GZhgR");
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', SkyBlock.getPlugin().getServerConfig().getPrimaryColor()) + " Shop: " + ChatColor.WHITE + "shop.skyparadisemc.com");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        return false;
    }
}
