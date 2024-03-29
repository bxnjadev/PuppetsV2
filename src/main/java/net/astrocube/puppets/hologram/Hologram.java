package net.astrocube.puppets.hologram;

import net.astrocube.puppets.location.Location;
import org.bukkit.entity.Player;

import java.util.List;

public interface Hologram {

    /**
     * @return player who is linked to the hologram.
     */
    Player getLinked();

    /**
     * @return base location of the hologram.
     */
    Location getLocation();

    /**
     *
     * @param place the place the line of set
     * @param line the content line
     */
    
    void setLine(int place, String line);

    /**
     * @return ordered list of {@link HologramLine}s shown.
     */
    List<HologramLine> getLines();

    /**
     * @return the raw lines
     */
    List<String> getRawLines();

    /**
     * @return if hologram is hidden
     */
    boolean isHidden();

    /**
     * Show hologram lines for the linked player.
     */
    void show();

    /**
     * Hide hologram lines for the linked player.
     */
    void hide();

}
