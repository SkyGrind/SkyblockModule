package net.skygrind.skyblock.command.mission;

import net.skygrind.skyblock.misc.MessageUtil;
import net.skygrind.skyblock.missions.Mission;
import org.bukkit.entity.Player;
import tech.rayline.core.command.CommandMeta;
import tech.rayline.core.command.RDCommand;

@CommandMeta(description="Get rewarded for your level", usage="/level")
public class LevelCommand extends RDCommand {
    
    public LevelCommand() {
        super("level");
    }
    
    @Override
    protected void handleCommand(Player player, String[] args) {
        if (!(args.length == 0)) {
            MessageUtil.sendUrgent(player, "Incorrect arguments, use /level");
            return;
        }
        
    }
}
