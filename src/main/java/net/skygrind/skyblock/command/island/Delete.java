package net.skygrind.skyblock.command.island;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class Delete extends RDCommand {


    IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    protected Delete() {
        super("delete");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length != 0) {
            MessageUtil.sendUrgent(player, "/island delete");
        }

        Island island = registry.getIslandForPlayer(player);

        if (island == null) {
            MessageUtil.sendUrgent(player, "You do not have an island to delete!");
            return;
        }

        if (!island.getOwner().equals(player.getUniqueId())) {
            MessageUtil.sendUrgent(player, "You do not have permission to do this!");
            return;
        }

        registry.deleteIsland(player, island);
        MessageUtil.sendGood(player, "Deleted your island.");
    }
}
