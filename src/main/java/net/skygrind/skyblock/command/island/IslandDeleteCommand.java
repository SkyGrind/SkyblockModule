package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.entity.Player;
import xyz.sethy.commands.SubCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandDeleteCommand extends SubCommand {
    private final IslandRegistry registry;

    public IslandDeleteCommand() {
        super("delete", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();
    }


    @Override
    public void execute(Player player, String[] args) {
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
        return;
    }
}
