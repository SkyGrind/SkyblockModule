package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class PayCommand extends BukkitCommand {
    public PayCommand() {
        super("pay");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /pay <Player> <Amount>");
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name '%s' was found.", args[0]));
            return true;
        }

        if (!StringUtils.isNumeric(args[1])) {
            sender.sendMessage(ChatColor.RED + String.format("The argument '%s' is not a number.", args[1]));
            return true;
        }

        int toPay = Integer.valueOf(args[1]);
        if (!SkyBlock.getPlugin().getEconomy().has(player, toPay)) {
            sender.sendMessage(ChatColor.RED + String.format("You do not have $%s to pay %s.", toPay, target.getName()));
            return true;
        }
        SkyBlock.getPlugin().getEconomy().withdrawPlayer(player, toPay);
        SkyBlock.getPlugin().getEconomy().depositPlayer(target, toPay);
        sender.sendMessage(ChatColor.GREEN + String.format("You have paid %s to %s", toPay, target.getName()));
        target.sendMessage(ChatColor.GREEN + String.format("You have been paid %s by %s", toPay, sender.getName()));
        return true;
    }
}
