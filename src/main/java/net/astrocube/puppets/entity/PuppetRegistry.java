package net.astrocube.puppets.entity;

import java.util.Set;
import java.util.UUID;

public interface PuppetRegistry {

    /**
     * Register a puppet inside registry.
     * @param entity to register
     */
    void register(PuppetEntity entity);

    /**
     * @return set of registered puppets.
     */
    Set<PuppetEntity> getRegistry();

    /**
     * Unregister a puppet with an identifier
     * @param uuid to unregister
     */
    void unregister(UUID uuid);

}
