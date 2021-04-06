package net.astrocube.puppets.hologram;

import net.astrocube.puppets.location.Location;
import net.minecraft.server.v1_8_R3.*;
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
    public void setLine(int place, String line) {

        place--;

        removeEntity(getLines().get(place));
        rawLines.set(place, line);

        HologramLine hologramLine = getLines().get(place);

        World world = Bukkit.getWorld(location.getWorld());

        EntityArmorStand stand = new EntityArmorStand(((CraftWorld) world).getHandle());
        stand.setPosition(getLocation().getX(), hologramLine.getY(), getLocation().getZ());

        getLines().set(place, new CoreHologramLine(line, stand.getId(), hologramLine.getY()));

        stand.setInvisible(true);
        stand.setCustomName(rawLines.get(place));
        stand.setCustomNameVisible(!rawLines.get(place).isEmpty());

        sendPacket(new PacketPlayOutSpawnEntityLiving(stand));

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
                            stand.getId(),
                            getLocation().getY() + (i * 0.25)
                    )
            );

            sendPacket(new PacketPlayOutSpawnEntityLiving(stand));

            this.hidden = false;

        }

    }

    @Override
    public void hide() {
        lines.forEach(this::removeEntity);
        lines.clear();
        this.hidden = true;
    }

    private void removeEntity(HologramLine hologramLine) {
        sendPacket(new PacketPlayOutEntityDestroy(hologramLine.getEntity()));
    }

    private void sendPacket(Packet<?> packet) {
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

}
