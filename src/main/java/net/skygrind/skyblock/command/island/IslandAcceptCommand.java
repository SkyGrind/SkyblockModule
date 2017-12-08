package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandAcceptCommand extends GooseCommand {
    private final IslandRegistry registry;

    public IslandAcceptCommand() {
        super("accept", Lists.newArrayList(), true);
        this.registry = SkyBlock.getPlugin().getIslandRegistry();

    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!registry.hasInvite(player)) {
            player.sendMessage(ChatColor.RED + "You do not have any pending invites.");
            return;
        }

        Island invite = registry.getInviteFor(player);

        invite.getMembers().add(player.getUniqueId());
        player.sendMessage(ChatColor.GREEN + String.format("You have joined %s island.", invite.getName()));

        Player owner = Bukkit.getPlayer(invite.getOwner());

        if (owner == null) {
            return;
        }

        owner.sendMessage(ChatColor.GREEN + String.format("%s has accepted your island invite.", player.getName()));
        registry.getIslandInvites().remove(player.getUniqueId());
        return;
    }
}
