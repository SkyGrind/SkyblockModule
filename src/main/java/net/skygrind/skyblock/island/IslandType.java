package net.skygrind.skyblock.island;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.bukkit.ChatColor;

/**
 * Created by Matt on 2017-02-11.
 */
@RequiredArgsConstructor
public enum IslandType {
    DEFAULT("Default", ChatColor.GREEN + ChatColor.BOLD.toString() + "Default", "skyblock.island.default", new String[] {ChatColor.GOLD + "A basic Skyblock island."}, 80),
    MODERN_HOUSE("Modern House", ChatColor.YELLOW + ChatColor.BOLD.toString() + "Modern House", "skyblock.island.modernhouse", new String[] {ChatColor.GOLD + "Modern house Skyblock island"}, 80),
    END_SHIP("End Ship", ChatColor.BLUE + ChatColor.BOLD.toString() + "End Ship", "skyblock.island.endship", new String[] {ChatColor.GOLD + "End ship Skyblock island"}, 80),
    COBBLE_THRONE("Cobble Throne", ChatColor.GRAY + ChatColor.BOLD.toString() + "Cobble Throne", "skyblock.island.cobblethrone", new String[] {ChatColor.GOLD + "Cobble throne Skyblock island"}, 80),
    PURPLE_LAVA("Purple Lava", ChatColor.DARK_PURPLE + ChatColor.BOLD.toString() + "Purple Lava", "skyblock.island.purplelava", new String[] {ChatColor.GOLD + "Purple lava Skyblock island"}, 80),
    DESERT("Desert", ChatColor.DARK_BLUE + ChatColor.BOLD.toString() + "Desert", "skyblock.island.desert", new String[] {ChatColor.GOLD + "Desert Skyblock island"}, 80);


    private final String raw, display, permRequired;
    private final String[] lore;
    private final int size;

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
}
