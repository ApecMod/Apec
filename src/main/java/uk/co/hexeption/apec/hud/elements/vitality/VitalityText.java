package uk.co.hexeption.apec.hud.elements.vitality;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;
import uk.co.hexeption.apec.utils.MultiColourText;

public class VitalityText extends Element {

    private int stringWidth = 0;

    public VitalityText() {
        super(ElementType.VITALITY_TEXT);
    }

    @Override
    public void drawText(GuiGraphicsExtractor graphics, boolean editMode) {
        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.VITALITY_TEXT)) {
            return;
        }

        int vit = Apec.SKYBLOCK_INFO.getPlayerStats().vitality();
        int base_vit = Apec.SKYBLOCK_INFO.getPlayerStats().base_vitality();

        float vitFactor = vit > base_vit ? 1 : (float) vit / (float) base_vit;

        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);
        int width = (int) statBar.x;
        int height = (int) statBar.y;

        String vitText = vit + "/" + base_vit + " Vit";

        ApecUtils.drawOutlineText(mc, graphics, vitText,  (int) (width - mc.font.width(vitText)), (int) (height - 10), GuiGraphicsUtils.fixColourAlpha(0xd13228));
        stringWidth = mc.font.width(vitText);
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return this.menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190 + 112 + 70, 53));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(-stringWidth * scale, -11 * scale);
    }
}
