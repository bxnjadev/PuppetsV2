package net.astrocube.puppets.player;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.astrocube.puppets.entity.ClickAction;
import net.astrocube.puppets.entity.CoreClickAction;
import net.astrocube.puppets.entity.PuppetRegistry;
import net.astrocube.puppets.location.Location;
import net.astrocube.puppets.player.skin.CorePuppetSkin;
import net.astrocube.puppets.player.skin.PuppetSkin;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.UUID;
import java.util.function.Consumer;

public class PlayerPuppetEntityBuilder {

    private final Plugin plugin;
    private final PuppetRegistry registry;
    private Location location;
    private int followingEntity;
    private PuppetSkin skin;
    private ClickAction.Type clickType = ClickAction.Type.LEFT;
    private Consumer<Player> action = (p) -> {};

    public static PlayerPuppetEntityBuilder create(Location location, Plugin plugin, PuppetRegistry registry) {
        return new PlayerPuppetEntityBuilder(location, plugin, registry);
    }

    private PlayerPuppetEntityBuilder(Location location, Plugin plugin, PuppetRegistry registry) {
        this.location = location;
        this.skin = new CorePuppetSkin("", "");
        this.followingEntity = -1;
        this.registry = registry;
        this.plugin = plugin;
    }

    public PlayerPuppetEntityBuilder setLocation(Location location) {
        this.location = location;
        return this;
    }

    public PlayerPuppetEntityBuilder setClickType(ClickAction.Type clickType) {
        this.clickType = clickType;
        return this;
    }

    public PlayerPuppetEntityBuilder setAction(Consumer<Player> action) {
        this.action = action;
        return this;
    }

    public PlayerPuppetEntityBuilder setSkin(PuppetSkin skin) {
        this.skin = skin;
        return this;
    }

    public PlayerPuppetEntityBuilder setFollowingEntity(int followingEntity) {
        this.followingEntity = followingEntity;
        return this;
    }

    public PlayerPuppetEntity build() {

        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

        World world = Bukkit.getWorld(location.getWorld());

        if (world == null) {
            throw new IllegalArgumentException("World not found");
        }

        WorldServer worldHandle = ((CraftWorld) world).getHandle();

        GameProfile profile = new GameProfile(UUID.randomUUID(), RandomStringUtils.random(16, "0123456789abcdef"));

        profile.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));

        EntityPlayer npc = new EntityPlayer(
                server,
                worldHandle,
                profile,
                new PlayerInteractManager(worldHandle)
        );

        PlayerPuppetEntity puppetEntity = new CorePlayerPuppetEntity(
                plugin,
                location,
                new CoreClickAction(clickType, action),
                npc,
                skin,
                followingEntity
        );

        registry.register(puppetEntity);

        return puppetEntity;

    }

}
