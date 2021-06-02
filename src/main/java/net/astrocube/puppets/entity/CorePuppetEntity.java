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
    private final Set<UUID> viewers;
    private final Set<UUID> autoHidden;
    private final Set<UUID> rendered;
    private boolean visibleAll;
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
        this.visibleAll = false;
        this.UUID = UUID;
    }

    @Override
    public boolean isRendered(Player player) {
        return rendered.contains(player.getUniqueId());
    }

    @Override
    public void show(Player player) {
        this.rendered.add(player.getUniqueId());
    }

    @Override
    public void hide(Player player) {
        this.rendered.remove(player.getUniqueId());
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
        return viewers.contains(player.getUniqueId());
    }

    @Override
    public void register(Player player) {
        viewers.add(player.getUniqueId());
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
        viewers.remove(player.getUniqueId());
    }

    @Override
    public Set<UUID> getViewers() {
        return viewers;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void showAll() {
        visibleAll = true;
        Bukkit.getOnlinePlayers().forEach(this::show);
    }

    @Override
    public void hideAll() {
        visibleAll = false;
        Bukkit.getOnlinePlayers().forEach(this::hide);
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
        autoHidden.add(player.getUniqueId());
    }

    @Override
    public boolean isAutoHidden(Player player) {
        return autoHidden.contains(player.getUniqueId());
    }

    @Override
    public void removeAutoHide(Player player) {
        autoHidden.remove(player.getUniqueId());
    }

    public static double square(double val) {
        return val * val;
    }

}
