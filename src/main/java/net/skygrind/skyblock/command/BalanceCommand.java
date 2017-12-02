package net.skygrind.skyblock.command;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class BalanceCommand extends BukkitCommand {
    public BalanceCommand() {
        super("balance");
        setAliases(Lists.newArrayList("econ", "money", "bal", "economoy"));
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        sender.sendMessage(ChatColor.GREEN + "Balance: " + ChatColor.WHITE + "$" + SkyBlock.getPlugin().getEconomy().getBalance((Player) sender));
        return true;
    }
}
