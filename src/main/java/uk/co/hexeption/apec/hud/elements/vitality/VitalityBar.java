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

public class VitalityBar extends Element {
    public VitalityBar() {
        super(ElementType.VITALITY_BAR);
    }

    @Override
    public void drawText(GuiGraphicsExtractor graphics, boolean editMode) {
        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.VITALITY_BAR)) {
            return;
        }

        int vit = Apec.SKYBLOCK_INFO.getPlayerStats().vitality();
        int base_vit = Apec.SKYBLOCK_INFO.getPlayerStats().base_vitality();

        float vitFactor = vit > base_vit ? 1 : (float) vit / (float) base_vit;

        ApecTextures vitalityBarTexture = ApecTextures.STATUS_BAR;
        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);
        int width = (int) statBar.x;
        int height = (int) statBar.y;

        // Empty
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), vitalityBarTexture.getIdentifier(), width, height, 0, 0, 182, 5, vitalityBarTexture.getWidth(), vitalityBarTexture.getHeight());

        // Full
        graphics.blit(GuiGraphicsUtils.getGuiTextured(), vitalityBarTexture.getIdentifier(), width, height, 0, 5, (int) (vitFactor * 182f), 5, vitalityBarTexture.getWidth(), vitalityBarTexture.getHeight());
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190, 53));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(182 * scale, 5 * scale);
    }
}
