package net.skygrind.skyblock.command;

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
        sender.sendMessage(ChatColor.AQUA + " Island help: " + ChatColor.WHITE + "/is help");
        sender.sendMessage(ChatColor.AQUA + " Shop: " + ChatColor.WHITE + "/shop or visit the Shop NPC");
        sender.sendMessage(ChatColor.AQUA + " Auction House: " + ChatColor.WHITE + "/ah or visit the Auction House NPC");
        sender.sendMessage(ChatColor.AQUA + " Custom Enchants: " + ChatColor.WHITE + "/ce or visit the enchanter NPC");
        sender.sendMessage(ChatColor.AQUA + " Custom Enchants: " + ChatColor.WHITE + "/ce or visit the enchanter NPC");
        sender.sendMessage(" ");
        sender.sendMessage(ChatColor.AQUA + " Website: " + ChatColor.WHITE + "http://skyparadise-mc.com");
        sender.sendMessage(ChatColor.AQUA + " Discord: " + ChatColor.WHITE + "https://discord.gg/q8GZhgR");
        sender.sendMessage(ChatColor.AQUA + " Shop: " + ChatColor.WHITE + "shop.skyparadisemc.com");
        sender.sendMessage(ChatColor.GRAY.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
        return false;
    }
}
