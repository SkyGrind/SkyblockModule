package net.skygrind.skyblock.command.island;

import com.google.common.collect.Lists;
import com.islesmc.modules.api.API;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.goose.GooseCommand;
import net.skygrind.skyblock.island.Island;
import net.skygrind.skyblock.island.IslandRegistry;
import net.skygrind.skyblock.misc.MessageUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * Created by Matt on 2017-02-25.
 */
public class IslandInviteCommand extends GooseCommand {
    public IslandInviteCommand() {
        super("invite", Lists.newArrayList(), true);
    }

    @Override
    public void execute(Player player, String[] args) {
        if (args.length < 1) {
            player.sendMessage(ChatColor.RED + "Usage: /island invite <Player>");
            return;
        }

        Player target = Bukkit.getPlayer(args[0]);

        IslandRegistry registry = SkyBlock.getPlugin().getIslandRegistry();

        if (!registry.hasIsland(player)) {
            player.sendMessage(ChatColor.RED + "You do not currently have an island.");
            return;
        }

        Island island = registry.getIslandForPlayer(player);

        if (target == null) {
            player.sendMessage(ChatColor.RED + String.format("No player with the name or UUID of '%s' is online.", args[0]));
            return;
        }

        if (island.getMembers().size() >= island.getMaxPlayers()) {
            player.sendMessage(ChatColor.RED + String.format("Your island is currently limited to %s island members..", island.getMaxPlayers()));
            MessageUtil.sendServerTheme(player, "To increase this limit visit http://shop.skyparadisemc.com");
            return;
        }

        player.sendMessage(ChatColor.RED + String.format("You have invited %s to join your island.", target.getName()));

        TextComponent message = new TextComponent(ChatColor.GREEN + String.format("You have been invited to join %s's island.", player.getName()));
        TextComponent accept = new TextComponent(ChatColor.GREEN + "Click here to accept this invitation.");
        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is accept"));

        message.addExtra(accept);
        message.addExtra(ChatColor.GREEN + " or ");

        TextComponent decline = new TextComponent(ChatColor.GREEN + "click here to decline this invitation.");
        decline.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/is decline"));
        message.addExtra(decline);

        BaseComponent[] comps = new BaseComponent[]{message};

        target.playSound(target.getLocation(), Sound.ORB_PICKUP, 1, 1);
        target.spigot().sendMessage(comps);
        target.sendMessage(ChatColor.GREEN + String.format("Your invitation to join %s's island expires in 3 minutes.", player.getName()));

        registry.getIslandInvites().put(target.getUniqueId(), island);

        new BukkitRunnable() {
            @Override
            public void run() {
                registry.getIslandInvites().remove(target.getUniqueId());
            }
        }.runTaskLater(API.getPlugin(), 20 * 60 * 3L);
        return;
    }
}
