package net.skygrind.skyblock.player;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import net.skygrind.core.Core;
import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.missions.Mission;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Matt on 2017-02-12.
 */
public class PlayerRegistry implements Listener {

    private List<SkyPlayer> players = new ArrayList<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player pl = e.getPlayer();

        BasicDBObject find = new BasicDBObject("_id", pl.getUniqueId());
        DBObject toUse = null;

        DBCursor cursor = Core.getInstance().getMongo().getCollection("players").find(find);

        DBObject found = cursor.one();

        if (found == null) {
            toUse = find.append("missions", serializeMissions());
        }
        else {
            toUse = find;
        }
        System.out.println(toUse == null);

        SkyPlayer player = new SkyPlayer(pl, toUse);
        this.players.add(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player pl = e.getPlayer();
        SkyPlayer player = getPlayer(pl.getName());

        this.players.remove(player);
    }

    public SkyPlayer getPlayer(String name) {
        for (SkyPlayer player : players) {
            if (player.getBukkitPlayer().getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    private String serializeMissions() {
        String serialize = "";
        for (Mission entry : SkyBlock.getPlugin().getMissionRegistry().getMissions()) {
            serialize= serialize+entry.getName() + ":" + "false,";
        }
        return serialize;
    }
}
