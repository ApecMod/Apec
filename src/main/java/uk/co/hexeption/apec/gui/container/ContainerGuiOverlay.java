package uk.co.hexeption.apec.gui.container;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;

public interface ContainerGuiOverlay {

    boolean matchesTitle(Component title);

    void render(GuiGraphics g, int mouseX, int mouseY, float delta,
            int left, int top, int width, int height,
            Minecraft mc,
            AbstractContainerMenu menu,
            List<Slot> slots);

    default boolean mouseClicked(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, int button) {return false;}

    default boolean mouseReleased(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, int button) {return false;}

    default boolean mouseScrolled(AbstractContainerMenu menu, List<Slot> slots, double mouseX, double mouseY, double scrollX, double scrollY) {return false;}

    default boolean keyPressed(AbstractContainerMenu menu, List<Slot> slots, int keyCode, int scanCode, int modifiers) {return false;}

}
