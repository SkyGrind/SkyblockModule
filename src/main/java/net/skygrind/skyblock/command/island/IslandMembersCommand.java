package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import net.skygrind.skyblock.goose.GooseCommand;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class IslandMembersCommand extends GooseCommand {
    public IslandMembersCommand() {
        super("members", Lists.newArrayList("list"), true);
    }

    @Override
    public void execute(Player sender, String[] args) {
        if (args.length != 0) {
            sender.sendMessage(ChatColor.RED + "Usage: /island members");
            return;
        }


    }
}
