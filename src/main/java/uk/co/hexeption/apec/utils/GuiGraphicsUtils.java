package uk.co.hexeption.apec.utils;

//? if >= 1.21.8
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphics;
//? if >= 1.21.8 {
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;
//?} else {
/*import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;
*///?}

public class GuiGraphicsUtils {
    public static /*? if >= 1.21.8 {*/ RenderPipeline /*?} else {*/ /*Function<ResourceLocation, RenderType> *//*?}*/ getGuiTextured() {
        //? if >= 1.21.8 {
        return RenderPipelines.GUI_TEXTURED;
        //?} else {
        /*return RenderType::guiTextured;
        *///?}
    }

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
