package net.skygrind.skyblock.island.listeners.isles;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.island.Island;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class IslesRaidListener implements Listener {
    @EventHandler
    public void onVehicleMove(final VehicleMoveEvent event) {
        if (!event.getVehicle().getType().equals(EntityType.BOAT))
            return;

        if ((event.getVehicle().getPassenger() instanceof Player))
            return;


        Player player = (Player) event.getVehicle().getPassenger();

        Island toIsland = SkyBlock.getPlugin().getIslandRegistry().getIslandAt(event.getTo());
        if (toIsland == null)
            return;

        if (toIsland.getIslandLevel() < 5) {
            if (!toIsland.getMembers().contains(player.getUniqueId()) || !toIsland.getOwner().equals(player.getUniqueId()))
                return;

            event.getVehicle().teleport(event.getFrom());
            player.sendMessage(ChatColor.RED + "You cannot raid this island until it's level 5; try come back later.");
            return;
        }
    }
}
