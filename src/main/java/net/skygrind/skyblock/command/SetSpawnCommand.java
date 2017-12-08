package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-25.
 */
public class SetSpawnCommand extends BukkitCommand {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;
        if (!player.hasPermission("islesmc.setspawn")) {
            player.sendMessage(ChatColor.RED + "You do not have permission to execute this command.");
            return true;
        }

        Location loc = player.getLocation();

        SkyBlock.getPlugin().getServerConfig().setSpawnLocation(GooseLocation.fromLocation(loc));
        SkyBlock.getPlugin().getServerConfig().save();
        player.sendMessage(ChatColor.GREEN + "You have successfully set the server's spawn.");
        return true;
    }
}
