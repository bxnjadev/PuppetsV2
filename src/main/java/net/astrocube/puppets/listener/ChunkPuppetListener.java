package net.astrocube.puppets.listener;

import net.astrocube.puppets.entity.PuppetEntity;
import net.astrocube.puppets.entity.PuppetRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;

public class ChunkPuppetListener implements Listener {

    private final PuppetRegistry puppetRegistry;

    public ChunkPuppetListener(PuppetRegistry puppetRegistry) {
        this.puppetRegistry = puppetRegistry;
    }

    @EventHandler
    public void onChunkUnload(ChunkUnloadEvent event) {

        Chunk chunk = event.getChunk();

        for (PuppetEntity entity : puppetRegistry.getRegistry()) {

            if (isInDifferentChunk(chunk, entity)) {
                continue;
            }

            entity.getViewers().forEach(viewer -> {

                Player player = Bukkit.getPlayer(viewer);

                if (player != null) {
                    entity.autoHide(player);
                    entity.hide(player);
                }

            });


        }

    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {

        Chunk chunk = event.getChunk();

        for (PuppetEntity entity : puppetRegistry.getRegistry()) {

            if (isInDifferentChunk(chunk, entity)) {
                continue;
            }

            entity.getViewers().forEach(viewer -> {

                Player player = Bukkit.getPlayer(viewer);

                if (player != null && entity.isInRange(player) && entity.isAutoHidden(player)) {

                    entity.removeAutoHide(player);

                    if (!entity.isRendered(player)) {
                        entity.show(player);
                    }

                }

            });

        }

    }

    private boolean isInDifferentChunk(Chunk chunk, PuppetEntity entity) {

        World world = Bukkit.getWorld(entity.getLocation().getWorld());

        if (world == null) {
            return true;
        }

        Location location = new Location(
                world,
                entity.getLocation().getX(),
                entity.getLocation().getY(),
                entity.getLocation().getZ()
        );

        return !compareChunkEquality(location, chunk);
    }



    private static int getChunkCoordinate(int coordinate) {
        return coordinate >> 4;
    }

    private static boolean compareChunkEquality(Location loc, Chunk chunk) {
        return getChunkCoordinate(loc.getBlockX()) == chunk.getX()
                && getChunkCoordinate(loc.getBlockZ()) == chunk.getZ();
    }

}
