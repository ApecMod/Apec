package uk.co.hexeption.apec.hud.elements.pressure;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class PressureBar extends Element {
    public PressureBar() {
        super(ElementType.PRESSURE_BAR);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {
        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.SHOW_PRESSURE_BAR)) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        assert player != null;

        if (!player.isUnderWater() && !editMode) {
            return;
        }

        int pressure = Apec.SKYBLOCK_INFO.getPlayerStats().pressure();
        float pressureFactor = Math.max(1.0f - (pressure / 100.0f), 0);

        ApecTextures pressureBarTexture = ApecTextures.STATUS_BAR;
        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);
        int width = (int) statBar.x;
        int height = (int) statBar.y;

        // Pressure Empty Bar
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), pressureBarTexture.getResourceLocation(),
                width, height,
                0, 90, 182, 5,
                pressureBarTexture.getWidth(), pressureBarTexture.getHeight()
        );

        // Pressure Filled Bar
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), pressureBarTexture.getResourceLocation(),
                width, height,
                0, 95, (int) (pressureFactor * 182), 5,
                pressureBarTexture.getWidth(), pressureBarTexture.getHeight()
        );
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190, 93));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(182 * scale, 5 * scale);
    }

}
