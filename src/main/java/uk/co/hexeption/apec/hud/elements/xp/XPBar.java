package uk.co.hexeption.apec.hud.elements.xp;

//? >= 1.21.6 {
/*import net.minecraft.client.renderer.RenderPipelines;
*///?} else {
import net.minecraft.client.renderer.RenderType;
 //?}

import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;

public class XPBar extends Element {

    public XPBar() {
        super(ElementType.XP_BAR);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        if(Apec.INSTANCE.settingsManager.getSettingState(SettingID.XP_BAR) == false) {
            return;
        }

        ApecTextures xpBarTexture = ApecTextures.STATUS_BAR;

        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);
        int width = (int) statBar.x;
        int height = (int) statBar.y;

        // Empty Bar
        graphics.blit(/*? >= 1.21.6 {*/ /*RenderPipelines.GUI_TEXTURED *//*?} else {*/ RenderType::guiTextured /*?}*/, xpBarTexture.getResourceLocation(), width, height, 0, 30, 182, 5, xpBarTexture.getWidth(), xpBarTexture.getHeight());

        // Full Bar
        graphics.blit(/*? >= 1.21.6 {*/ /*RenderPipelines.GUI_TEXTURED *//*?} else {*/ RenderType::guiTextured /*?}*/, xpBarTexture.getResourceLocation(), width, height, 0, 35, (int) (mc.player.experienceProgress * 182f), 5, xpBarTexture.getWidth(), xpBarTexture.getHeight());
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return this.menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190, 53));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(182 * scale, 5 * scale);
    }
}
