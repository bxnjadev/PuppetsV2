package net.astrocube.puppets.packet;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.astrocube.puppets.Reflection;
import net.astrocube.puppets.entity.ClickAction;
import net.astrocube.puppets.entity.PuppetEntity;
import net.astrocube.puppets.entity.PuppetRegistry;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.seocraft.lib.netty.channel.ChannelDuplexHandler;
import net.seocraft.lib.netty.channel.ChannelHandlerContext;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.logging.Level;

public class PuppetClickPacket implements PacketHandler {

    private final PuppetRegistry registry;
    private final Plugin plugin;
    private final Multimap<UUID, String> cooldown;
    private final Reflection.FieldAccessor<Integer> entityIdField =
            Reflection.getField(PacketPlayInUseEntity.class, "a", int.class);
    private final Reflection.FieldAccessor<?> actionField =
            Reflection.getField(PacketPlayInUseEntity.class, "action", Object.class);

    public PuppetClickPacket(PuppetRegistry registry, Plugin plugin) {
        this.registry = registry;
        this.plugin = plugin;
        this.cooldown = MultimapBuilder.hashKeys().arrayListValues().build();
    }

    @Override
    public void handle(Player player) {
        ((CraftPlayer) player).getHandle()
                .playerConnection
                .networkManager
                .channel
                .pipeline()
                .addBefore("packet_handler", "puppet_click", new ChannelDuplexHandler() {

                    @Override
                    public void channelRead(ChannelHandlerContext ctx, Object packetObject) {

                        if (packetObject instanceof PacketPlayInUseEntity) {

                            int packetEntityId = entityIdField.get(packetObject);

                            for (PuppetEntity puppet : registry.getRegistry()) {

                                if (cooldown.containsEntry(puppet.getUUID(), player.getDatabaseIdentifier())) {
                                    return;
                                }

                                if (puppet.isViewing(player) && puppet.getEntity().getId() == packetEntityId) {

                                    ClickAction.Type action = actionField.get(packetObject).toString().equals("ATTACK") ?
                                            ClickAction.Type.LEFT : ClickAction.Type.RIGHT;

                                    if (action == puppet.getClickAction().getType()) {

                                        puppet.getClickAction().getAction().accept(player);

                                        cooldown.put(puppet.getUUID(), player.getDatabaseIdentifier());

                                        Bukkit.getScheduler().runTaskLater(
                                                plugin,
                                                () -> cooldown.remove(puppet.getUUID(), player.getDatabaseIdentifier()),
                                                5L
                                        );

                                    }

                                    break;
                                }
                            }

                        }

                        try {
                            super.channelRead(ctx, packetObject);
                        } catch (Exception e) {
                            plugin.getLogger().log(Level.SEVERE, "There was an error reading puppet click packet", e);
                        }


                    }

                });
    }

}
