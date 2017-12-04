package net.skygrind.skyblock.region;

import net.skygrind.skyblock.goose.GooseLocation;
import org.bukkit.Location;

/**
 * Created by Matt on 2017-02-11.
 */
public class Region {

    private String name;
    private GooseLocation min;
    private GooseLocation max;

    public Region(String name, Location min, Location max) {
        this.name = name;
        this.min = GooseLocation.fromLocation(min);
        this.max = GooseLocation.fromLocation(max);
    }

    public String getName() {
        return name;
    }

    public Location getMin() {
        return min.toLocation();
    }

    public Location getMax() {
        return max.toLocation();
    }
}
