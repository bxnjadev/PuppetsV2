package net.astrocube.puppets.entity;

import net.astrocube.puppets.location.Location;
import net.minecraft.server.v1_8_R3.Entity;
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
        this.viewers = new HashSet<>();
        this.UUID = UUID;
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
}
