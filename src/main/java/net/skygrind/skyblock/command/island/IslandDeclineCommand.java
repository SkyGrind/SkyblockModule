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
public class IslandDeclineCommand extends GooseCommand {

    public IslandDeclineCommand() {
        super("decline", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] strings) {
        if (!SkyBlock.getPlugin().getIslandRegistry().hasInvite(player)) {
            player.sendMessage(ChatColor.RED + "You do not have any pending invitations.");
            return;
        }

        Island invite = SkyBlock.getPlugin().getIslandRegistry().getInviteFor(player);

        player.sendMessage(ChatColor.GREEN + String.format("You have declined %s's invite.", invite.getName()));
        Player owner = Bukkit.getPlayer(invite.getOwner());

        if (owner == null) {
            return;
        }

        owner.sendMessage(ChatColor.GREEN + String.format("%s has declined his invitation to your island.", player.getName()));
        SkyBlock.getPlugin().getIslandRegistry().getIslandInvites().remove(player.getUniqueId());
        return;
    }
}
