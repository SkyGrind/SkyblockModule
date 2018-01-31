package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandHomeCommand extends GooseCommand {
    public IslandHomeCommand() {
        super("home", Lists.newArrayList("go"), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            player.sendMessage(ChatColor.RED + "Usage: /island home");
            return;
        }

        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You do not have an island to teleport to.");
            return;
        }

        Island playerIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        System.out.println(" ");
        System.out.println(playerIsland.getName());
        Location spawn = playerIsland.getSpawn();
        System.out.println(" ");
        
        Location teleport = new Location(spawn.getWorld(), spawn.getBlockX(), spawn.getWorld().getHighestBlockYAt(spawn.getBlockX(), spawn.getBlockZ()) + 2, spawn.getZ());
        
        player.teleport(teleport);
        player.sendMessage(ChatColor.GREEN + "You have been teleported to your island's home.");
        return;
    }
    
}
