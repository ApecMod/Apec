package uk.co.hexeption.apec.hud.elements.skill;

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.hud.SkillType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class SkillBar extends Element {

    public SkillBar() {

        super(ElementType.SKILL_BAR);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        var skillBarPos = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);

        var ps = Apec.SKYBLOCK_INFO.getPlayerStats();

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.SHOW_SKILL_XP)) {

            if (ps.skill_shown()) {
                var skillBarTexture = ApecTextures.SKILL_BAR;
                var skillProgress = ps.skill_xp_percentage();
                if (skillProgress > 1f) {
                    skillProgress = 1f;
                }
                skillProgress *= 182f;

                graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                        (int) skillBarPos.x, (int) skillBarPos.y,
                        0, 0, 182, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());

                var skillType = SkillType.getSkillType(ps.skill_info());

                switch (skillType) {
                    case NONE, FARMING -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 5, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                    case COMBAT -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 15, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                    case MINING -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 25, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                    case FORAGING -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 35, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                    case ENCHANTING -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 45, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                    case FISHING -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 55, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                    case ALCHEMY -> graphics.blit(GuiGraphicsUtils.getGuiTextured(), skillBarTexture.getIdentifier(),
                            (int) skillBarPos.x, (int) skillBarPos.y,
                            0, 65, (int) skillProgress, 5, skillBarTexture.getWidth(), skillBarTexture.getHeight());
                }

            }

        }
        if (editMode) {
            graphics.blit(GuiGraphicsUtils.getGuiTextured(), ApecTextures.SKILL_BAR.getIdentifier(),
                    (int) skillBarPos.x, (int) skillBarPos.y,
                    0, 25, 182, 5, ApecTextures.SKILL_BAR.getWidth(), ApecTextures.SKILL_BAR.getHeight());
        }
    }

    @Override
    public Vector2f getAnchorPointPosition() {

        return this.menu.applyGlobalChanges(this, new Vector2f((int) (mc.getWindow().getGuiScaledWidth() / 2 - 91), mc.getWindow().getGuiScaledHeight() - 30 + 20 * (1 - this.menu.getGuiComponent(ElementType.BOTTOM_BAR).getScale())));

    }

    @Override
    public Vector2f getBoundingPoint() {

        return new Vector2f(182 * scale, 5 * scale);
    }

}
