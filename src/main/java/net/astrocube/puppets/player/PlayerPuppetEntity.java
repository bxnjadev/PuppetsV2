package net.astrocube.puppets.player;

import net.astrocube.puppets.entity.PuppetEntity;
import org.bukkit.entity.Player;

import java.util.List;

public interface PlayerPuppetEntity extends PuppetEntity {

    /**
     * Set holograms on top of the {@link PuppetEntity} location.
     * @param player owner of the holograms.
     * @param lines to create.
     */
    void setHolograms(Player player, List<String> lines);

    /**
     * @param player to check
     * @return if player has linked holograms.
     */
    boolean hasLinkedHolograms(Player player);

    /**
     * Removes player linked holograms.
     * @param player owner of the holograms.
     */
    void removeHolograms(Player player);

}
