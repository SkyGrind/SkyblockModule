package net.skygrind.skyblock.command.island;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import tech.rayline.core.command.CommandException;
import tech.rayline.core.command.RDCommand;

/**
 * Created by Matt on 2017-02-25.
 */
public class Invite extends RDCommand {

    protected Invite() {
        super("invite");
    }

    @Override
    protected void handleCommand(Player player, String[] args) throws CommandException {
        if (args.length < 1) {
            MessageUtil.sendUrgent(player, "/island invite [Player]");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

        if (!registry.hasIsland(player)) {
            MessageUtil.sendUrgent(player, "You do not have an island!");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (target == null) {
            MessageUtil.sendUrgent(player, "That player is not currently online!");
            return;
        }

        if (island.getMembers().size() >= island.getMaxPlayers()) {
            MessageUtil.sendUrgent(player, "You're currently restricted to 4 island members...");
            MessageUtil.sendServerTheme(player, "To increase this visit http://store.skygrind.net");
            return;
        }

        MessageUtil.sendGood(player, "Invited " + ChatColor.GOLD + target.getName() + ChatColor.GREEN + " to join your island.");

        TextComponent message = new TextComponent(ChatColor.GRAY + "You've been invited to join " + ChatColor.GOLD + player.getName() + ChatColor.GRAY + "'s island");
        TextComponent accept = new TextComponent(ChatColor.GREEN + ChatColor.BOLD.toString() + " /ACCEPT ");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/accept"));

        message.addExtra(accept);
        message.addExtra(ChatColor.GRAY + "or ");

        TextComponent decline = new TextComponent(ChatColor.RED + ChatColor.BOLD.toString() + "/DECLINE");
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/decline"));
        message.addExtra(decline);

        BaseComponent[] comps = new BaseComponent[]{message};

        target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1,1);
        target.spigot().sendMessage(comps);
        target.sendMessage(ChatColor.GRAY + "This invite expires in 3 minutes!");

        registry.getIslandInvites().put(target.getUniqueId(), island);

        new BukkitRunnable() {
            @Override
            public void run() {
                registry.getIslandInvites().remove(target.getUniqueId());
            }
        }.runTaskLater(SkyBlock.getPlugin(), 20*60*3L);
    }
}
