package uk.co.hexeption.apec.hud.elements.drillfuel;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.CommonColors;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.hud.Element;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class DrillFuelText extends Element {

    private int textWidth;

    public DrillFuelText() {
        super(ElementType.DRILL_FUEL_TEXT);
    }

    @Override
    public void drawText(GuiGraphics graphics, boolean editMode) {

        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.DRILL_FUEL_TEXT)) {
            return;
        }

        int drillFuelRemaining = Apec.SKYBLOCK_INFO.getPlayerStats().drillFuelRemaining();
        int drillFuelCapacity = Apec.SKYBLOCK_INFO.getPlayerStats().drillFuelCapacity();

        // Only show if player has drill fuel (capacity > 0)
        if (drillFuelCapacity == 0) {
            return;
        }

        // Format the fuel text
        String fuelText = formatFuelValue(drillFuelRemaining) + "/" + formatFuelValue(drillFuelCapacity) + " Fuel";

        // Color based on fuel percentage
        float fuelPercentage = (float) drillFuelRemaining / (float) drillFuelCapacity;
        ChatFormatting color;
        if (fuelPercentage > 0.5f) {
            color = ChatFormatting.GREEN;
        } else if (fuelPercentage > 0.25f) {
            color = ChatFormatting.YELLOW;
        } else {
            color = ChatFormatting.RED;
        }

        String coloredFuelText = color + fuelText + ChatFormatting.RESET;

        Vector2f statBar = ApecUtils.scalarMultiply(getCurrentAnchorPoint(), 1f / scale);

        ApecUtils.drawOutlineText(mc, graphics, coloredFuelText, (int) (statBar.x - mc.font.width(coloredFuelText)), (int) (statBar.y - 10), getColorValue(color));
        textWidth = mc.font.width(coloredFuelText);
    }

    @Override
    public Vector2f getAnchorPointPosition() {
        return menu.applyGlobalChanges(this, new Vector2f(mc.getWindow().getGuiScaledWidth() - 190 + 112 + 70, 73));
    }

    @Override
    public Vector2f getBoundingPoint() {
        return new Vector2f(-textWidth * scale, -11 * scale);
    }

    /**
     * Formats fuel values to use k/m notation for large numbers
     */
    private String formatFuelValue(int value) {
        if (value >= 1000000) {
            return String.format("%.1fM", value / 1000000.0);
        } else if (value >= 1000) {
            return String.format("%.1fk", value / 1000.0);
        } else {
            return String.valueOf(value);
        }
    }

    /**
     * Gets the color value for the outline text
     */
    private int getColorValue(ChatFormatting color) {
        return switch (color) {
            case GREEN -> GuiGraphicsUtils.fixColourAlpha(0x55FF55);
            case YELLOW -> GuiGraphicsUtils.fixColourAlpha(0xFFFF55);
            case RED -> GuiGraphicsUtils.fixColourAlpha(0xFF5555);
            default -> CommonColors.WHITE;
        };
    }
}
