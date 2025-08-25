package uk.co.hexeption.apec.mixins.container;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.gui.container.ContainerGuiManager;
import uk.co.hexeption.apec.gui.container.ContainerGuiOverlay;
import uk.co.hexeption.apec.gui.container.impl.SkillViewOverlay;
import uk.co.hexeption.apec.settings.SettingID;

@Mixin(AbstractContainerScreen.class)
public abstract class MixinAbstractContainerScreen extends Screen implements MC {

    @Shadow
    protected int leftPos;
    @Shadow
    protected int topPos;
    @Shadow
    protected int imageWidth;
    @Shadow
    protected int imageHeight;
    @Shadow
    protected AbstractContainerMenu menu;

    @Shadow
    protected abstract void slotClicked(Slot slot, int slotId, int mouseButton, ClickType type);

    private ContainerGuiOverlay currentOverlay = null;

    protected MixinAbstractContainerScreen(Component title) {

        super(title);
    }

    private ContainerGuiOverlay apec$getOverlay() {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            apec$clearOverlay();
            return null;
        }
        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.CUSTOM_SKILL_VIEW)) {
            apec$clearOverlay();
            return null;
        }
        if (!Apec.INSTANCE.settingsManager.getSettingState(SettingID.MENU_GUI)) {
            apec$clearOverlay();
            return null;
        }
        if (currentOverlay == null) {
            currentOverlay = ContainerGuiManager.get().findForTitle(this.title);
            // Set up the slot click callback for the overlay
            if (currentOverlay instanceof SkillViewOverlay) {
                ((SkillViewOverlay) currentOverlay).setSlotClickCallback(slotIndex -> {
                    apec$clickSlot(slotIndex, 0, ClickType.PICKUP);
                });
            }
        }
        return currentOverlay;
    }

    private void apec$clearOverlay() {
        if (currentOverlay != null) {
            currentOverlay = null;
            ContainerGuiManager.INSTANCE.clearActiveOverlay();
        }
    }

    @Inject(method = "render(Lnet/minecraft/client/gui/GuiGraphics;IIF)V", at = @At("HEAD"), cancellable = true)
    private void apec$renderOverlayHead(GuiGraphics g, int mouseX, int mouseY, float delta, CallbackInfo ci) {

        ContainerGuiOverlay overlay = apec$getOverlay();
        if (overlay == null) return;
        List<Slot> slots = this.menu != null ? this.menu.slots : java.util.List.of();
        overlay.render(g, mouseX, mouseY, delta, leftPos, topPos, imageWidth, imageHeight, mc, this.menu, slots);
        ci.cancel();
    }

    @Inject(method = "mouseClicked(DDI)Z", at = @At("HEAD"), cancellable = true)
    private void apec$mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {

        ContainerGuiOverlay overlay = apec$getOverlay();
        if (overlay == null) return;
        List<Slot> slots = this.menu != null ? this.menu.slots : java.util.List.of();
        boolean handled = overlay.mouseClicked(this.menu, slots, mouseX, mouseY, button);
        if (handled) {
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }
        // By default, block vanilla clicks while overlay is active
        cir.setReturnValue(false);
        cir.cancel();
    }

    @Inject(method = "mouseReleased(DDI)Z", at = @At("HEAD"), cancellable = true)
    private void apec$mouseReleased(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {

        ContainerGuiOverlay overlay = apec$getOverlay();
        if (overlay == null) return;
        List<Slot> slots = this.menu != null ? this.menu.slots : java.util.List.of();
        boolean handled = overlay.mouseReleased(this.menu, slots, mouseX, mouseY, button);
        if (handled) {
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }
        cir.setReturnValue(false);
        cir.cancel();
    }

    @Inject(method = "mouseScrolled(DDDD)Z", at = @At("HEAD"), cancellable = true)
    private void apec$mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY, CallbackInfoReturnable<Boolean> cir) {

        ContainerGuiOverlay overlay = apec$getOverlay();
        if (overlay == null) return;
        List<Slot> slots = this.menu != null ? this.menu.slots : java.util.List.of();
        boolean handled = overlay.mouseScrolled(this.menu, slots, mouseX, mouseY, scrollX, scrollY);
        if (handled) {
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }
        cir.setReturnValue(false);
        cir.cancel();
    }

    @Inject(method = "keyPressed(III)Z", at = @At("HEAD"), cancellable = true)
    private void apec$keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {

        ContainerGuiOverlay overlay = apec$getOverlay();
        if (overlay == null) return;
        // Allow ESC to close container
        if (keyCode == 256 /* GLFW.GLFW_KEY_ESCAPE */) return;
        List<Slot> slots = this.menu != null ? this.menu.slots : java.util.List.of();
        boolean handled = overlay.keyPressed(this.menu, slots, keyCode, scanCode, modifiers);
        if (handled) {
            cir.setReturnValue(true);
            cir.cancel();
            return;
        }
        // Block other keys by default
        cir.setReturnValue(false);
        cir.cancel();
    }

    // Expose the slot clicking method for overlays to use
    public void apec$clickSlot(int slotId, int mouseButton, ClickType type) {

        if (this.menu != null && slotId >= 0 && slotId < this.menu.slots.size()) {
            Slot slot = this.menu.slots.get(slotId);
            slotClicked(slot, slotId, mouseButton, type);
        }
    }

    @Inject(method = "onClose()V", at = @At("HEAD"))
    private void apec$onClose(CallbackInfo ci) {
        apec$clearOverlay();
    }

}
