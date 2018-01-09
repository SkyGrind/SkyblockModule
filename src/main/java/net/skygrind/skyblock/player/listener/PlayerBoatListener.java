package net.skygrind.skyblock.player.listener;

import com.islesmc.modules.api.config.ModuleConfig;
import com.islesmc.modules.api.module.ModuleManager;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.configuration.ServerType;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleMoveEvent;

public class PlayerBoatListener implements Listener {
    
    @EventHandler
    public void onBoatMove(VehicleMoveEvent event) {
        if (!(SkyBlock.getPlugin().getServerConfig().getServerType() == ServerType.ISLES)) return;
        
        Vehicle vehicle = event.getVehicle();
        
        if (!(vehicle.getPassenger() instanceof Player)) return;
        
        Player player = (Player) vehicle.getPassenger();
        
        if (!(vehicle instanceof Boat)) return;
        
        Boat boat = (Boat) vehicle;
        
        if (player.hasPermission("isles.boatspeed")) {
            boat.setMaxSpeed(200D);
        }
    }
}
