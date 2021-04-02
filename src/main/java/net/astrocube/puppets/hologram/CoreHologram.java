package net.astrocube.puppets.hologram;

import net.astrocube.puppets.location.Location;
import net.minecraft.server.v1_8_R3.EntityArmorStand;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CoreHologram implements Hologram {

    private final Player player;
    private final Location location;
    private final List<String> rawLines;
    private final List<HologramLine> lines;
    private boolean hidden;

    public CoreHologram(Player player, Location location, List<String> lines) {
        this.player = player;
        this.location = location;
        this.rawLines = lines;
        this.lines = new ArrayList<>();
        this.hidden = true;
    }

    @Override
    public Player getLinked() {
        return player;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public List<HologramLine> getLines() {
        return lines;
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    @Override
    public void show() {

        for (int i = 0; i < rawLines.size(); i++) {

            World world = Bukkit.getWorld(location.getWorld());

            if (world == null) {
                throw new IllegalArgumentException("World not found");
            }

            EntityArmorStand stand = new EntityArmorStand(((CraftWorld) world).getHandle());
            stand.setPosition(
                    getLocation().getX(),
                    getLocation().getY() + (i * 0.25),
                    getLocation().getZ()
            );


            stand.setInvisible(true);
            stand.setCustomName(rawLines.get(i));
            stand.setCustomNameVisible(!rawLines.get(i).isEmpty());

            lines.add(
                    new CoreHologramLine(
                            rawLines.get(i),
                            stand.getId()
                    )
            );

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(stand));

            this.hidden = false;

        }

    }

    @Override
    public void hide() {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        lines.forEach(line -> connection.sendPacket(new PacketPlayOutEntityDestroy(line.getEntity())));
        lines.clear();
        this.hidden = true;
    }



}
