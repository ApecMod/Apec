package uk.co.hexeption.apec.hud.elements.drillfuel;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class DrillFuelBar extends Element {

    public DrillFuelBar() {
        super(ElementType.DRILL_FUEL_BAR);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.DRILL_FUEL_BAR)) {
            return;
        }

        int drillFuelRemaining = Apec.SKYBLOCK_INFO.getPlayerStats().drillFuelRemaining();
        int drillFuelCapacity = Apec.SKYBLOCK_INFO.getPlayerStats().drillFuelCapacity();

        // Only show if player has drill fuel (capacity > 0)
        if (drillFuelCapacity == 0) {
            return;
        }

        float fuelFactor = (float) drillFuelRemaining / (float) drillFuelCapacity;

        ApecTextures drillFuelBarTexture = ApecTextures.STATUS_BAR;
        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);
        int width = (int) statBar.x;
        int height = (int) statBar.y;

        // Empty Bar - using a different row from the texture for drill fuel
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), drillFuelBarTexture.getResourceLocation(), width, height, 0, 80, 182, 5, drillFuelBarTexture.getWidth(), drillFuelBarTexture.getHeight());

        // Full Bar - with drill fuel color (orange/brown)
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), drillFuelBarTexture.getResourceLocation(), width, height, 0, 85, (int) (fuelFactor * 182f), 5, drillFuelBarTexture.getWidth(), drillFuelBarTexture.getHeight());
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return this.menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190, 73));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(182 * scale, 5 * scale);
    }
}
