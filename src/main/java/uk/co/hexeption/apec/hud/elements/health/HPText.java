package uk.co.hexeption.apec.hud.elements.health;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class HPText extends Element {

    private int textWidth;

    public HPText() {
        super(ElementType.HP_TEXT);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        if(Apec.INSTANCE.settingsManager.getSettingState(SettingID.HP_TEXT) == false) {
            return;
        }

        boolean showAPBar = false;

        int hp = Apec.SKYBLOCK_INFO.getPlayerStats().hp();
        int base_hp = Apec.SKYBLOCK_INFO.getPlayerStats().base_hp();
        int ap = Apec.SKYBLOCK_INFO.getPlayerStats().absorption();
        int base_ap = Apec.SKYBLOCK_INFO.getPlayerStats().base_absorption();
        int heal_duration = Apec.SKYBLOCK_INFO.getPlayerStats().heal_duration();
        char heal_duration_tick = Apec.SKYBLOCK_INFO.getPlayerStats().heal_duration_tick();

        int addedHp = hp + ap;
        String hpText = (!showAPBar && ap != 0 ? ChatFormatting.YELLOW + String.valueOf(addedHp) + ChatFormatting.RESET : hp) + "/" + base_hp + " HP" + (heal_duration != 0 ? " +" + heal_duration + "/s " + heal_duration_tick : "");

        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);

        ApecUtils.drawOutlineText(mc, graphics, hpText, (int) (statBar.x - mc.font.width(hpText)), (int) (statBar.y - 10), GuiGraphicsUtils.fixColourAlpha(0xd10808));
        textWidth = mc.font.width(hpText);

        if (ap != 0 && showAPBar) {
            String apText = ap + "/" + base_ap + " AP";
            ApecUtils.drawOutlineText(mc, graphics, apText, (int) (statBar.x - 5 - mc.font.width(apText) - mc.font.width(hpText)), (int) (statBar.y - 10), GuiGraphicsUtils.fixColourAlpha(0x1966AD));
        }
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190 + 112 + 70, 15));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(-textWidth * scale, -11 * scale);
    }
}
