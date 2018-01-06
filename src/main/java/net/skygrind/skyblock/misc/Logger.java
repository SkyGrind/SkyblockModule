package net.skygrind.skyblock.misc;

import com.islesmc.modules.api.API;
import org.bukkit.plugin.Plugin;

/**
 * Created by Matt on 2017-02-10.
 */
public class Logger {
    
    // Test push for webhook

    public static void info(String msg) {
        ((Plugin) API.getPlugin()).getLogger().info(msg);
    }
}
