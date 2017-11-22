package net.skygrind.skyblock.goose;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.UUID;

/**
 * Owned by SethyCorp, and KueMedia respectively.
 **/
public class GooseLocation {
    private final UUID world;
    private final Double x;
    private final Double y;
    private final Double z;

    private GooseLocation(final UUID world, final Double x, final Double y, final Double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static GooseLocation fromLocation(final Location location) {
        return new GooseLocation(location.getWorld().getUID(), location.getX(), location.getY(), location.getZ());
    }

    public UUID getWorld() {
        return world;
    }

    public Double getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getZ() {
        return z;
    }

    public Location toLocation() {
        return new Location(Bukkit.getWorld(world), x, y, z);
    }
}
