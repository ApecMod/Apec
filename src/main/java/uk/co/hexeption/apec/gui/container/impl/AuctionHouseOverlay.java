package uk.co.hexeption.apec.gui.container.impl;

//? if 1.21.5 {
/*import net.minecraft.client.gui.screens.Screen;
import net.minecraft.util.FormattedCharSequence;
*///?}

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
import net.minecraft.world.item.component.ItemLore;
import uk.co.hexeption.apec.gui.container.ContainerGuiOverlay;
import uk.co.hexeption.apec.hud.ApecTextures;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class AuctionHouseOverlay implements ContainerGuiOverlay {

    private SlotClickCallback slotClickCallback;

    private CategoryID currentCategory = CategoryID.WEAPONS;
    private String searchTerm = "";
    private final List<Component> sortList = new ArrayList<>();
    private final List<Component> modeList = new ArrayList<>();
    private final List<Component> rarityList = new ArrayList<>();
    private String pageText = "";
    private boolean initialCategorySet = false;

    public enum CategoryID {
        WEAPONS,
        ARMOUR,
        ACCESSORIES,
        CONSUMABLES,
        BLOCKS,
        OTHER
    }

    public enum Actions {
        SEARCH,
        NEXT,
        BACK,
        RARITY_CHANGE,
        SORT_CHANGE,
        MOD_CHANGE,
        CLOSE
    }

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

        boolean isInOtherMenu = titleText.contains("View") || titleText.contains("Confirm");
        return titleText.startsWith("Auctions") && !isInOtherMenu;
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
            extractSearchTerm();
            updateInfoFromSlots(slots);
            setInitialCategoryFromSlots(slots);
            renderAuctionInterface(g, mc, slots, scaledWidth, scaledHeight, mouseX, mouseY);
        } catch (Exception e) {
            renderFallback(g, mc, scaledWidth, scaledHeight);
        }
    }

    private void renderAuctionInterface(GuiGraphics g, Minecraft mc, List<Slot> slots,
            int scaledWidth, int scaledHeight, int mouseX, int mouseY) {

        int centerX = scaledWidth / 2;
        int centerY = scaledHeight / 2;

        g.fill(centerX - 100, centerY - 100, centerX + 100, centerY + 70, 0xAA353535);
        g.fill(centerX + 110, centerY - 100, centerX + 190, centerY + 70, 0xAA353535);
        g.fill(centerX + 110, centerY - 30, centerX + 190, centerY + 40, 0xAA353535);
        g.fill(centerX + 200, centerY - 100, centerX + 280, centerY + 70, 0xAA353535);

        renderHeader(g, mc, centerX, centerY);
        renderCategoryButtons(g, mc, centerX, centerY, mouseX, mouseY);
        renderActionButtons(g, mc, centerX, centerY, mouseX, mouseY);
        renderInfoPanels(g, mc, centerX, centerY);
        renderAuctionItems(g, mc, slots, centerX, centerY, mouseX, mouseY);
    }

    private void renderHeader(GuiGraphics g, Minecraft mc, int centerX, int centerY) {

        String title = "Auction Browser" + pageText.replace("(", " ").replace(")", "");
        g.drawString(mc.font, title, centerX - 97, centerY - 97, CommonColors.WHITE, false);

        if (!searchTerm.isEmpty()) {
            String searchDisplay = "Search: \"" + searchTerm + "\"";
            g.drawString(mc.font, searchDisplay, centerX - 97, centerY - 87, CommonColors.WHITE, false);
        }
    }

    private void renderCategoryButtons(GuiGraphics g, Minecraft mc, int centerX, int centerY, int mouseX, int mouseY) {

        String[] categories = { "Weapons", "Armour", "Accessories", "Consumables", "Blocks", "Tools & Misc" };
        CategoryID[] categoryIDs = CategoryID.values();

        for (int i = 0; i < categories.length; i++) {
            int buttonX = centerX - 200;
            int buttonY = centerY - 80 + 54 + (i * 16);
            int buttonWidth = 100;
            int buttonHeight = 16;

            boolean isSelected = currentCategory == categoryIDs[i];
            boolean isHovered = mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight;

            if (isSelected) {
                g.fill(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, 0xAA353535);
            } else if (isHovered) {
                g.fill(buttonX, buttonY, buttonX + buttonWidth, buttonY + buttonHeight, 0x1ADDDDDD);
            }

            int textX = buttonX + buttonWidth / 2;
            int textY = buttonY + (buttonHeight - 8) / 2;
            g.drawCenteredString(mc.font, categories[i], textX, textY, CommonColors.WHITE);
        }
    }

    private void renderActionButtons(GuiGraphics g, Minecraft mc, int centerX, int centerY, int mouseX, int mouseY) {

        ApecTextures iconsTexture = ApecTextures.ICONS;

        int[][] buttonDefs = {
                { centerX - 99, centerY + 54, 1 },
                { centerX + 85, centerY + 54, 17 },
                { centerX + 70, centerY + 54, 33 },
                { centerX + 85, centerY - 100, 49 }
        };

        for (int[] def : buttonDefs) {
            int buttonX = def[0];
            int buttonY = def[1];
            int textureX = def[2];

            boolean isHovered = mouseX >= buttonX && mouseX <= buttonX + 15 &&
                    mouseY >= buttonY && mouseY <= buttonY + 15;

            g.blit(GuiGraphicsUtils.getGuiTextured(), iconsTexture.getIdentifier(),
                    buttonX, buttonY, textureX, 240, 15, 15,
                    iconsTexture.getWidth(), iconsTexture.getHeight());

            if (isHovered) {
                g.fill(buttonX, buttonY, buttonX + 15, buttonY + 15, 0x1ADDDDDD);
            }
        }

        renderLargeActionArea(g, centerX + 110, centerY - 100, mouseX, mouseY);
        renderLargeActionArea(g, centerX + 110, centerY - 30, mouseX, mouseY);
        renderLargeActionArea(g, centerX + 200, centerY - 100, mouseX, mouseY);
    }

    private void renderLargeActionArea(GuiGraphics g, int x, int y, int mouseX, int mouseY) {

        boolean isHovered = mouseX >= x && mouseX <= x + 80 && mouseY >= y && mouseY <= y + 70;

        if (isHovered) {
            g.fill(x, y, x + 80, y + 70, 0x1ADDDDDD);
        }
    }

    private void renderInfoPanels(GuiGraphics g, Minecraft mc, int centerX, int centerY) {

        for (int i = 0; i < Math.min(sortList.size(), 6); i++) {
            g.drawString(mc.font, sortList.get(i), centerX + 113, centerY - 97 + 10 * i, CommonColors.WHITE, false);
        }

        for (int i = 0; i < Math.min(modeList.size(), 5); i++) {
            g.drawString(mc.font, modeList.get(i), centerX + 113, centerY - 27 + 10 * i, CommonColors.WHITE, false);
        }

        for (int i = 0; i < Math.min(rarityList.size(), 11); i++) {
            g.drawString(mc.font, rarityList.get(i), centerX + 203, centerY - 97 + 10 * i, CommonColors.WHITE, false);
        }
    }

    private void renderAuctionItems(GuiGraphics g, Minecraft mc, List<Slot> slots,
            int centerX, int centerY, int mouseX, int mouseY) {

        // Auction item slots: 11-16, 20-25, 29-34, 38-43
        int[] slotRanges = { 11, 20, 29, 38 };

        int itemsPerRow = 6;
        int itemSpacing = 18;
        int gridWidth = (itemsPerRow - 1) * itemSpacing;

        int gridStartX = centerX - (gridWidth / 2);
        int gridStartY = centerY - 50;

        for (int row = 0; row < 4; row++) {
            int startSlot = slotRanges[row];
            for (int col = 0; col < 6; col++) {
                int actualSlot = startSlot + col;
                if (actualSlot < slots.size()) {
                    Slot slot = slots.get(actualSlot);
                    ItemStack item = slot.getItem();

                    if (!item.isEmpty()) {
                        int baseX = gridStartX + (col * itemSpacing);
                        int baseY = gridStartY + (row * itemSpacing);

                        float expansion = (col - 2.5f) * 4f;
                        int itemX = (int) (baseX + expansion);
                        int itemY = baseY;

                        g.renderItem(item, itemX, itemY);
                        g.renderItemDecorations(mc.font, item, itemX, itemY);

                        boolean isHovered = mouseX >= itemX && mouseX <= itemX + 16 &&
                                mouseY >= itemY && mouseY <= itemY + 16;

                        if (isHovered) {
                            g.fill(itemX, itemY, itemX + 16, itemY + 16, 0x80FFFFFF);
                            //? if >= 1.21.7 {
                            g.setTooltipForNextFrame(mc.font, item, mouseX, mouseY);
                            //?} else {
                            /*List<Component> tooltip = Screen.getTooltipFromItem(mc, item);
                            List<FormattedCharSequence> tooltipText = new ArrayList<>();
                            for (Component line : tooltip) {
                                tooltipText.add(line.getVisualOrderText());
                            }

                            mc.screen.setTooltipForNextRenderPass(tooltipText);
                            *///?}

                        }
                    }
                }
            }
        }
    }

    private void extractSearchTerm() {

        try {
            if (Minecraft.getInstance().screen != null) {
                Component title = Minecraft.getInstance().screen.getTitle();
                String titleText = title.getString();

                if (titleText.contains("\"")) {
                    String[] parts = titleText.split("\"");
                    if (parts.length > 1) {
                        searchTerm = parts[1];
                        return;
                    }
                }
            }
            searchTerm = "";

        } catch (Exception e) {
            searchTerm = "";
        }
    }

    private void updateInfoFromSlots(List<Slot> slots) {

        try {
            if (slots.size() > 50 && !slots.get(50).getItem().isEmpty()) {
                List<Component> tooltip = getTooltipComponents(slots.get(50).getItem());
                sortList.clear();
                for (int i = 0; i < Math.min(tooltip.size(), 6); i++) {
                    sortList.add(tooltip.get(i));
                }
            }

            if (slots.size() > 52 && !slots.get(52).getItem().isEmpty()) {
                List<Component> tooltip = getTooltipComponents(slots.get(52).getItem());
                modeList.clear();
                for (int i = 0; i < Math.min(tooltip.size(), 5); i++) {
                    modeList.add(tooltip.get(i));
                }
            }

            if (slots.size() > 51 && !slots.get(51).getItem().isEmpty()) {
                List<Component> tooltip = getTooltipComponents(slots.get(51).getItem());
                rarityList.clear();
                for (int i = 0; i < Math.min(tooltip.size(), 11); i++) {
                    rarityList.add(tooltip.get(i));
                }
            }

            if (slots.size() > 53 && !slots.get(53).getItem().isEmpty()) {
                List<Component> tooltip = getTooltipComponents(slots.get(53).getItem());
                if (tooltip.size() > 1) {
                    pageText = tooltip.get(1).getString();
                }
            }
        } catch (Exception e) {
        }
    }

    private void setInitialCategoryFromSlots(List<Slot> slots) {

        if (!initialCategorySet && slots.size() > 1 && !slots.get(1).getItem().isEmpty()) {
            ItemStack item = slots.get(1).getItem();
            String itemName = item.getItem().toString();

            if (itemName.contains("orange")) {
                currentCategory = CategoryID.WEAPONS;
            } else if (itemName.contains("blue")) {
                currentCategory = CategoryID.ARMOUR;
            } else if (itemName.contains("green")) {
                currentCategory = CategoryID.ACCESSORIES;
            } else if (itemName.contains("red")) {
                currentCategory = CategoryID.CONSUMABLES;
            } else if (itemName.contains("brown")) {
                currentCategory = CategoryID.BLOCKS;
            } else if (itemName.contains("purple")) {
                currentCategory = CategoryID.OTHER;
            }

            initialCategorySet = true;
        }
    }

    @Override
    public boolean mouseClicked(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, int button) {

        if (button != 0 && button != 1) return false;

        int scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
        int scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
        int centerX = scaledWidth / 2;
        int centerY = scaledHeight / 2;

        if (handleCategoryButtonClick(mouseX, mouseY, centerX, centerY, slots)) {
            return true;
        }

        if (handleActionButtonClick(mouseX, mouseY, centerX, centerY, button, slots)) {
            return true;
        }

        return handleAuctionItemClick(mouseX, mouseY, centerX, centerY, slots);
    }

    private boolean handleCategoryButtonClick(double mouseX, double mouseY, int centerX, int centerY, List<Slot> slots) {

        CategoryID[] categories = CategoryID.values();

        for (int i = 0; i < categories.length; i++) {
            int buttonX = centerX - 200;
            int buttonY = centerY - 80 + 54 + (i * 16);
            int buttonWidth = 100;
            int buttonHeight = 16;

            if (mouseX >= buttonX && mouseX <= buttonX + buttonWidth &&
                    mouseY >= buttonY && mouseY <= buttonY + buttonHeight) {

                if (currentCategory != categories[i]) {
                    currentCategory = categories[i];
                    int slotIndex = categories[i].ordinal() * 9;
                    if (slotIndex < slots.size()) {
                        clickSlot(slotIndex);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private boolean handleActionButtonClick(double mouseX, double mouseY, int centerX, int centerY, int button, List<Slot> slots) {

        int[][] buttonDefs = {
                { centerX - 99, centerY + 54, 48, 47 },  // Search (left/right click different slots)
                { centerX + 85, centerY + 54, 53, 53 },  // Next
                { centerX + 70, centerY + 54, 46, 46 },  // Back
                { centerX + 85, centerY - 100, 49, 49 }  // Close
        };

        for (int[] def : buttonDefs) {
            if (mouseX >= def[0] && mouseX <= def[0] + 15 && mouseY >= def[1] && mouseY <= def[1] + 15) {
                int slotIndex = (button == 0) ? def[2] : def[3];
                if (slotIndex < slots.size()) {
                    clickSlot(slotIndex);
                }
                return true;
            }
        }

        if (mouseX >= centerX + 110 && mouseX <= centerX + 190) {
            if (mouseY >= centerY - 100 && mouseY <= centerY - 30) {
                clickSlot(50);
                return true;
            } else if (mouseY >= centerY - 30 && mouseY <= centerY + 40) {
                clickSlot(52);
                return true;
            }
        }

        if (mouseX >= centerX + 200 && mouseX <= centerX + 280 &&
                mouseY >= centerY - 100 && mouseY <= centerY + 20) {
            clickSlot(51);
            return true;
        }

        return false;
    }

    private boolean handleAuctionItemClick(double mouseX, double mouseY, int centerX, int centerY, List<Slot> slots) {

        int[] slotRanges = { 11, 20, 29, 38 };

        int itemsPerRow = 6;
        int rows = 4;
        int itemSpacing = 18;
        int gridWidth = (itemsPerRow - 1) * itemSpacing;
        int gridStartX = centerX - (gridWidth / 2);
        int gridStartY = centerY - 50;

        for (int row = 0; row < 4; row++) {
            int startSlot = slotRanges[row];
            for (int col = 0; col < 6; col++) {
                int actualSlot = startSlot + col;
                if (actualSlot < slots.size()) {
                    int baseX = gridStartX + (col * itemSpacing);
                    int baseY = gridStartY + (row * itemSpacing);

                    float expansion = (col - 2.5f) * 4f;
                    int itemX = (int) (baseX + expansion);
                    int itemY = baseY;

                    if (mouseX >= itemX && mouseX <= itemX + 16 &&
                            mouseY >= itemY && mouseY <= itemY + 16) {
                        clickSlot(actualSlot);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void renderFallback(GuiGraphics g, Minecraft mc, int scaledWidth, int scaledHeight) {

        String fallbackText = "Auction House Loading...";
        int textWidth = mc.font.width(fallbackText);
        int textX = (scaledWidth - textWidth) / 2;
        int textY = scaledHeight / 2;
        g.drawString(mc.font, fallbackText, textX, textY, CommonColors.WHITE, true);
    }

    private void clickSlot(int slotIndex) {

        if (slotClickCallback != null) {
            slotClickCallback.clickSlot(slotIndex);
        }
    }

    private List<Component> getTooltipComponents(ItemStack item) {

        if (item.isEmpty()) return new ArrayList<>();

        List<Component> tooltip = new ArrayList<>();
        tooltip.add(item.getHoverName());

        ItemLore lore = item.get(DataComponents.LORE);
        if (lore != null) {
            tooltip.addAll(lore.lines());
        }

        return tooltip;
    }

}
