package net.skygrind.skyblock.region;

import org.bukkit.Location;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 2017-02-11.
 */
public class RegionHandler {

    private List<Region> regions = new ArrayList<>();
    private File regionFile;


    public void init() {
        //TODO load from regions.yml
    }

    public Region createRegion(String name, Location min, Location max) {
        if (isRegion(name)) {
            return null;
        }

        Region region = new Region(name, min, max);
        this.regions.add(region);
        return region;
    }

    public boolean isRegion(String name) {
        for (Region region : regions) {
            if (region.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public void deleteRegion(Region region) {
        this.regions.remove(region);
    }
}
