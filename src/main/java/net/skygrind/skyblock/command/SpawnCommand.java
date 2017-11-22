package net.skygrind.skyblock.command;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class SpawnCommand extends RDCommand {

    public SpawnCommand() {
        super("spawn");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        player.sendMessage(ChatColor.GOLD + ChatColor.BOLD.toString() + "[!] " + ChatColor.GRAY + "Sending you to spawn in 3 seconds...");

        new BukkitRunnable() {
            @Override
            public void run() {
                player.teleport(SkyBlock.getPlugin().getSpawn());
            }
        }.runTaskLater(SkyBlock.getPlugin(), 3 * 20L);
    }
}
