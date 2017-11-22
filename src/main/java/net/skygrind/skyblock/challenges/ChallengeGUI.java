package net.skygrind.skyblock.challenges;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by Matt on 23/08/2017.
 */
public class ChallengeGUI {
    private final SkyBlock plugin;

    public ChallengeGUI(SkyBlock plugin) {
        this.plugin = plugin;
    }


    public void generate(Player player) {
        Inventory inventory = Bukkit.createInventory(null, 9, "Challenges");


    }
}
