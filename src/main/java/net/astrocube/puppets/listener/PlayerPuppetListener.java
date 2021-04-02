package net.astrocube.puppets.listener;

import net.astrocube.puppets.entity.PuppetEntity;
import net.astrocube.puppets.entity.PuppetRegistry;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPuppetListener implements Listener {

    private final PuppetRegistry puppetRegistry;
    private final Plugin plugin;

    public PlayerPuppetListener(PuppetRegistry puppetRegistry, Plugin plugin) {
        this.puppetRegistry = puppetRegistry;
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        for (PuppetEntity entity : puppetRegistry.getRegistry()) {
            entity.unregister(event.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        for (PuppetEntity entity : puppetRegistry.getRegistry()) {
            if (entity.isViewing(player)) {
                entity.hide(player);
            }
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {

        Player player = event.getPlayer();
        Location location = event.getRespawnLocation();

        if (location.getWorld() != null && location.getWorld().equals(player.getWorld())) {
            new BukkitRunnable() {

                @Override
                public void run() {

                    if (!player.isOnline()) {
                        this.cancel();
                        return;
                    }

                    if (player.getLocation().equals(location)) {
                        handleMovement(player, location.getWorld());
                        this.cancel();
                    }

                }

            }.runTaskTimer(plugin, 0L, 1L);
        }


        for (PuppetEntity entity : puppetRegistry.getRegistry()) {
            if (entity.isViewing(player) && entity.isInRange(player)) {
                if (!entity.isRendered(player)) {
                    entity.show(player);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
        handleMovement(event.getPlayer(), event.getFrom());
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        handleMovement(event.getPlayer(), event.getPlayer().getWorld());
    }

    private void handleMovement(Player player, World world) {
        for (PuppetEntity entity : puppetRegistry.getRegistry()) {
            if (entity.isViewing(player)) {

                if (world == null) {

                    if (!entity.isRendered(player)) {
                        entity.hide(player);
                    }

                    return;
                }

                if (entity.getLocation().getWorld().equalsIgnoreCase(world.getName()) && entity.isInRange(player)) {
                    if (!entity.isRendered(player) && entity.isAutoHidden(player)) {
                        entity.removeAutoHide(player);
                        entity.show(player);
                    }
                } else {
                    if (entity.isRendered(player)) {
                        entity.autoHide(player);
                        entity.hide(player);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Location from = event.getFrom();
        Location to = event.getTo();

        // Only check movement when the player moves from one block to another. The event is called often
        // as it is also called when the pitch or yaw change. This is worth it from a performance view.
        if (to == null || from.getBlockX() != to.getBlockX()
                || from.getBlockY() != to.getBlockY()
                || from.getBlockZ() != to.getBlockZ())
            handleMovement(event.getPlayer(), to.getWorld());
    }

}
