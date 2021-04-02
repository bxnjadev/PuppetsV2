package net.astrocube.puppets.entity;

import org.bukkit.entity.Player;

import java.util.function.Consumer;

public class CoreClickAction implements ClickAction {

    private final Type type;
    private final Consumer<Player> action;

    public CoreClickAction(Type type, Consumer<Player> action) {
        this.type = type;
        this.action = action;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public Consumer<Player> getAction() {
        return action;
    }
}
