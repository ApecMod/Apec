package uk.co.hexeption.apec.hud.elements;

import java.awt.Color;
import net.minecraft.client.gui.GuiGraphics;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class DebugText extends Element {
    public DebugText() {
        super(ElementType.DEBUG_TEXT);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {
        if(Apec.SKYBLOCK_INFO.getScoreboard() != null) {
            ApecUtils.drawOutlineWrappedText(mc, graphics, Apec.SKYBLOCK_INFO.getScoreboard().toString(), 2, 30, 300, GuiGraphicsUtils.fixColourAlpha(new Color(170, 84, 255, 255).getRGB()));
            ApecUtils.drawOutlineWrappedText(mc, graphics, Apec.SKYBLOCK_INFO.getPlayerStats().toString(), 2, 80, 300, GuiGraphicsUtils.fixColourAlpha(new Color(84, 255, 170, 255).getRGB()));
        }
    }
}
