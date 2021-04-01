package net.astrocube.puppets;

import org.bukkit.entity.Player;

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
     * @return id of the entity related with the puppet.
     */
    int getEntity();

}
