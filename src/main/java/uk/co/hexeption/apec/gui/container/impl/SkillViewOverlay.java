package uk.co.hexeption.apec.gui.container.impl;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.util.CommonColors;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Blocks;
import uk.co.hexeption.apec.gui.container.ContainerGuiOverlay;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class SkillViewOverlay implements ContainerGuiOverlay {

    public enum Actions {
        PAGE_CHANGE,
        BACK,
        CLOSE,
        OPEN_INFO,
        BESTIARY,
        SLAYER,
        BAIT,
        SEA_CREATURE_GUIDE,
        ROD_PARTS
    }

    private static final int[] SKILL_XP_CONTAINER_SLOTS = {
            0, 9, 18, 27, 28, 29, 20, 11, 2, 3, 4, 13, 22, 31, 32, 33, 24, 15, 6, 7, 8, 17, 26, 35, 44, 53
    };

    private boolean userIsDragging = false;
    private float initialMouseX = 0;
    private int xSliderValue = 0;
    private int tempSlideValue = 0;
    private boolean firstSetxSlider = true;
    private SlotClickCallback slotClickCallback;

    public void setSlotClickCallback(SlotClickCallback callback) {

        this.slotClickCallback = callback;
    }

    @FunctionalInterface
    public interface SlotClickCallback {

        void clickSlot(int slotIndex);

    }

    @Override
    public boolean matchesTitle(Component title) {

        if (title == null) return false;
        String titleText = title.getString();

        if (titleText.contains("Garden Levels")) return true;

        if (!titleText.contains("Skill")) return false;

        return titleText.contains("Farming") ||
                titleText.contains("Mining") ||
                titleText.contains("Combat") ||
                titleText.contains("Foraging") ||
                titleText.contains("Fishing") ||
                titleText.contains("Enchanting") ||
                titleText.contains("Alchemy") ||
                titleText.contains("Carpentry") ||
                titleText.contains("Runecrafting") ||
                titleText.contains("Taming") ||
                titleText.contains("Social") ||
                titleText.contains("Hunting");
    }

    @Override
    public void render(GuiGraphics g, int mouseX, int mouseY, float delta,
            int left, int top, int width, int height,
            Minecraft mc,
            AbstractContainerMenu menu,
            List<Slot> slots) {

        int scaledWidth = mc.getWindow().getGuiScaledWidth();
        int scaledHeight = mc.getWindow().getGuiScaledHeight();

        g.fillGradient(0, 0, scaledWidth, scaledHeight, -1072689136, -804253680);

        try {
            if (slots.size() > SKILL_XP_CONTAINER_SLOTS[0]) {
                ItemStack introStack = slots.get(SKILL_XP_CONTAINER_SLOTS[0]).getItem();
                if (!introStack.isEmpty()) {
                    renderSkillBoxes(g, mc, slots, scaledWidth, scaledHeight, mouseX);
                    renderInfoText(g, mc, slots, scaledWidth);
                }
            }
        } catch (Exception e) {
        }

        renderButtons(g, mc, slots, scaledWidth, scaledHeight);
    }

    private void renderSkillBoxes(GuiGraphics g, Minecraft mc, List<Slot> slots, int scaledWidth, int scaledHeight, int mouseX) {

        int totalWidth = 15;

        for (int k = 0; k < 25 && k + 1 < SKILL_XP_CONTAINER_SLOTS.length; k++) {
            if (SKILL_XP_CONTAINER_SLOTS[k + 1] >= slots.size()) continue;

            ItemStack stack = slots.get(SKILL_XP_CONTAINER_SLOTS[k + 1]).getItem();
            if (stack.isEmpty()) continue;

            List<Component> tooltipComponents = getTooltipComponents(stack);
            int boxHeight = tooltipComponents.size() * 10;
            int boxWidth = getMaxComponentWidth(tooltipComponents, mc);

            int boxX = totalWidth + xSliderValue + tempSlideValue;
            int boxY = scaledHeight / 2 - boxHeight / 2;

            g.fill(boxX, boxY, boxX + boxWidth, boxY + boxHeight, 0xaa353535);

            if (containsText(tooltipComponents, "Progress") && firstSetxSlider) {
                firstSetxSlider = false;
            }

            for (int l = 0; l < tooltipComponents.size(); l++) {
                g.drawString(mc.font, tooltipComponents.get(l), boxX, boxY + 1 + 10 * l, CommonColors.WHITE, false);
            }

            totalWidth += boxWidth + 15;
        }

        handleSliderLogic(totalWidth, scaledWidth, mouseX);
    }

    private void handleSliderLogic(int totalWidth, int scaledWidth, int mouseX) {

        if (firstSetxSlider) {
            firstSetxSlider = false;
        }

        if (userIsDragging) {
            tempSlideValue = mouseX - (int) initialMouseX;
            constrainSlider(totalWidth, scaledWidth);
        } else {
            constrainSlider(totalWidth, scaledWidth);
            xSliderValue += tempSlideValue;
            tempSlideValue = 0;
        }
    }

    private void constrainSlider(int totalWidth, int scaledWidth) {

        if (tempSlideValue + xSliderValue > 0) {
            tempSlideValue = 0;
            xSliderValue = 0;
        }
        if (tempSlideValue + xSliderValue < -totalWidth + scaledWidth - 15) {
            tempSlideValue = 0;
            xSliderValue = -totalWidth + scaledWidth - 15;
        }
    }

    private void renderInfoText(GuiGraphics g, Minecraft mc, List<Slot> slots, int scaledWidth) {

        ItemStack introStack = slots.get(SKILL_XP_CONTAINER_SLOTS[0]).getItem();
        List<Component> introLines = getTooltipComponents(introStack);
        for (int k = 0; k < introLines.size(); k++) {
            g.drawString(mc.font, introLines.get(k), 1, 1 + 10 * k, CommonColors.WHITE, false);
        }

        renderTopRightInfo(g, mc, slots, scaledWidth);
    }

    private void renderTopRightInfo(GuiGraphics g, Minecraft mc, List<Slot> slots, int scaledWidth) {

        if (slots.size() > 51) {
            ItemStack petStack = slots.get(51).getItem();
            if (!petStack.isEmpty() && !petStack.is(Blocks.GRAY_STAINED_GLASS_PANE.asItem())) {
                List<Component> petLines = getTooltipComponents(petStack);
                renderRightAlignedText(g, mc, petLines, scaledWidth, 16);
                return;
            }
        }

        if (slots.size() > 45) {
            ItemStack farmingStack = slots.get(45).getItem();
            if (!farmingStack.isEmpty() && !farmingStack.is(Blocks.GRAY_STAINED_GLASS_PANE.asItem())) {
                List<Component> farmingLines = getTooltipComponents(farmingStack);
                renderRightAlignedText(g, mc, farmingLines, scaledWidth, 16);
            }
        }
    }

    private void renderRightAlignedText(GuiGraphics g, Minecraft mc, List<Component> lines, int scaledWidth, int startY) {

        for (int k = 0; k < lines.size(); k++) {
            int textWidth = mc.font.width(lines.get(k));
            g.drawString(mc.font, lines.get(k), scaledWidth - 1 - textWidth, startY + 10 * k, CommonColors.WHITE, false);
        }
    }

    private void renderButtons(GuiGraphics g, Minecraft mc, List<Slot> slots, int scaledWidth, int scaledHeight) {

        drawButton(g, mc, scaledWidth - 30, 0, 15, 15, Actions.BACK, slots);
        drawButton(g, mc, scaledWidth - 15, 0, 15, 15, Actions.CLOSE, slots);
        drawButton(g, mc, 0, scaledHeight - 30, 120, 30, Actions.PAGE_CHANGE, slots);

        if (isFishingSkill(slots)) {
            drawButton(g, mc, scaledWidth - 120, scaledHeight - 30, 120, 30, Actions.BAIT, slots);
            drawButton(g, mc, scaledWidth - 120, scaledHeight - 60, 120, 30, Actions.SEA_CREATURE_GUIDE, slots);
            drawButton(g, mc, scaledWidth - 120, scaledHeight - 90, 120, 30, Actions.ROD_PARTS, slots);
        } else if (isCombatSkill(slots)) {
            drawButton(g, mc, scaledWidth - 120, scaledHeight - 30, 120, 30, Actions.BESTIARY, slots);
            drawButton(g, mc, scaledWidth - 120, scaledHeight - 60, 120, 30, Actions.SLAYER, slots);
        } else {
            drawButton(g, mc, scaledWidth - 120, scaledHeight - 30, 120, 30, Actions.OPEN_INFO, slots);
        }
    }

    public void executeAction(Actions action, List<Slot> slots) {

        switch (action) {
            case CLOSE -> clickSlot(49);
            case BACK -> clickSlot(48);
            case PAGE_CHANGE -> {
                if (slots.size() > 45 && slots.get(45).getItem().is(Items.ARROW)) {
                    clickSlot(45);
                } else if (slots.size() > 50) {
                    clickSlot(50);
                }
            }
            case OPEN_INFO -> clickSlot(40);
            case BESTIARY -> clickSlot(39);
            case SLAYER -> clickSlot(41);
            case BAIT -> clickSlot(39);
            case SEA_CREATURE_GUIDE -> clickSlot(40);
            case ROD_PARTS -> clickSlot(41);
        }
    }

    @Override
    public boolean mouseClicked(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, int button) {

        if (button != 0) return false;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();

        if (isInBounds(mouseX, mouseY, scaledWidth - 30, 0, 15, 15)) {
            executeAction(Actions.BACK, slots);
            return true;
        }
        if (isInBounds(mouseX, mouseY, scaledWidth - 15, 0, 15, 15)) {
            executeAction(Actions.CLOSE, slots);
            return true;
        }
        if (isInBounds(mouseX, mouseY, 0, scaledHeight - 30, 120, 30)) {
            executeAction(Actions.PAGE_CHANGE, slots);
            return true;
        }

        if (isCombatSkill(slots)) {
            if (isInBounds(mouseX, mouseY, scaledWidth - 120, scaledHeight - 30, 120, 30)) {
                executeAction(Actions.BESTIARY, slots);
                return true;
            }
            if (isInBounds(mouseX, mouseY, scaledWidth - 120, scaledHeight - 60, 120, 30)) {
                executeAction(Actions.SLAYER, slots);
                return true;
            }
        } else {
            if (isInBounds(mouseX, mouseY, scaledWidth - 120, scaledHeight - 30, 120, 30)) {
                executeAction(Actions.OPEN_INFO, slots);
                return true;
            }
        }

        if (isFishingSkill(slots)) {
            if (isInBounds(mouseX, mouseY, scaledWidth - 120, scaledHeight - 30, 120, 30)) {
                executeAction(Actions.BAIT, slots);
                return true;
            }
            if (isInBounds(mouseX, mouseY, scaledWidth - 120, scaledHeight - 60, 120, 30)) {
                executeAction(Actions.SEA_CREATURE_GUIDE, slots);
                return true;
            }
            if (isInBounds(mouseX, mouseY, scaledWidth - 120, scaledHeight - 90, 120, 30)) {
                executeAction(Actions.ROD_PARTS, slots);
                return true;
            }
        }

        userIsDragging = true;
        initialMouseX = (float) mouseX;
        return false;
    }

    @Override
    public boolean mouseReleased(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, int button) {

        if (button == 0) {
            userIsDragging = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, double scrollX, double scrollY) {

        xSliderValue += scrollY > 0 ? 20 : -20;
        return true;
    }

    private void drawButton(GuiGraphics g, Minecraft mc, int x, int y, int width, int height, Actions action, List<Slot> slots) {

        boolean isHovered = isInBounds(mc.mouseHandler.xpos() * mc.getWindow().getGuiScaledWidth() / mc.getWindow().getScreenWidth(),
                mc.mouseHandler.ypos() * mc.getWindow().getGuiScaledHeight() / mc.getWindow().getScreenHeight(),
                x, y, width, height);

        ApecTextures statusBarTexture = ApecTextures.ICONS;

        switch (action) {
            case BACK -> {
                g.blit(GuiGraphicsUtils.getGuiTextured(), statusBarTexture.getIdentifier(),
                        x, y, 33, 240, 15, 15, statusBarTexture.getWidth(), statusBarTexture.getHeight());
            }
            case CLOSE -> {
                g.blit(GuiGraphicsUtils.getGuiTextured(), statusBarTexture.getIdentifier(),
                        x, y, 49, 240, 15, 15, statusBarTexture.getWidth(), statusBarTexture.getHeight());
            }
            case PAGE_CHANGE -> {
                drawButtonText(g, mc, getPageText(slots), x, y, false);
            }
            case OPEN_INFO -> {
                drawButtonText(g, mc, getOtherInfoText(slots), x, y, width, true);
            }
            case BESTIARY -> {
                drawButtonText(g, mc, getBestiaryText(slots), x, y, width, true);
            }
            case SLAYER -> {
                drawButtonText(g, mc, getSlayerText(slots), x, y, width, true);
            }
            case BAIT -> {
                drawButtonText(g, mc, getBaitText(slots), x, y, width, true);
            }
            case SEA_CREATURE_GUIDE -> {
                drawButtonText(g, mc, getSeaCreatureGuideText(slots), x, y, width, true);
            }
            case ROD_PARTS -> {
                drawButtonText(g, mc, getRodPartsText(slots), x, y, width, true);
            }
        }

        if (isHovered) {
            g.fill(x, y, x + width, y + height, 0x1adddddd);
        }
    }

    private void drawButtonText(GuiGraphics g, Minecraft mc, List<Component> lines, int x, int y, boolean rightAlign) {

        drawButtonText(g, mc, lines, x, y, 0, rightAlign);
    }

    private void drawButtonText(GuiGraphics g, Minecraft mc, List<Component> lines, int x, int y, int width, boolean rightAlign) {

        for (int i = 0; i < lines.size(); i++) {
            int textX = rightAlign ? x + width - 5 - mc.font.width(lines.get(i)) : x + 5;
            g.drawString(mc.font, lines.get(i), textX, y + 5 + i * 10, CommonColors.WHITE, false);
        }
    }

    private List<Component> getPageText(List<Slot> slots) {

        try {
            if (slots.size() > 45 && slots.get(45).getItem().is(Items.ARROW)) {
                return getTooltipComponents(slots.get(45).getItem());
            }
            if (slots.size() > 50) {
                return getTooltipComponents(slots.get(50).getItem());
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    private List<Component> getOtherInfoText(List<Slot> slots) {

        return getFirstAndLastLine(slots, 40);
    }

    private List<Component> getBestiaryText(List<Slot> slots) {

        return getFirstAndLastLine(slots, 39);
    }

    private List<Component> getSlayerText(List<Slot> slots) {

        try {
            if (slots.size() > 41 && !slots.get(41).getItem().is(Blocks.GRAY_STAINED_GLASS_PANE.asItem())) {
                List<Component> allText = getTooltipComponents(slots.get(41).getItem());
                if (!allText.isEmpty()) {
                    List<Component> result = new ArrayList<>();
                    result.add(allText.getFirst());
                    result.add(Component.literal("§eClick to view!"));
                    return result;
                }
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    private List<Component> getBaitText(List<Slot> slots) {

        return getFirstAndLastLine(slots, 39);
    }

    private List<Component> getSeaCreatureGuideText(List<Slot> slots) {

        return getFirstAndLastLine(slots, 40);
    }

    private List<Component> getRodPartsText(List<Slot> slots) {

        return getFirstAndLastLine(slots, 41);
    }

    private List<Component> getFirstAndLastLine(List<Slot> slots, int slotIndex) {

        try {
            if (slots.size() > slotIndex && !slots.get(slotIndex).getItem().is(Blocks.GRAY_STAINED_GLASS_PANE.asItem())) {
                List<Component> allText = getTooltipComponents(slots.get(slotIndex).getItem());
                if (!allText.isEmpty()) {
                    List<Component> result = new ArrayList<>();
                    result.add(allText.getFirst());
                    if (allText.size() > 1) {
                        result.add(allText.getLast());
                    }
                    return result;
                }
            }
        } catch (Exception ignored) {}
        return new ArrayList<>();
    }

    private void clickSlot(int slotIndex) {

        if (slotClickCallback != null) {
            slotClickCallback.clickSlot(slotIndex);
        }
    }

    private List<Component> getTooltipComponents(ItemStack stack) {

        List<Component> lines = new ArrayList<>();
        lines.add(stack.getHoverName());

        ItemLore lore = stack.get(DataComponents.LORE);
        if (lore != null) {
            lines.addAll(lore.lines());
        }

        return lines;
    }

    private int getMaxComponentWidth(List<Component> lines, Minecraft mc) {

        return lines.stream()
                .mapToInt(mc.font::width)
                .max()
                .orElse(0);
    }

    private boolean containsText(List<Component> lines, String pattern) {

        return lines.stream()
                .anyMatch(line -> line.getString().contains(pattern));
    }

    private boolean isInBounds(double mouseX, double mouseY, int x, int y, int width, int height) {

        return mouseX >= x && mouseX < x + width && mouseY >= y && mouseY < y + height;
    }

    private boolean isCombatSkill(List<Slot> slots) {

        try {
            if (slots.size() > SKILL_XP_CONTAINER_SLOTS[0]) {
                ItemStack introStack = slots.get(SKILL_XP_CONTAINER_SLOTS[0]).getItem();
                if (!introStack.isEmpty()) {
                    List<Component> tooltipComponents = getTooltipComponents(introStack);
                    return tooltipComponents.stream()
                            .anyMatch(line -> {
                                String text = line.getString();
                                return text.contains("Combat") || text.contains("Mining") || text.contains("Hunting");
                            });
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

    private boolean isFishingSkill(List<Slot> slots) {

        try {
            if (slots.size() > SKILL_XP_CONTAINER_SLOTS[0]) {
                ItemStack introStack = slots.get(SKILL_XP_CONTAINER_SLOTS[0]).getItem();
                if (!introStack.isEmpty()) {
                    List<Component> tooltipComponents = getTooltipComponents(introStack);
                    return tooltipComponents.stream()
                            .anyMatch(line -> {
                                String text = line.getString();
                                return text.contains("Fishing");
                            });
                }
            }
        } catch (Exception ignored) {}
        return false;
    }

}
