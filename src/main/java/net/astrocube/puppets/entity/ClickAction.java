package net.astrocube.puppets.entity;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public interface ClickAction {

    /**
     * @return click type of the action
     */
    Type getType();

    /**
     * @return action to run.
     */
    Consumer<Player> getAction();

    enum Type {
        LEFT, RIGHT
    }

}
