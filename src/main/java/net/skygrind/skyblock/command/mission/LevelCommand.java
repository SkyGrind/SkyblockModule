package net.skygrind.skyblock.command.mission;

import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class LevelCommand extends BukkitCommand {

    public LevelCommand() {
        super("level");
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        Player player = (Player) sender;
        if (!(args.length == 0)) {
            MessageUtil.sendUrgent(player, "Incorrect arguments, use /level");
            return true;
        }
        //TODO FINISH THIS
        return true;
    }
}
