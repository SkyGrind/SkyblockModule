package net.skygrind.skyblock.island;

import net.skygrind.skyblock.SkyBlock;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockFromToEvent;

import java.util.Random;

/**
 * Created by Matt on 25/04/2017.
 */
public class IslandOreGens implements Listener {

    private SkyBlock plugin = SkyBlock.getPlugin();

    @EventHandler
    public void onChange(BlockFromToEvent event) {
        Block before = event.getBlock();
        int id = before.getTypeId();
        Block to = event.getToBlock();

        if (id >= 8 && id <= 11) {

            //We're fucking with water here

            Random random = new Random();

            if (!before.getLocation().getWorld().getName().equalsIgnoreCase("SkyBlock")) {
                return;
            }

            int chance = 0;

            double cobble = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.COBBLESTONE);
            double coal = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.COAL_ORE);
            double iron = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.IRON_ORE);
            double diamond = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.DIAMOND_ORE);
            double lapis = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.LAPIS_ORE);
            double redstone = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.REDSTONE_ORE);
            double gold = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.GOLD_ORE);
            double emerald = SkyBlock.getPlugin().getOreGenerationConfig().getValue(Material.EMERALD_ORE);

            chance = chance + random.nextInt(100);

            if (chance > 0 && chance <= emerald) {
                to.setType(Material.EMERALD_ORE);
            }
            if (chance > emerald && chance <= diamond) {
                to.setType(Material.DIAMOND_ORE);
            }
            if (chance > diamond && chance <= gold) {
                to.setType(Material.GOLD_ORE);
            }
            if (chance > gold && chance <= iron) {
                to.setType(Material.IRON_ORE);
            }
            if (chance > iron && chance <= redstone) {
                to.setType(Material.REDSTONE_ORE);
            }
            if (chance > redstone && chance <= coal) {
                to.setType(Material.COAL_ORE);
            }
            if (chance > coal && chance <= lapis) {
                to.setType(Material.LAPIS_ORE);
            }
            if (chance > lapis && chance <= 100) {
                to.setType(Material.COBBLESTONE);
            }
        }
    }
}
