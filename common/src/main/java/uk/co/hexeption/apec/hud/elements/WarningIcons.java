package uk.co.hexeption.apec.hud.elements;

import java.util.ArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderType;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.EventIDs;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.utils.ApecUtils;

public class WarningIcons extends Element {

    public WarningIcons() {

        super(ElementType.WARNING_ICONS);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        var otherData = Apec.SKYBLOCK_INFO.getOtherData();

        if (editMode) {
            otherData.currentEvents = new ArrayList<EventIDs>() {{
                add(EventIDs.INV_FULL);
                add(EventIDs.TRADE_OUT);
                add(EventIDs.TRADE_IN);
                add(EventIDs.COIN_COUNT);
                add(EventIDs.SERVER_REBOOT);
                add(EventIDs.HIGH_PING);
            }};
        }

        if (otherData.currentEvents != null) {
            var warningIconPos = ApecUtils.scalarMultiply(this.getCurrentAnchorPoint(), 1 / scale);
            for (int i = 0; i < otherData.currentEvents.size(); i++) {
                var eventID = otherData.currentEvents.get(i);
                if (eventID != null) {
                    drawIconForID(eventID, (int)(warningIconPos.x - (i+1) * 20), (int) warningIconPos.y, graphics);
                }
            }
        }

    }

    private void drawIconForID(EventIDs eventID, int x, int y, GuiGraphics graphics) {

        var warningIconTexture = ApecTextures.ICONS;
        switch (eventID) {
            case INV_FULL -> graphics.blit(RenderType::guiTextured, warningIconTexture.getResourceLocation(), x, y, 1,226,14,13, warningIconTexture.getWidth(), warningIconTexture.getHeight());
            case TRADE_IN -> graphics.blit(RenderType::guiTextured, warningIconTexture.getResourceLocation(), x, y, 32,226,15,13, warningIconTexture.getWidth(), warningIconTexture.getHeight());
            case TRADE_OUT -> graphics.blit(RenderType::guiTextured, warningIconTexture.getResourceLocation(), x, y, 16,226,15,13, warningIconTexture.getWidth(), warningIconTexture.getHeight());
            case COIN_COUNT -> graphics.blit(RenderType::guiTextured, warningIconTexture.getResourceLocation(), x, y, 48,226,13,13, warningIconTexture.getWidth(), warningIconTexture.getHeight());
            case SERVER_REBOOT -> graphics.blit(RenderType::guiTextured, warningIconTexture.getResourceLocation(), x, y, 62,226,15,13, warningIconTexture.getWidth(), warningIconTexture.getHeight());
            case HIGH_PING -> graphics.blit(RenderType::guiTextured, warningIconTexture.getResourceLocation(), x, y, 78,226,15,13, warningIconTexture.getWidth(), warningIconTexture.getHeight());
        }
    }

    @Override
    public Vector2f getAnchorPointPosition() {

        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 2, 65));
    }

    @Override
    public Vector2f getBoundingPoint() {

        return new Vector2f(-120 * scale, 15 * scale);
    }

}
