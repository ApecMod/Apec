package uk.co.hexeption.apec.settings.menu;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.network.chat.Component;
import uk.co.hexeption.apec.settings.Setting;
import uk.co.hexeption.apec.utils.ApecUtils;

public class SettingButton extends PlainTextButton {

    Setting setting;

    public SettingButton(int i, int j, int k, int l, Setting setting) {

        super(i, j, k, l, Component.literal(""), button -> setting.Toggle(), Minecraft.getInstance().font);

        this.setting = setting;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int i, int j, float f) {

        if (this.isHovered) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + this.width, this.getY() + this.height, 0x1adddddd);
        }

        // Render the setting name with scrolling text
        var title = Component.literal(setting.name).withStyle(style -> style.withColor(setting.enabled ? 0x00ff00 : 0xff0000).withUnderlined(false));

        // Calculate text area bounds (scaled for 1.1f)
        int textX = this.getX() + 7;
        int textY = this.getY() + 6;
        int textWidth = (int) ((this.width - 14) / 1.1f); // Available width accounting for padding and scale
        int textHeight = 10; // Height for the text area

        //? if >= 1.21.6 {
        /*guiGraphics.pose().pushMatrix();
        *///?} else {
        guiGraphics.pose().pushPose();
        //?}

        //? if >= 1.21.6 {
        /*guiGraphics.pose().scale(1.1f, 1.1f);
        *///?} else {
        guiGraphics.pose().scale(1.1f, 1.1f, 1.1f);
         //?}

        // Scale coordinates for the 1.1f scale
        int scaledX = (int) (textX / 1.1f);
        int scaledY = (int) (textY / 1.1f);
        int scaledHeight = (int) (textHeight / 1.1f);

        renderScrollingString(guiGraphics, Minecraft.getInstance().font, title, scaledX, scaledY, scaledX + textWidth, scaledY + scaledHeight, title.getStyle().getColor().getValue());

        //? if >= 1.21.6 {
        /*guiGraphics.pose().popMatrix();
        *///?} else {
        guiGraphics.pose().popPose();
         //?}

        // Render the description
        renderDescription(guiGraphics);
    }

    private void renderDescription(GuiGraphics guiGraphics) {

        //? if >= 1.21.6 {
        /*guiGraphics.pose().pushMatrix();
        *///?} else {
        guiGraphics.pose().pushPose();
         //?}
        //? if >= 1.21.6 {
        /*guiGraphics.pose().scale(0.8f, 0.8f);
        *///?} else {
        guiGraphics.pose().scale(0.8f, 0.8f, 0.8f);
         //?}

        ApecUtils.drawWrappedText(
                guiGraphics,
                setting.description,
                (int) ((this.getX() + 7) / 0.8f),
                (int) ((this.getY() + 18) / 0.8f),
                140,
                0xffffff
        );

        //? if >= 1.21.6 {
        /*guiGraphics.pose().popMatrix();
        *///?} else {
        guiGraphics.pose().popPose();
         //?}
    }

}
