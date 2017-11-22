package net.skygrind.skyblock.region;

import org.bukkit.Location;

/**
 * Created by Matt on 2017-02-11.
 */
public class Region {

    private String name;
    private Location min, max;

    public Region(String name, Location min, Location max) {
        this.name = name;
        this.min = min;
        this.max = max;
    }

    public String getName() {
        return name;
    }

    public Location getMin() {
        return min;
    }

    public Location getMax() {
        return max;
    }
}
