package uk.co.hexeption.apec.hud;

import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;

@FunctionalInterface
public interface HudRenderLayer {

    void render(GuiGraphics guiGraphics, DeltaTracker tickCounter);
}
