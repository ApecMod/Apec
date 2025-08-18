package uk.co.hexeption.apec.utils;

import net.minecraft.client.gui.GuiGraphics;
import uk.co.hexeption.apec.MC;

public class MultiColourText implements MC {

    private int x;
    private int y;
    private int shiftValue = 0;

    private String[] stringSet;
    private int[] colorSet;

    public MultiColourText() {

    }

    public MultiColourText(String[] stringSet, int[] colorSet, int x, int y) {
        this(stringSet, colorSet);
        this.x = x;
        this.y = y;
    }

    public MultiColourText(String[] stringSet, int[] colorSet) {
        this.stringSet = stringSet;
        this.colorSet = colorSet;
    }

    public MultiColourText(String[] stringSet, Integer[] colorSet) {
        this.stringSet = stringSet;
        int[] i = new int[colorSet.length];
        for (int j = 0; j < colorSet.length; j++) {
            i[j] = colorSet[j];
        }
        this.colorSet = i;
    }

    public void setString(String[] stringSet, int[] colorSet) {
        this.stringSet = stringSet;
        this.colorSet = colorSet;
    }

    public void setXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setShift(int shiftValue) {
        this.shiftValue = shiftValue;
    }

    public int width() {
        int width = 0;
        for (String string : stringSet) {
            width += mc.font.width(string);
        }
        return width;
    }

    public String[] getStringSet() {
        return this.stringSet;
    }

    public int[] getColorSet() {
        return this.colorSet;
    }

    public void render(GuiGraphics guiGraphics) {
        int nowX = this.x;
        for (int i = 0; i < this.stringSet.length; i++) {
            ApecUtils.drawOutlineText(mc, guiGraphics, stringSet[i], nowX + this.shiftValue, this.y, GuiGraphicsUtils.fixColourAlpha(colorSet[i]));
            nowX += mc.font.width(stringSet[i]);
        }
    }
}
