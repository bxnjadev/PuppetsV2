package net.astrocube.puppets;

import net.astrocube.puppets.entity.ClickAction;
import net.astrocube.puppets.entity.CorePuppetRegistry;
import net.astrocube.puppets.entity.PuppetEntity;
import net.astrocube.puppets.entity.PuppetRegistry;
import net.astrocube.puppets.location.CoreLocation;
import net.astrocube.puppets.packet.PuppetClickPacket;
import net.astrocube.puppets.player.PlayerPuppetEntity;
import net.astrocube.puppets.player.PlayerPuppetEntityBuilder;
import net.astrocube.puppets.player.skin.CorePuppetSkin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public class Playground extends JavaPlugin implements Listener {

    private PuppetRegistry registry = new CorePuppetRegistry();

    @Override
    public void onEnable() {

        getServer().getPluginManager().registerEvents(this, this);

        PlayerPuppetEntityBuilder
                .create(
                        new CoreLocation(180, 76, 255, 0, 0, Bukkit.getWorlds().get(0).getName()),
                        this,
                        registry
                )
                .setClickType(ClickAction.Type.RIGHT)
                .setAction((p) -> p.sendMessage("Ola hermoso"))
                .setSkin(
                        new CorePuppetSkin(
                                "ewogICJ0aW1lc3RhbXAiIDogMTYxNzMwODMyODg3OSwKICAicHJvZmlsZUlkIiA6ICI1ZWE0ODg2NTg2OWI0Y2ZhOWRjNTg5YmFlZWQwNzM5MCIsCiAgInByb2ZpbGVOYW1lIiA6ICJfUllOMF8iLAogICJzaWduYXR1cmVSZXF1aXJlZCIgOiB0cnVlLAogICJ0ZXh0dXJlcyIgOiB7CiAgICAiU0tJTiIgOiB7CiAgICAgICJ1cmwiIDogImh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNDlmZWZiNmRlYzdlYmI0NWMwODA0NTUzNzJjNTkyYTAzN2I3N2ZkYzU2ZmZmMWU1ZDY3ZWI2OTg0NGQyMzBmYSIKICAgIH0KICB9Cn0=",
                                "HS0hO3M4JaqC5fP97wUHDMeTYpa3+wvxustD47/nimQNqro2pkmdJoFDsATe8sdQHoFDNXTOb7qRwWfVCz0DianM3X06EQKdQ9s4OZgv6+v59tj3jRdj+yHUPznKqIBCiBMbe8oEfLFHgOoOR5P8twztX7CrweqOWBj2Qcvg0kgz7uXirAqeCCkFF7W27Ycb3cp9DZfoDZk/S3YeZ8ZLlR8In5FKoNCWgy2Xfto2ypzsh2HTaxBEf/UhApFHd5EJNUlifD9lKgjxOt1WV2zbc2gmWi4SFwkHPMuYQzdLhQiJkDZ4aCR/3pwekjuEBUeqSeAWYZopaZrqEk869KIPuhVfnGQ8APA+m7DXp9HPzNaf7qTT0xZjERlIBb2AfVDVPZMgxYoCqGrcG6tczADhi7iZhg2F0MEiOJBsIi8eWr98auc7/+T94yspWE7Hf32fyQOMSbjkhvwGLwONKk4/9/CMEL0R5vKDvILpfTQrqOCnDqV9IbiRdPr0L3+MsvAH0EsGz3+5nGy61IBwSd2FTIIz6C1cEk3i6amcROA+jTax3fjgOpnus5ONWBouBrGD5RyK59Ze/59gkQ32NSpokQgKkkbSxQCiaqsHvuOL8J3w2MUkfr/nqzrF7lD3k1LD/WrvJ6m1ZKcBov0gAfMr5noz2Gw3VGqf122Gz34LkrY="
                        )
                )
                .build();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

        new PuppetClickPacket(registry).handle(event.getPlayer());

        for (PuppetEntity puppetEntity : registry.getRegistry()) {

            List<String> demo = new ArrayList<>();

            demo.add(ChatColor.RED + "HOLA BEBE");
            demo.add("");
            demo.add(ChatColor.GREEN + "YA QUE CONTIGO NO SIRVE LA LABIA");
            demo.add("");
            demo.add(ChatColor.AQUA + "Y TE CREES MUY SABIA... PERO VAS A CAER TE LO DIGO MUJER");

            ((PlayerPuppetEntity) puppetEntity).setHolograms(event.getPlayer(), demo);
            puppetEntity.register(event.getPlayer());
        }

    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        for (PuppetEntity puppetEntity : registry.getRegistry()) {
            puppetEntity.unregister(event.getPlayer());
        }
    }

}
