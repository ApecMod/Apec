package uk.co.hexeption.apec.hud.elements.air;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class AirText extends Element {

    private int textWidth;

    public AirText() {
        super(ElementType.AIR_TEXT);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {
        if(!Apec.INSTANCE.settingsManager.getSettingState(SettingID.AIR_TEXT)) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        assert player != null;

        if (!player.isUnderWater() && !editMode) {
            return;
        }

        int base_air = player.getMaxAirSupply();
        int air = player.getAirSupply();

        float airPerc = Math.max(air / (float)base_air, 0) * 100f;

        String airText = ((int)airPerc) + "% Air";

        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);

        ApecUtils.drawOutlineText(mc, graphics, airText, (int) (statBar.x - mc.font.width(airText)), (int) (statBar.y - 10), GuiGraphicsUtils.fixColourAlpha(0x8ba6b2));
        textWidth = mc.font.width(airText);
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190 + 112 + 70, 73));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(-textWidth * scale, -11 * scale);
    }
}
