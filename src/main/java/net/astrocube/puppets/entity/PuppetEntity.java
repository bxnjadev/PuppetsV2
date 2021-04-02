package net.astrocube.puppets.entity;

import net.astrocube.puppets.location.Location;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;

public interface PuppetEntity {

    /**
     * @return actual puppet location
     */
    Location getLocation();

    /**
     * @return entity which should be looking
     * the puppet. (If -1 will see the {@link Location}.
     */
    int getFollowingEntity();

    /**
     * @param player to check
     * @return if player is viewing the entity.
     */
    boolean isViewing(Player player);

    /**
     * Register player in order to make Puppet visible
     * @param player who will be capable to view the puppet.
     */
    void register(Player player);

    /**
     * Register player in order to make Puppet invisible
     * @param player who will be unregistered from puppet view.
     */
    void unregister(Player player);

    /**
     * Clear every player visualization.
     */
    void clear();

    /**
     * @return action when entity clicked.
     */
    ClickAction getClickAction();

    /**
     * @return id of the entity related with the puppet.
     */
    Entity getEntity();

    /**
     * @return registered plugin.
     */
    Plugin getPlugin();

    /**
     * @return unique identifier for puppet.
     */
    UUID getUUID();

}
