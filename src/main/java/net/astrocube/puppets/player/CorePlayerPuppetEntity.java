package net.astrocube.puppets.player;

import net.astrocube.puppets.entity.ClickAction;
import net.astrocube.puppets.entity.CorePuppetEntity;
import net.astrocube.puppets.hologram.CoreHologram;
import net.astrocube.puppets.hologram.Hologram;
import net.astrocube.puppets.location.Location;
import net.astrocube.puppets.player.skin.PuppetSkin;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class CorePlayerPuppetEntity extends CorePuppetEntity implements PlayerPuppetEntity {

    private final PuppetSkin skin;
    private final Map<String, Hologram> linkedHolograms;

    CorePlayerPuppetEntity(
            Plugin plugin,
            Location location,
            ClickAction action,
            Entity entity,
            PuppetSkin skin,
            int followingEntity
    ) {

        super(plugin, UUID.randomUUID(), action, location, entity, followingEntity);

        this.skin = skin;

        entity.setLocation(
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getYaw(),
                location.getPitch()
        );

        this.linkedHolograms = new HashMap<>();

    }

    @Override
    public void register(Player player) {

        super.register(player);

        PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;

        World world = Bukkit.getWorld(getLocation().getWorld());

        if (world == null) {
            throw new UnsupportedOperationException("Registered world is null");
        }

        if (!(getEntity() instanceof EntityPlayer)) {
            throw new IllegalArgumentException("Obtained entity is not a player");
        }

        EntityPlayer playerEntity = (EntityPlayer) getEntity();

        PacketUtil.createRegisterTeam(connection, getEntity().getName());

        PacketUtil.sendConnection(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, connection, playerEntity);

        PacketUtil.sendNamedSpawn(connection, playerEntity);
        PacketUtil.sendFixedHeadPacket(connection, playerEntity);

        Bukkit.getScheduler().runTaskLater(getPlugin(), () -> PacketUtil.sendConnection(
                PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER,
                connection,
                playerEntity
        ), 5 * 20L);

    }

    @Override
    public void unregister(Player player) {
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(getEntity().getId()));
    }

    @Override
    public void clear() {
        Bukkit.getOnlinePlayers().forEach(this::unregister);
    }

    @Override
    public ClickAction getClickAction() {
        return null;
    }

    @Override
    public void setHolograms(Player player, List<String> lines) {
        removeHolograms(player);
        Collections.reverse(lines);
        Hologram hologram = new CoreHologram(player, getLocation(), lines);
        linkedHolograms.put(player.getDatabaseIdentifier(), hologram);
        hologram.show();
    }

    @Override
    public boolean hasLinkedHolograms(Player player) {
        return linkedHolograms.containsKey(player.getDatabaseIdentifier());
    }

    @Override
    public void removeHolograms(Player player) {
        if (hasLinkedHolograms(player)) {
            Hologram hologram = linkedHolograms.get(player.getDatabaseIdentifier());
            hologram.hide();
            linkedHolograms.remove(player.getDatabaseIdentifier());
        }
    }

}
