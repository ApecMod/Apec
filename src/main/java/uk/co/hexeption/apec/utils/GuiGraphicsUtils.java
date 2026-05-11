package uk.co.hexeption.apec.utils;

import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.util.ARGB;

public class GuiGraphicsUtils {
    public static RenderPipeline getGuiTextured() {
        return RenderPipelines.GUI_TEXTURED;
    }

    public static int fixColourAlpha(int colour) {
        if ((colour & 0xfc000000) == 0) {
            return ARGB.opaque(colour);
        } else {
            return colour;
        }
    }

    public static void push(GuiGraphicsExtractor graphics) {
        graphics.pose().pushMatrix();
    }

    public static void pop(GuiGraphicsExtractor graphics) {
        graphics.pose().popMatrix();
    }

    public static void scale(GuiGraphicsExtractor graphics, float scale) {
        graphics.pose().scale(scale, scale);
    }

    public static void translate(GuiGraphicsExtractor graphics, float x, float y) {
        graphics.pose().translate(x, y);
    }
}
