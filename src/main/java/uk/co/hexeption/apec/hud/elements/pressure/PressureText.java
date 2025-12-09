package uk.co.hexeption.apec.hud.elements.pressure;

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

public class PressureText extends Element {

    private int textWidth;

    public PressureText() {
        super(ElementType.PRESSURE_TEXT);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {
        if(!Apec.INSTANCE.settingsManager.getSettingState(SettingID.PRESSURE_TEXT)) {
            return;
        }

        Player player = Minecraft.getInstance().player;
        assert player != null;

        if (!player.isUnderWater() && !editMode) {
            return;
        }

        int pressure = Apec.SKYBLOCK_INFO.getPlayerStats().pressure();

        String pressureText = pressure + "% Pressure";

        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);

        ApecUtils.drawOutlineText(mc, graphics, pressureText, (int) (statBar.x - mc.font.width(pressureText)), (int) (statBar.y - 10), GuiGraphicsUtils.fixColourAlpha(0x19ade6));
        textWidth = mc.font.width(pressureText);
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190 + 112 + 70, 93));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(-textWidth * scale, -11 * scale);
    }
}
