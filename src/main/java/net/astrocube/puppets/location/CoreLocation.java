package net.astrocube.puppets.location;

import org.bukkit.Bukkit;

public class CoreLocation implements Location {

    private final double x;
    private final double y;
    private final double z;
    private final int yaw;
    private final int pitch;
    private final String world;

    public CoreLocation(double x, double y, double z, int yaw, int pitch, String world) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
        this.world = world;

        if (Bukkit.getWorld(world) == null) {
            throw new IllegalArgumentException("World location not found");
        }

    }

    @Override
    public double getX() {
        return x;
    }

    @Override
    public double getY() {
        return y;
    }

    @Override
    public double getZ() {
        return z;
    }

    @Override
    public int getYaw() {
        return yaw;
    }

    @Override
    public int getPitch() {
        return pitch;
    }

    @Override
    public String getWorld() {
        return world;
    }

}
