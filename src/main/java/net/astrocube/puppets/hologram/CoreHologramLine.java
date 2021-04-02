package net.astrocube.puppets.hologram;

public class CoreHologramLine implements HologramLine {

    private final String text;
    private final int entity;

    public CoreHologramLine(String text, int entity) {
        this.text = text;
        this.entity = entity;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getEntity() {
        return entity;
    }

}
