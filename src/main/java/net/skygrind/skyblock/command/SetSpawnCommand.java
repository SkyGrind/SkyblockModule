package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class SetSpawnCommand extends RDCommand {

    public SetSpawnCommand() {
        super("setspawn");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (!player.hasPermission("skygrind.setspawn")) {
            player.sendMessage(ChatColor.RED + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "You do not have permission to do this!");
            return;
        }

        Location loc = player.getLocation();

        SkyBlock.getPlugin().setSpawn(loc);
        player.sendMessage(ChatColor.GREEN + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Set server spawn to your location.");
        return;
    }
}
