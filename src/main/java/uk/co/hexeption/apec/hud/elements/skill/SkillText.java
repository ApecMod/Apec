package uk.co.hexeption.apec.hud.elements.skill;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.hud.SkillType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class SkillText extends Element {

    private int stringWidth = 0;

    public SkillText() {

        super(ElementType.SKILL_TEXT);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        var skillTextPos = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);

        var ps = Apec.SKYBLOCK_INFO.getPlayerStats();

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.SKILL_TEXT)) {
            if (ps.skill_shown()) {
                stringWidth = mc.font.width(ps.skill_info());

                SkillType skillType = SkillType.getSkillType(ps.skill_info());
                int color = switch (skillType) {
                    case NONE, FARMING -> 0xD0CE30;
                    case COMBAT -> 0xDC3615;
                    case MINING -> 0x797979;
                    case FORAGING -> 0x237926;
                    case ENCHANTING -> 0x711C99;
                    case FISHING -> 0x184A87;
                    case ALCHEMY -> 0x981B4C;
                };
                ApecUtils.drawOutlineText(mc, graphics, ps.skill_info(), (int) (skillTextPos.x), (int) (skillTextPos.y - 10), GuiGraphicsUtils.fixColourAlpha(color));
            }
        }

        if (editMode) {
            stringWidth = mc.font.width("+0.0 Farming (0/0)");
            ApecUtils.drawOutlineText(mc, graphics, "+0.0 Farming (0/0)", (int) (skillTextPos.x), (int) (skillTextPos.y - 10), GuiGraphicsUtils.fixColourAlpha(0x4ca7a8));
        }

    }

    @Override
    public Vector2f getAnchorPointPosition() {

        return this.menu.applyGlobalChanges(this, new Vector2f((int) (mc.getWindow().getGuiScaledWidth() * 0.5f) - stringWidth * 0.5f, mc.getWindow().getGuiScaledHeight() - 30 + 20 * (1 - this.menu.getGuiComponent(ElementType.BOTTOM_BAR).getScale())));
    }

    @Override
    public Vector2f getBoundingPoint() {

        return new Vector2f(stringWidth * scale, -11 * scale);
    }

}
