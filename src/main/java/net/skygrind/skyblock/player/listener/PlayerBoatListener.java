package net.skygrind.skyblock.player.listener;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.configuration.ServerType;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class PlayerBoatListener implements Listener {
    
    @EventHandler
    public void onBoatMove(VehicleMoveEvent event) {
        if (!(SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES)) return;
        
        Vehicle vehicle = event.getVehicle();
        
        if (!(vehicle.getPassenger() instanceof Player)) return;
        
        Player player = (Player) vehicle.getPassenger();
        
        if (!(vehicle instanceof Boat)) return;
        
        Boat boat = (Boat) vehicle;

        double maxSpeed = boat.getMaxSpeed() * getSpeedMultiplier(player);

        if (player.hasPermission("isles.boatspeed")) {
            boat.setMaxSpeed(maxSpeed);
        }
    }


    private double getSpeedMultiplier(final Player player) {
        double multiplier = 1D;
        for (PermissionAttachmentInfo info : player.getEffectivePermissions()) {
            if (info.getPermission().startsWith("isles.boatspeed.")) {
                multiplier = Double.parseDouble(info.getPermission().replace("isles.boatspeed.", ""));
            }
        }
        return multiplier;
    }
}
