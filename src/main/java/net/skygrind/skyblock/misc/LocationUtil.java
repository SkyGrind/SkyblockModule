package net.skygrind.skyblock.misc;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * Created by Matt on 2017-02-12.
 */
public class LocationUtil {

    public static Location deserialize(String serialized) {
        String[] split = serialized.split(",");

        World world = Bukkit.getWorld(split[0]);
        double x = Double.valueOf(split[1]);
        double y = Double.valueOf(split[2]);
        double z = Double.valueOf(split[3]);

        return new Location(world, x, y, z);
    }

    public static String serialize(Location loc) {
        return loc.getWorld().getName().trim() + "," + loc.getBlockX() + "," + loc.getBlockY() + "," + loc.getBlockZ();
    }
}
