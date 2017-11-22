package net.skygrind.skyblock;

import net.skygrind.skyblock.island.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by Matt on 2017-02-10.
 */
public class SkyBlockAPI {

    public static Island getIsland(Player player) {
        return SkyBlock.getPlugin().getIslandRegistry().getInviteFor(player);
    }

    public static Island getIsland(Location location) {
        return SkyBlock.getPlugin().getIslandRegistry().getIslandAt(location);
    }
}
