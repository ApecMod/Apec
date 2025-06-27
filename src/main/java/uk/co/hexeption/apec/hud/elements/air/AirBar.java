package uk.co.hexeption.apec.hud.elements.air;

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

public class AirBar extends Element {
    public AirBar() {
        super(ElementType.AIR_BAR);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {
        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.SHOW_AIR_BAR)) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        assert player != null;

        if (!player.isUnderWater() && !editMode) {
            return;
        }

        int base_air = player.getMaxAirSupply();
        int air = player.getAirSupply();

        float airFactor = Math.max(air / (float) base_air, 0);

        ApecTextures airBarTexture = ApecTextures.STATUS_BAR;
        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);
        int width = (int) statBar.x;
        int height = (int) statBar.y;

        // Empty Bar
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), airBarTexture.getResourceLocation(),
                width, height,
                0, 40, 182, 5,
                airBarTexture.getWidth(), airBarTexture.getHeight()
        );

        // Air Bar
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), airBarTexture.getResourceLocation(),
                width, height,
                0, 45, (int) (airFactor * 182), 5,
                airBarTexture.getWidth(), airBarTexture.getHeight()
        );
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190, 73));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(182 * scale, 5 * scale);
    }

}
