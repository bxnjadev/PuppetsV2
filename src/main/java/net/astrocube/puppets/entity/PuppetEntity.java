package net.astrocube.puppets.entity;

import net.astrocube.puppets.location.Location;
import net.minecraft.server.v1_8_R3.Entity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.Set;
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
     * Register player in order to keep track of which
     * players can see the puppet.
     *
     * @param player who will be capable to view the puppet.
     */
    void register(Player player);

    /**
     * Unregister player from list of who is available to
     * see the puppet.
     *
     * @param player who will be unregistered from puppet view.
     */
    void unregister(Player player);

    /**
     * Displays the puppet for a registered player.
     *
     * @param player where puppet will be shown.
     */
    void show(Player player);

    /**
     * All players show the npc
     */

    void showAll();

    /**
     * All players hide the npc
     */

    void hideAll();

    /**
     * @param player to check
     * @return if user is actually displaying.
     */
    boolean isRendered(Player player);

    /**
     * Hides the puppet for a registered player without
     * removing the record, so he can see it again.
     *
     * @param player where puppet will be hidden.
     */
    void hide(Player player);

    /**
     * @return all puppet viewers.
     */
    Set<UUID> getViewers();

    /**
     * Add player to auto hide list if an entity was forced
     * to disappear.
     * @param player to auto-hide.
     */
    void autoHide(Player player);

    /**
     * @param player to check
     * @return if player is auto-hidden.
     */
    boolean isAutoHidden(Player player);

    /**
     * Removes player from auto-hide.
     * @param player to remove
     */
    void removeAutoHide(Player player);

    /**
     * Check if player is in range, otherwise Puppet must be hidden.
     * @param player to check
     * @return if player is in viewable range.
     */
    boolean isInRange(Player player);

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
