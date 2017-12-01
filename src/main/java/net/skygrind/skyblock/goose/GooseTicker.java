package net.skygrind.skyblock.goose;

import com.islesmc.modules.api.API;
import com.islesmc.modules.api.framework.user.User;
import com.islesmc.modules.api.framework.user.profile.Profile;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import sun.applet.Main;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.concurrent.TimeUnit;

public class GooseTicker extends BukkitRunnable
{
    private static final DecimalFormat FORMAT = new DecimalFormat("0.0");

    private List<String> splitEqually(final String text, final int size)
    {
        List<String> ret = new ArrayList<>();

        for (int start = 0; start < text.length(); start += size)
            ret.add(text.substring(start, Math.min(text.length(), start + size)));
        return ret;
    }

    public static String formatTime(long time)
    {
        if (time > 60000L)
            return setLongFormat(time);
        else
            return format(time);
    }

    private static String format(long millisecond)
    {
        return FORMAT.format(millisecond / 1000.0D);
    }

    private static String setLongFormat(long paramMilliseconds)
    {
        if (paramMilliseconds < TimeUnit.MINUTES.toMillis(1L))
            return FORMAT.format(paramMilliseconds);
        return DurationFormatUtils.formatDuration(paramMilliseconds,
                                                  (paramMilliseconds >= TimeUnit.HOURS.toMillis(1L) ? "HH:" : "") +
                                                  "mm:ss");
    }

    private String translateString(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    @Override
    public void run()
    {
        for (Player player : Bukkit.getOnlinePlayers())
        {
            GooseScoreboard scoreboard = SkyBlock.getPlugin().getGooseHandler().getScoreboard(player);
            if (scoreboard == null)
                continue;

            scoreboard.clear();


            User user = API.getUserManager().findByUniqueId(player.getUniqueId());
            Profile profile = user.getProfile("Skyblock");
            scoreboard.add(translateString("&7&m---------"), translateString("&7&m---------"));
            scoreboard.add(translateString("&bBalance&7: "), translateString("&f$0"));
            Island island = SkyBlock.getPlugin().getIslandRegistry().getIslandForPlayer(player);
            if(island == null) {
                scoreboard.add(translateString("&bIsland&7: "), translateString("&fNone"));
            } else {
                String name = splitEqually(island.getName(), 13).get(0);
                scoreboard.add(translateString("&bIsland&7: "), translateString("&f " + name));
                scoreboard.add(translateString("&7\u00BB&b Level&7:"), translateString("&f " + island.getIslandLevel()));
                scoreboard.add(translateString("&7\u00BB&b Balance&7:"), translateString("&f " + SkyBlock.getPlugin().format(SkyBlock.getPlugin().getEconomy().getBalance(player))));
                scoreboard.add(translateString("&7\u00BB&b Members&7:"), translateString("&f " + island.getMembers().size() + "/" + island.getMaxPlayers()));
                scoreboard.add(translateString("&7\u00BB&b Type&7:"), translateString("&f " + island.getType().getDisplay()));
            }

            scoreboard.add("", "");
            scoreboard.add(translateString("&bshop.skypara"), translateString("&bdisemc.com"));
            scoreboard.add(translateString("&7&m---------"), translateString("&7&m---------"));
            scoreboard.update();
        }
    }
}
