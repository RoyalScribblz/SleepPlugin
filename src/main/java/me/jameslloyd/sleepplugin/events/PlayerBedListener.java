package me.jameslloyd.sleepplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public class PlayerBedListener implements Listener {
    private final HashSet<UUID> playersInBed = new HashSet<>();
    private final JavaPlugin plugin;

    public PlayerBedListener(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.isSleeping()) {
                    playersInBed.add(playerId);
                    checkSleepConditions(player.getWorld());
                }
            }
        }.runTaskLater(plugin, 60L); // 60 ticks = 3 seconds
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        playersInBed.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playersInBed.remove(event.getPlayer().getUniqueId());
    }

    private void checkSleepConditions(World world) {
        int onlinePlayerCount = Bukkit.getOnlinePlayers().size();

        if (onlinePlayerCount > 0 && (double) playersInBed.size() / onlinePlayerCount >= 1.0 / 3.0) {
            if (world.hasStorm()) {
                world.setStorm(false);
                world.setThundering(false);
            }
            world.setTime(1000);

            List<Player> players = world.getPlayers();
            for (Player player : players) {
                if (playersInBed.contains(player.getUniqueId())) {
                    player.setStatistic(Statistic.TIME_SINCE_REST, 0);
                }
            }
        }
    }
}
