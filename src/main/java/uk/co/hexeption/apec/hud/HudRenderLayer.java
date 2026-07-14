package uk.co.hexeption.apec.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphicsExtractor;

@FunctionalInterface
public interface HudRenderLayer {

    void render(GuiGraphicsExtractor guiGraphics, DeltaTracker tickCounter);
}
