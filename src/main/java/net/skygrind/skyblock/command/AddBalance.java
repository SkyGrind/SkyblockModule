package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class AddBalance extends BukkitCommand {
    public AddBalance() {
        super("setbalance");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;
        if (!sender.hasPermission("essentials.setbalance")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission for this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /setbalance <Player> <Amount>");
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

        int targetBalance = Integer.parseInt(args[1]);
        double needs = targetBalance - SkyBlock.getPlugin().getEconomy().getBalance(target);

        SkyBlock.getPlugin().getEconomy().depositPlayer(player, needs);

        sender.sendMessage(ChatColor.GREEN + String.format("You have paid %s to %s", needs, target.getName()));
        target.sendMessage(ChatColor.GREEN + String.format("You have been paid %s by %s", needs, sender.getName()));
        return true;
    }
}
