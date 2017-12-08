package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandHomeCommand extends GooseCommand {
    public IslandHomeCommand() {
        super("home", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length > 0) {
            MessageUtil.sendUrgent(player, "/island home");
            return;
        }

        if (!SkyBlock.getPlugin().getIslandRegistry().hasIsland(player)) {
            MessageUtil.sendUrgent(player, "You do not have an island to teleport to!");
            return;
        }

        Island playerIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);

        System.out.println(" ");
        System.out.println(playerIsland.getName());
        Location spawn = playerIsland.getSpawn();
        System.out.println(" ");


        Location teleport = new Location(SkyBlock.getPlugin().getIslandWorld(), spawn.getX(), spawn.getY(), spawn.getZ());

        player.teleport(teleport);
        MessageUtil.sendInfo(player, "Teleported you to your island home.");
        return;
    }
}
