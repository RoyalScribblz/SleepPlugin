package me.jameslloyd.sleepplugin.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.HashSet;
import java.util.UUID;

public class PlayerBedListener implements Listener {
    HashSet<UUID> playersInBed = new HashSet<>();

    @EventHandler
    public void onPlayerBedEnter(PlayerBedEnterEvent event) {
        Player player = event.getPlayer();
        playersInBed.add(player.getUniqueId());
        int onlinePlayerCount = Bukkit.getOnlinePlayers().size();

        if ((double) playersInBed.size() / onlinePlayerCount >= 1.0 / 3.0) {
            player.getWorld().setTime(1000);
        }
    }

    @EventHandler
    public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
        playersInBed.remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        playersInBed.remove(event.getPlayer().getUniqueId());
    }
}
