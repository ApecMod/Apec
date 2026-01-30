package uk.co.hexeption.apec.settings.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import uk.co.hexeption.apec.settings.Setting;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class SettingButton extends PlainTextButton {

    Setting setting;

    public SettingButton(int i, int j, int k, int l, Setting setting) {

        super(i, j, k, l, Component.literal(""), button -> setting.Toggle(), Minecraft.getInstance().font);

        this.setting = setting;
    }

    @Override
    //? if >= 1.21.11 {
    public void renderContents(GuiGraphics guiGraphics, int i, int j, float f) {
    //?} else {
    /*public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {
     *///?}
        if (this.isHovered) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x1adddddd);
        }

        // Render the setting name with scrolling text
        var title = Component.literal(setting.name).withStyle(style -> style.withColor(setting.enabled ? CommonColors.GREEN : CommonColors.RED).withUnderlined(false));

        // Calculate text area bounds (scaled for 1.1f)
        int textX = this.getX() + 7;
        int textY = this.getY() + 6;
        int textWidth = (int) ((this.width - 14) / 1.1f); // Available width accounting for padding and scale
        int textHeight = 10; // Height for the text area

        GuiGraphicsUtils.push(guiGraphics);

        GuiGraphicsUtils.scale(guiGraphics, 1.1f);

        // Scale coordinates for the 1.1f scale
        int scaledX = (int) (textX / 1.1f);
        int scaledY = (int) (textY / 1.1f);
        int scaledHeight = (int) (textHeight / 1.1f);

        // TODO: why white? idk! but it works! so i dont care!! genuinely, this makes absolutely no sense
        //? if >= 1.21.11 {
        // TODO: idk how scrolling text works in 1.21.11
        guiGraphics.textRendererForWidget(this, GuiGraphics.HoveredTextEffects.NONE).accept(scaledX, scaledY, title);
        //?} else {
        /*renderScrollingString(guiGraphics, Minecraft.getInstance().font, title, scaledX, scaledY, scaledX + textWidth, scaledY + scaledHeight, /^? if >= 1.21.8 {^/ CommonColors.WHITE /^?} else {^/ /^title.getStyle().getColor().getValue() ^//^?}^/);
        *///?}

        GuiGraphicsUtils.pop(guiGraphics);

        // Render the description
        renderDescription(guiGraphics);
    }

    private void renderDescription(GuiGraphics guiGraphics) {

        GuiGraphicsUtils.push(guiGraphics);

        GuiGraphicsUtils.scale(guiGraphics, 0.8f);

        ApecUtils.drawWrappedText(
                guiGraphics,
                setting.description,
                (int) ((this.getX() + 7) / 0.8f),
                (int) ((this.getY() + 18) / 0.8f),
                140,
                CommonColors.WHITE
        );

        GuiGraphicsUtils.pop(guiGraphics);
    }

}
