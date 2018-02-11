package net.skygrind.skyblock.player.listener;

import net.skygrind.skyblock.SkyBlock;
import net.skygrind.skyblock.timers.DefaultTimer;
import net.skygrind.skyblock.timers.Timer;
import net.skygrind.skyblock.timers.TimerType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.concurrent.TimeUnit;

public class PlayerDamageListener implements Listener {

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event)
    {
        if(event.isCancelled())
            return;

        if (!(event.getEntity() instanceof Player))
            return;

        Player damaged = (Player) event.getEntity();

        Timer timer = SkyBlock.getPlugin().getTimerHandler().getTimer(damaged, TimerType.COMBAT_TAG);
        if(timer != null && timer.getTime() > 0)
            timer.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            SkyBlock.getPlugin().getTimerHandler().addTimer(damaged, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damaged));

        if(!(event.getDamager() instanceof Player))
            return;

        Player damager = (Player) event.getDamager();

        Timer timer1 = SkyBlock.getPlugin().getTimerHandler().getTimer(damager, TimerType.COMBAT_TAG);
        if(timer1 != null && timer1.getTime() > 0)
            timer1.setTime(TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis());
        else
            SkyBlock.getPlugin().getTimerHandler().addTimer(damager, new DefaultTimer(TimerType.COMBAT_TAG, TimeUnit.SECONDS.toMillis(30L) + System.currentTimeMillis(), damager));
    }
}
