package net.astrocube.puppets.packet;

import net.astrocube.puppets.Reflection;
import net.astrocube.puppets.entity.ClickAction;
import net.astrocube.puppets.entity.PuppetEntity;
import net.astrocube.puppets.entity.PuppetRegistry;
import net.minecraft.server.v1_8_R3.PacketPlayInUseEntity;
import net.seocraft.lib.netty.channel.ChannelDuplexHandler;
import net.seocraft.lib.netty.channel.ChannelHandlerContext;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PuppetClickPacket implements PacketHandler {

    private final PuppetRegistry registry;
    private final Reflection.FieldAccessor<Integer> entityIdField =
            Reflection.getField(PacketPlayInUseEntity.class, "a", int.class);
    private final Reflection.FieldAccessor<?> actionField =
            Reflection.getField(PacketPlayInUseEntity.class, "action", Object.class);

    public PuppetClickPacket(PuppetRegistry registry) {
        this.registry = registry;
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
                                if (puppet.isViewing(player) && puppet.getEntity().getId() == packetEntityId) {

                                    ClickAction.Type action = actionField.get(packetObject).toString().equals("ATTACK") ?
                                            ClickAction.Type.LEFT : ClickAction.Type.RIGHT;

                                    if (action == puppet.getClickAction().getType()) {
                                        puppet.getClickAction().getAction().accept(player);
                                    }

                                    break;
                                }
                            }

                        }

                        try {
                            super.channelRead(ctx, packetObject);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                });
    }

}
