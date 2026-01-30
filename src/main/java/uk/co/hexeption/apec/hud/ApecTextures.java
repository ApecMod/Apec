package uk.co.hexeption.apec.hud;

import net.minecraft.resources.Identifier;

public enum ApecTextures {

    STATUS_BAR("status_bar", 256, 256),
    ICONS("icons", 256, 256),
    BOTTOM_BAR("bottom_bar", 256, 256),
    SKILL_BAR("skill_bar", 256, 256);

    private final Identifier resourceLocation;
    private final int width;
    private final int height;

    ApecTextures(String path, int width, int height) {
        this.resourceLocation = Identifier.fromNamespaceAndPath("apec", "textures/" + path + ".png");
        this.width = width;
        this.height = height;
    }

    public Identifier getIdentifier() {
        return resourceLocation;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
