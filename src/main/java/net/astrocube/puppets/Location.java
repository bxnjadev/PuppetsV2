package net.astrocube.puppets;

import org.bukkit.World;

public interface Location {

    /**
     * @return x location.
     */
    double getX();

    /**
     * @return y location.
     */
    double getY();

    /**
     * @return z location.
     */
    double getZ();

    /**
     * @return actual yaw.
     */
    int getYaw();

    /**
     * @return actual pitch.
     */
    int getPitch();

    /**
     * @return registered Bukkit world.
     */
    World getWorld();

}
