package net.astrocube.puppets.player.skin;

public class CorePuppetSkin implements PuppetSkin {

    private final String texture;
    private final String signature;

    public CorePuppetSkin(String texture, String signature) {
        this.texture = texture;
        this.signature = signature;
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public String getSignature() {
        return signature;
    }

}
