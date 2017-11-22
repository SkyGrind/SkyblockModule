package net.skygrind.skyblock.misc;

import net.skygrind.skyblock.SkyBlock;

/**
 * Created by Matt on 2017-02-10.
 */
public class Logger {

    public static void info(String msg) {
        SkyBlock.getPlugin().getLogger().info(msg);
    }
}
