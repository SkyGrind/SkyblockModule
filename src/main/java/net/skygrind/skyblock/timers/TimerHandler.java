package net.skygrind.skyblock.timers;

import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class TimerHandler
{
    private final Map<UUID, List<Timer>> timers;

    public TimerHandler()
    {
        this.timers = new ConcurrentHashMap<>();
    }

    public List<Timer> getPlayerTimers(Player player)
    {
        return this.timers.get(player.getUniqueId());
    }

    public boolean hasTimer(Player player, TimerType timerType)
    {
        return timers.get(player.getUniqueId()) != null && this.timers.get(player.getUniqueId()).stream().filter(timer -> timer.getTimerType() == timerType).anyMatch(timer -> timer.getTime() > 0L);
    }

    public void addTimer(Player player, Timer defaultTimer)
    {
        if (timers.get(player.getUniqueId()) == null)
        {
            List<Timer> timersList = new LinkedList<>();
            timersList.add(defaultTimer);
            timers.put(player.getUniqueId(), timersList);
            return;
        }
        List<Timer> timersList = this.timers.get(player.getUniqueId());
        timersList.add(defaultTimer);
        timers.put(player.getUniqueId(), timersList);
    }

    public boolean hasActiveTimers(Player player)
    {
        return !this.timers.containsKey(player.getUniqueId()) && this.timers.get(player.getUniqueId()).stream().anyMatch(timer -> timer.getTime() > 0);

    }

    public Timer getTimer(Player player, TimerType timerType)
    {
        if (timers.get(player.getUniqueId()) == null)
            return null;

        for(Timer timer : this.timers.get(player.getUniqueId()))
        {
            if(timer.getTimerType() == timerType)
            {
                if(timer.getTime() > 0L)
                    return timer;
            }
        }

        return null;
    }

    public Map<UUID, List<Timer>> getTimers()
    {
        return this.timers;
    }
}
