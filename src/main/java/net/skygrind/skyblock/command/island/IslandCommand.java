package net.skygrind.skyblock.command.island;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.IslandRegistry;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.CommandMeta;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-11.
 */
@CommandMeta(aliases = {"is"})
public class IslandCommand extends RDCommand {

    private IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

    public IslandCommand() {
        super("island", new Create(), new Home(), new Invite(), new Leave(), new Kick(), new Delete(), new IslandLevel(), new IslandTop());
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length == 0) {
            player.sendMessage(ChatColor.YELLOW + "-----" + ChatColor.GRAY + "SkyBlock Help" + ChatColor.YELLOW + "-----");
            player.sendMessage(ChatColor.YELLOW + "/island " + ChatColor.GRAY + "Aliases: /is");
            player.sendMessage(ChatColor.YELLOW + "/island create -- " + ChatColor.GRAY + "Initiates creation of a new island");
            player.sendMessage(ChatColor.YELLOW + "/island home -- " + ChatColor.GRAY + "Teleports you to island home");
            player.sendMessage(ChatColor.YELLOW + "/island leave -- " + ChatColor.GRAY + "Abandon your island");
            player.sendMessage(ChatColor.YELLOW + "/island kick [Player] -- " + ChatColor.GRAY + "Kicks player from your island");
            player.sendMessage(ChatColor.YELLOW + "/island level -- " + ChatColor.GRAY + "Reloads your island level");
            player.sendMessage(ChatColor.YELLOW + "/island top -- " + ChatColor.GRAY + "Displays a GUI of the top islands");

        }
    }
}
