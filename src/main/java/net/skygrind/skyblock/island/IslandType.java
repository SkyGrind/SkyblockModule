package net.skygrind.skyblock.island;

import net.skygrind.skyblock.configuration.ServerType;
import net.skygrind.skyblock.goose.GooseLocation;
import org.bukkit.ChatColor;
import org.bukkit.Location;

/**
 * Created by Matt on 2017-02-11.
 */
public enum IslandType {
    DEFAULT("Default", ChatColor.GREEN + ChatColor.BOLD.toString() + "Default", "skyblock.island.default", new String[]{ChatColor.GOLD + "A basic Skyblock island."}, 80, ServerType.BOTH),
    MODERN_HOUSE("Modern House", ChatColor.YELLOW + ChatColor.BOLD.toString() + "Modern House", "skyblock.island.modernhouse", new String[]{ChatColor.GOLD + "Modern house Skyblock island"}, 80, ServerType.SKY),
    END_SHIP("End Ship", ChatColor.BLUE + ChatColor.BOLD.toString() + "End Ship", "skyblock.island.endship", new String[]{ChatColor.GOLD + "End ship Skyblock island"}, 80, ServerType.SKY),
    COBBLE_THRONE("Cobble Throne", ChatColor.GRAY + ChatColor.BOLD.toString() + "Cobble Throne", "skyblock.island.cobblethrone", new String[]{ChatColor.GOLD + "Cobble throne Skyblock island"}, 80, ServerType.SKY),
    PURPLE_LAVA("Purple Lava", ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Purple Lava", "skyblock.island.purplelava", new String[]{ChatColor.GOLD + "Purple lava Skyblock island"}, 80, ServerType.SKY),
    DESERT("Desert", ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "Desert", "skyblock.island.desert", new String[]{ChatColor.GOLD + "Desert Skyblock island"}, 80, ServerType.SKY),
    CASTLE("Castle", ChatColor.GRAY + ChatColor.BOLD.toString() + "Castle", "skyblock.island.castle", new String[]{ChatColor.GOLD + "Castle Skyblock island"}, 90, ServerType.ISLES);


    private final String raw, display, permRequired;
    private final String[] lore;
    private final int size;
    private final ServerType serverType;

    IslandType(String raw, String display, String perm, String[] lore, int size, final ServerType type) {
        this.raw = raw;
        this.display = display;
        this.permRequired = perm;
        this.lore = lore;
        this.size = size;
        this.serverType = type;
    }

    public String getRaw() {
        return raw;
    }

    public String getDisplay() {
        return display;
    }

    public String getPermRequired() {
        return permRequired;
    }

    public int getSize() {
        return size;
    }

    public String[] getLore() {
        return lore;
    }

    public ServerType getServerType() {
        return serverType;
    }
}
