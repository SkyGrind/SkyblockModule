package net.skygrind.skyblock.player.listener;

import com.skygrind.api.API;
import com.skygrind.api.framework.user.User;
import com.skygrind.api.framework.user.profile.Profile;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.ArrayList;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent event) {
        Player player = event.getPlayer();
        User user = API.getUserManager().findByUniqueId(player.getUniqueId());
        Profile profile = user.getProfile("skyblock");
        if (profile == null) {
            profile = new Profile("skyblock");
            profile.set("achievements", new ArrayList<String>());
            user.getAllProfiles().add(profile);
        }
    }
}
