package uk.co.hexeption.apec.utils;

import java.util.HashMap;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.settings.SettingID;

public class ApecUtils {

    private static HashMap <String,Integer> multipleNotations = new HashMap<String, Integer>() {{
        put("k",1000);
        put("m",1000000);
    }};

    /**
     * @brief Converts values strings which contain values represented in short form (ex: "20k") to float
     * @param s = input string
     * @return Converted output
     */
    public static float hypixelShortValueFormattingToFloat(String s) {
        s = s.replace(",","");
        for (String notation : multipleNotations.keySet()) {
            if (s.contains(notation)) {
                s = s.replace(notation,"");
                return Float.parseFloat(s) * multipleNotations.get(notation);
            }
        }
        return Float.parseFloat(s);
    }

    /**
     * @brief This is made since there is this weird character in the purse text that im too lazy to see what unicode it has so now we have this
     * @return Returns a string that has all non numerical characters removed from a string
     */

    public static String removeNonNumericalChars(String s) {

        StringBuilder _s = new StringBuilder();

        for (int i = 0;i < s.length();i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c) || c == '.') _s.append(c);
        }

        return _s.toString();

    }

    public static boolean isContainedIn(String s1, String s2) {
        char[] targetChars = s2.toCharArray();
        char[] sourceChars = s1.toCharArray();

        int targetIndex = 0;
        for (char sourceChar : sourceChars) {
            if (sourceChar == targetChars[targetIndex]) {
                targetIndex++;
                if (targetIndex == targetChars.length) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean containedByCharSequence(String str, String substr) {
        if (substr.length() > str.length()) {
            return false;
        }
        int j = 0;
        for (char c : str.toCharArray()) {
            if (c == substr.charAt(j)) {
                j++;
                if (j == substr.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean containedByCharSequence(Component c, String substr) {
        if (substr.length() > c.getString().length()) {
            return false;
        }
        int j = 0;
        for (char ch : c.getString().toCharArray()) {
            if (ch == substr.charAt(j)) {
                j++;
                if (j == substr.length()) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String removeFirstSpaces(String s) {
        if (s == null || s.isEmpty()) {
            return s;
        }

        int i = 0;
        while (i < s.length() && s.charAt(i) == ' ') {
            i++;
        }

        return s.substring(i);
    }

    /**
     * @param Seq = The sequence of characters
     * @param s = The string
     * @return Returns a string with the char sequence removed
     */
    public static String removeCharSequence (String Seq,String s) {
        char[] csq = Seq.toCharArray();
        String result = "";
        int CurrentInSequence = 0;
        boolean SequenceEnded = false;
        for (int i = 0;i < s.length();i++) {
            if (!SequenceEnded) {
                if (csq[CurrentInSequence] == s.charAt(i)) {
                    CurrentInSequence++;
                    if (CurrentInSequence == csq.length) SequenceEnded = true;
                    continue;
                }
            }
            result += String.valueOf(s.charAt(i));
        }
        return result;
    }

    // Keep style of the Component
    public static Component removeCharSequence(Component Seq, Component s) {
        String csq = Seq.getString();
        String result = "";
        int CurrentInSequence = 0;
        boolean SequenceEnded = false;
        for (int i = 0; i < s.getString().length(); i++) {
            if (!SequenceEnded) {
                if (csq.charAt(CurrentInSequence) == s.getString().charAt(i)) {
                    CurrentInSequence++;
                    if (CurrentInSequence == csq.length()) SequenceEnded = true;
                    continue;
                }
            }
            result += String.valueOf(s.getString().charAt(i));
        }
        return Component.literal(result).withStyle(s.getStyle());
    }

    public static Vector2f addVec(Vector2f a, Vector2f b) {
        return new Vector2f(a.x + b.x, a.y + b.y);
    }

    public static List<Vector2f> addVecListToList(List<Vector2f> vl1,List<Vector2f> vl2) {
        assert (vl1.size() < vl2.size());
        for (int i = 0;i < vl1.size();i++) {
            vl1.set(i,addVec(vl1.get(i), vl2.get(i)));
        }
        return vl1;
    }

    public static Vector2f scalarMultiply(Vector2f v, float s) {
        return new Vector2f(v.x * s, v.y * s);
    }

    public enum SegmentationOptions {

        TOTALLY_EXCLUSIVE, TOTALLY_INCLUSIVE, ALL_INSTANCES_RIGHT, ALL_INSTANCES_LEFT

    }

    public static String segmentString(String string, String symbol, char leftChar, char rightChar, int allowedInstancesL, int allowedInstancesR, SegmentationOptions... options) {
        boolean totallyExclusive = false, totallyInclusive = false, allInstancesR = false, allInstancesL = false;
        for (SegmentationOptions option : options) {
            if (option == SegmentationOptions.TOTALLY_EXCLUSIVE) totallyExclusive = true;
            if (option == SegmentationOptions.TOTALLY_INCLUSIVE) totallyInclusive = true;
            if (option == SegmentationOptions.ALL_INSTANCES_RIGHT) allInstancesR = true;
            if (option == SegmentationOptions.ALL_INSTANCES_LEFT) allInstancesL = true;
        }
        return segmentString(string, symbol, leftChar, rightChar, allowedInstancesL, allowedInstancesR, totallyExclusive, totallyInclusive, allInstancesR, allInstancesL);
    }

    /**
     * @param string            = The string you want to extract data from
     * @param symbol            = A string that will act as a pivot
     * @param leftChar          = It will copy all the character from the left of the pivot until it encounters this character
     * @param rightChar         = It will copy all the character from the right of the pivot until it encounters this character
     * @param allowedInstancesL = How many times can it encounter the left char before it stops copying the characters
     * @param allowedInstancesR = How many times can it encounter the right char before it stops copying the characters
     * @param totallyExclusive  = Makes so that the substring wont include the character from the left index
     * @return Returns the string that is defined by the bounds of leftChar and rightChar encountered allowedInstacesL  respectively allowedInctancesR - 1 within it
     * allowedInsracesL only if totallyExclusive = false else allowedInstacesL - 1
     */

    public static String segmentString(String string, String symbol, char leftChar, char rightChar, int allowedInstancesL, int allowedInstancesR, boolean totallyExclusive, boolean totallyInclusive, boolean allInstancesR, boolean allInstancesL) {

        int leftIdx = 0, rightIdx = 0;

        if (string.contains(symbol)) {

            int symbolIdx = string.indexOf(symbol);

            for (int i = 0; symbolIdx - i > -1; i++) {
                leftIdx = symbolIdx - i;
                if (string.charAt(symbolIdx - i) == leftChar) allowedInstancesL--;
                if (allowedInstancesL == 0) {
                    break;
                }
            }

            symbolIdx += symbol.length() - 1;

            for (int i = 0; symbolIdx + i < string.length(); i++) {
                rightIdx = symbolIdx + i;
                if (string.charAt(symbolIdx + i) == rightChar) allowedInstancesR--;
                if (allowedInstancesR == 0) {
                    break;
                }
            }

            if (allowedInstancesL != 0 && allInstancesL) return null;
            if (allowedInstancesR != 0 && allInstancesR) return null;
            return string.substring(leftIdx + (totallyExclusive ? 1 : 0), rightIdx + (totallyInclusive ? 1 : 0));
        } else {
            return null;
        }

    }

    public static String removeAllColourCodes(String s) {
        while (s.contains("§")) {
            s = s.replace("§" + s.charAt(s.indexOf("§") + 1), "");
        }
        return s;
    }

    public static void drawOutlineText(Minecraft mc, GuiGraphics guiGraphics, String text, int x, int y, int colour) {
        String noColorText = removeAllColourCodes(text);
        guiGraphics.drawString(mc.font, noColorText, x + 1, y, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, noColorText, x - 1, y, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, noColorText, x, y + 1, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, noColorText, x, y - 1, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, text, x, y, colour);
    }

    public static void drawOutlineText(Minecraft mc, GuiGraphics guiGraphics, Component text, int x, int y, int colour) {
        var copyOfText = setComponentColorDeep(text, ChatFormatting.BLACK);
        guiGraphics.drawString(mc.font, copyOfText, x + 1, y, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, copyOfText, x - 1, y, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, copyOfText, x, y + 1, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, copyOfText, x, y - 1, (colour >> 24) << 24);
        guiGraphics.drawString(mc.font, text, x, y, colour);
    }

    /**
     * Recursively sets the color of a Component and all its siblings to the given ChatFormatting color.
     */
    public static Component setComponentColorDeep(Component component, ChatFormatting color) {
        Component copy = component.copy();
        if (copy instanceof net.minecraft.network.chat.MutableComponent mutable) {
            mutable.setStyle(mutable.getStyle().withColor(color));
            for (int i = 0; i < mutable.getSiblings().size(); i++) {
                Component sib = mutable.getSiblings().get(i);
                Component coloredSib = setComponentColorDeep(sib, color);
                mutable.getSiblings().set(i, coloredSib);
            }
            return mutable;
        } else {
            return copy;
        }
    }

    public static void drawOutlineWrappedText(Minecraft mc, GuiGraphics guiGraphics, String text, int x, int y, int wordWrap, int colour) {
        FormattedText formattedText = FormattedText.of(text);
        guiGraphics.drawWordWrap(mc.font, formattedText, x + 1, y, wordWrap, (colour >> 24) << 24);
        guiGraphics.drawWordWrap(mc.font, formattedText, x - 1, y, wordWrap, (colour >> 24) << 24);
        guiGraphics.drawWordWrap(mc.font, formattedText, x, y + 1, wordWrap, (colour >> 24) << 24);
        guiGraphics.drawWordWrap(mc.font, formattedText, x, y - 1, wordWrap, (colour >> 24) << 24);
        guiGraphics.drawWordWrap(mc.font, formattedText, x, y, wordWrap, colour);
    }

    public static void drawWrappedText(GuiGraphics guiGraphics, String text, int x, int y, int wordWrap, int colour) {
        FormattedText formattedText = FormattedText.of(text);
        guiGraphics.drawWordWrap(Minecraft.getInstance().font, formattedText, x, y, wordWrap, colour);
    }

    /**
     * @param string = Input message
     * @brief Shown the specified message in the chat if debug messages are on
     */

    public static void showMessage(String string) {
        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.SHOW_DEBUG_MESSAGES))
            Minecraft.getInstance().player.displayClientMessage(Component.literal(string), false);
    }

    public static void showNonDebugMessage(String string) {
        Minecraft.getInstance().player.displayClientMessage(Component.literal(string), false);
    }

    // A wise man once said bubble sort is good enough when there are not a lot of elements
    public static <T> void bubbleSort(List<Integer> arr, List<T> s) {
        int n = arr.size();
        for (int i = 0; i < n - 1; i++)
            for (int j = 0; j < n - i - 1; j++)
                if (arr.get(j) < arr.get(j + 1)) {
                    int temp = arr.get(j);
                    arr.set(j, arr.get(j + 1));
                    arr.set(j + 1, temp);

                    T _temp = s.get(j);
                    s.set(j, s.get(j + 1));
                    s.set(j + 1, _temp);
                }
    }

    /**
     * Recursively removes all components (and their siblings) whose getString() contains the given substring.
     * Returns a new component with the same structure, minus the removed parts.
     */
    public static Component removeComponentContaining(Component s, String substring) {
        if (s.getString().contains(substring) && s.getSiblings().isEmpty()) {
            return null;
        }
        if (s instanceof net.minecraft.network.chat.MutableComponent mutable) {
            net.minecraft.network.chat.MutableComponent copy = mutable.copy();
            copy.getSiblings().clear();
            for (Component sib : mutable.getSiblings()) {
                Component cleaned = removeComponentContaining(sib, substring);
                if (cleaned != null) copy.append(cleaned);
            }
            return copy;
        }
        return s;
    }


}
