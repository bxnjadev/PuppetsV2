package net.astrocube.puppets.player;

import net.astrocube.puppets.Reflection;
import net.astrocube.puppets.entity.PuppetEntity;
import net.minecraft.server.v1_8_R3.*;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PacketUtil {

    /**
     * Send packet play out to modify {@link CorePlayerPuppetEntity}
     * @param action belonging to packet
     * @param connection where packet will be sent
     * @param playerEntity of the NPC to be shown
     */
    public static void sendConnection(
            PacketPlayOutPlayerInfo.EnumPlayerInfoAction action,
            PlayerConnection connection,
            EntityPlayer playerEntity
    ) {

        PacketPlayOutPlayerInfo packetPlayOutPlayerInfo = new PacketPlayOutPlayerInfo(
                action,
                playerEntity
        );

        PacketPlayOutPlayerInfo.PlayerInfoData playerInfoData =
                packetPlayOutPlayerInfo.new PlayerInfoData(
                        playerEntity.getProfile(),
                        1,
                        WorldSettings.EnumGamemode.NOT_SET,
                        IChatBaseComponent.ChatSerializer.a(
                                "{\"text\":\"[NPC] " + playerEntity.getProfile().getName() + "\",\"color\":\"dark_gray\"}"
                        )
                );

        Reflection.FieldAccessor<List> fieldAccessor = Reflection.getField(packetPlayOutPlayerInfo.getClass(), "b", List.class);
        fieldAccessor.set(packetPlayOutPlayerInfo, Collections.singletonList(playerInfoData));

        connection.sendPacket(packetPlayOutPlayerInfo);
    }

    /**
     * Creates a team to make invisible a {@link PuppetEntity}
     * @param connection where packet will be sent
     * @param name of the {@link PuppetEntity} to add.
     */
    public static void createRegisterTeam(
            PlayerConnection connection,
            String name
    ) {

        PacketPlayOutScoreboardTeam packetPlayOutScoreboardTeam = new PacketPlayOutScoreboardTeam();

        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "h", int.class)
                .set(packetPlayOutScoreboardTeam, 0);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "b", String.class)
                .set(packetPlayOutScoreboardTeam, name);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "a", String.class)
                .set(packetPlayOutScoreboardTeam, name);
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "e", String.class)
                .set(packetPlayOutScoreboardTeam, "never");
        Reflection.getField(packetPlayOutScoreboardTeam.getClass(), "i", int.class)
                .set(packetPlayOutScoreboardTeam, 1);
        Reflection.FieldAccessor<Collection> collectionFieldAccessor = Reflection.getField(
                packetPlayOutScoreboardTeam.getClass(), "g", Collection.class);
        collectionFieldAccessor.set(packetPlayOutScoreboardTeam, Collections.singletonList(name));

        connection.sendPacket(packetPlayOutScoreboardTeam);

    }

    /**
     * Spawn named entity belonging to {@link CorePlayerPuppetEntity}
     * @param connection of player to be displayed
     * @param playerEntity of the {@link CorePlayerPuppetEntity}
     */
    public static void sendNamedSpawn(PlayerConnection connection, EntityPlayer playerEntity) {
        connection.sendPacket(
                new PacketPlayOutNamedEntitySpawn(
                        playerEntity
                )
        );
    }

    /**
     * Fix of wrong head position
     * @param connection of the player to be fixed
     * @param playerEntity of the {@link CorePlayerPuppetEntity}
     */
    public static void sendFixedHeadPacket(PlayerConnection connection, EntityPlayer playerEntity) {
        connection.sendPacket(
                new PacketPlayOutEntityHeadRotation(
                        playerEntity,
                        (byte) (playerEntity.yaw * 256 / 360)
                )
        );
    }

}
