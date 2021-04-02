package net.astrocube.puppets.entity;

import net.astrocube.puppets.location.Location;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public abstract class CorePuppetEntity implements PuppetEntity {

    private final Plugin plugin;
    private final Location location;
    private final int followingEntity;
    private final ClickAction action;
    private final Set<String> viewers;
    private final Set<String> autoHidden;
    private final Set<String> rendered;
    private final Entity entity;
    private final UUID UUID;

    public CorePuppetEntity(
            Plugin plugin,
            UUID UUID,
            ClickAction action,
            Location location,
            Entity entity,
            int followingEntity
    ) {
        this.plugin = plugin;
        this.location = location;
        this.entity = entity;
        this.action = action;
        this.followingEntity = followingEntity;
        this.autoHidden = new HashSet<>();
        this.viewers = new HashSet<>();
        this.rendered = new HashSet<>();
        this.UUID = UUID;
    }

    @Override
    public boolean isRendered(Player player) {
        return rendered.contains(player.getDatabaseIdentifier());
    }

    @Override
    public void show(Player player) {
        this.rendered.add(player.getDatabaseIdentifier());
    }

    @Override
    public void hide(Player player) {
        this.rendered.remove(player.getDatabaseIdentifier());
    }

    @Override
    public Location getLocation() {
        return this.location;
    }

    @Override
    public int getFollowingEntity() {
        return followingEntity;
    }

    @Override
    public boolean isViewing(Player player) {
        return viewers.contains(player.getDatabaseIdentifier());
    }

    @Override
    public void register(Player player) {
        viewers.add(player.getDatabaseIdentifier());
    }

    @Override
    public boolean isInRange(Player player) {

        if (player == null) {
            return false;
        }

        if (!player.getWorld().getName().equalsIgnoreCase(location.getWorld())) {
            return false;
        }

        World world = Bukkit.getWorld(getLocation().getWorld());

        if (world == null) {
            return false;
        }

        double hideDistance = 50.0;
        double distanceSquared = player.getLocation().distanceSquared(
                new org.bukkit.Location(
                        world,
                        getLocation().getX(),
                        getLocation().getY(),
                        getLocation().getZ()
                )
        );

        double bukkitRange = Bukkit.getViewDistance() << 4;

        return distanceSquared <= square(hideDistance) && distanceSquared <= square(bukkitRange);
    }

    @Override
    public void unregister(Player player) {
        viewers.remove(player.getDatabaseIdentifier());
    }

    @Override
    public Set<String> getViewers() {
        return viewers;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public UUID getUUID() {
        return UUID;
    }

    @Override
    public ClickAction getClickAction() {
        return action;
    }

    @Override
    public void autoHide(Player player) {
        autoHidden.add(player.getDatabaseIdentifier());
    }

    @Override
    public boolean isAutoHidden(Player player) {
        return autoHidden.contains(player.getDatabaseIdentifier());
    }

    @Override
    public void removeAutoHide(Player player) {
        autoHidden.remove(player.getDatabaseIdentifier());
    }


    public static double square(double val) {
        return val * val;
    }

}
