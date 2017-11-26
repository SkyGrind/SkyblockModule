package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.goose.GooseCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-11.
 */
public class IslandBaseCommand extends GooseCommand {

    public IslandBaseCommand() {
        super("help", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length == 0) {
            player.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");
            player.sendMessage(ChatColor.LIGHT_PURPLE + "Island Help Page");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island " + ChatColor.WHITE + "Displays this help page.");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island create " + ChatColor.WHITE + "Creates a new island.");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island home " + ChatColor.WHITE + "Teleports you to your island's home.");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island leave " + ChatColor.WHITE + "IslandLeaveCommand your current island.");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island kick " + ChatColor.WHITE + "IslandKickCommand a member from your island.");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island level " + ChatColor.WHITE + "Check your islands current level.");
            player.sendMessage(ChatColor.LIGHT_PURPLE + " /island top -- " + ChatColor.WHITE + "Displays the top islands");
            player.sendMessage(ChatColor.LIGHT_PURPLE.toString() + ChatColor.STRIKETHROUGH + "-----------------------------------------------------");

        }
    }
}
