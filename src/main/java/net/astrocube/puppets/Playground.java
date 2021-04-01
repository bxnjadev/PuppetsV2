package net.astrocube.puppets;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.MinecraftServer;
import net.minecraft.server.v1_8_R3.PlayerInteractManager;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class Playground extends JavaPlugin {

    @Override
    public void onEnable() {


        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();

        WorldServer nmsWorld = ((CraftWorld) Bukkit.getWorld("world")).getHandle();

        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "playername");

        EntityPlayer npc = new EntityPlayer(server, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));

        npc.getId();

        npc.setLocation(
                0,
                0,
                0,
                0,
                0
        );

    }

}
