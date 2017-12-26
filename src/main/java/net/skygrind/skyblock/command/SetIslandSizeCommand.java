package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.region.Region;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class SetIslandSizeCommand extends BukkitCommand {
    public SetIslandSizeCommand() {
        super("setislandsize");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if (!sender.hasPermission("island.setsize")) {
            sender.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }
        if (args.length != 2) {
            sender.sendMessage(ChatColor.RED + "Usage: /island setsize <player> <size>");
            return true;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        if (target == null) {
            sender.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' could be found.", args[0]));
            return true;
        }
        if (!target.isOnline()) {
            sender.sendMessage(ChatColor.RED + "No player with the name or UUID of '%s' is online.");
            return true;
        }
        Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer((Player) target);
        if (island == null) {
            sender.sendMessage(ChatColor.RED + String.format("No island for the player '%s' could be found.", args[0]));
            return true;
        }

        String sizeSTR = args[1];
        if (!StringUtils.isNumeric(sizeSTR)) {
            sender.sendMessage(ChatColor.RED + String.format("The argument '%s' is not a number.", args[1]));
            return true;
        }
        Integer size = Integer.parseInt(sizeSTR);
        Integer currentSize = island.getSize();
        island.setSize(size);
        Integer addition = size - currentSize;
        Region region = new Region(island.getContainer().getName(), island.getContainer().getMin().subtract(addition, 0, addition), island.getContainer().getMax().add(addition, 0, addition));
        island.setContainer(region);
        island.save();
        sender.sendMessage(ChatColor.GREEN + String.format("You have set %s's island size to %s.", args[0], args[1]));
        return true;
    }
}
