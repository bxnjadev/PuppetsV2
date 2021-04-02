package net.astrocube.puppets.entity;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CorePuppetRegistry implements PuppetRegistry {

    private final Set<PuppetEntity> registry;

    public CorePuppetRegistry() {
        this.registry = new HashSet<>();
    }

    @Override
    public void register(PuppetEntity entity) {
        this.registry.add(entity);
    }

    @Override
    public Set<PuppetEntity> getRegistry() {
        return this.registry;
    }

    @Override
    public void unregister(UUID uuid) {
        registry.removeIf(registry -> {

            if (registry.getUUID().equals(uuid)) {
                return true;
            }

            return false;
        });
    }
}
