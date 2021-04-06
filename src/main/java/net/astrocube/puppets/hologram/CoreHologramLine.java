package net.astrocube.puppets.hologram;

public class CoreHologramLine implements HologramLine {

    private final String text;
    private final int entity;
    private final double y;

    public CoreHologramLine(String text, int entity, double y) {
        this.text = text;
        this.entity = entity;
        this.y = y;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public int getEntity() {
        return entity;
    }

    @Override
    public double getY() {
        return y;
    }

}
