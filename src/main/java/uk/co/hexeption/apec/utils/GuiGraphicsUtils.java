package uk.co.hexeption.apec.utils;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.ARGB;

public class GuiGraphicsUtils {
    public static int fixColourAlpha(int colour) {
        //? if >= 1.21.8 {
        if ((colour & 0xfc000000) == 0) {
            return ARGB.opaque(colour);
        } else {
            return colour;
        }
        //?} else {
        /*return colour;
        *///?}
    }

    public static void push(GuiGraphics graphics) {
        //? if >= 1.21.8 {
        graphics.pose().pushMatrix();
        //?} else {
        /*graphics.pose().pushPose();
        *///?}
    }

    public static void pop(GuiGraphics graphics) {
        //? if >= 1.21.8 {
        graphics.pose().popMatrix();
        //?} else {
        /*graphics.pose().popPose();
        *///?}
    }

    public static void scale(GuiGraphics graphics, float scale) {
        //? if >= 1.21.8 {
        graphics.pose().scale(scale, scale);
        //?} else {
        /*graphics.pose().scale(scale, scale, scale);
        *///?}
    }

    public static void translate(GuiGraphics graphics, float x, float y, float z) {
        //? if >= 1.21.8 {
        graphics.pose().translate(x, y);
        //?} else {
        /*graphics.pose().translate(x, y, z);
        *///?}
    }
}
