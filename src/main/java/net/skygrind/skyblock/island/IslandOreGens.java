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

            double cobble = plugin.getConfig().getDouble("oregen.cobble");
            double coal = plugin.getConfig().getDouble("oregen.coal");
            double iron = plugin.getConfig().getDouble("oregen.iron");
            double diamond = plugin.getConfig().getDouble("oregen.diamond");
            double lapis = plugin.getConfig().getDouble("oregen.lapis");
            double redstone = plugin.getConfig().getDouble("oregen.redstone");
            double gold = plugin.getConfig().getDouble("oregen.gold");
            double emerald = plugin.getConfig().getDouble("oregen.emerald");


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
