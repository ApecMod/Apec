package uk.co.hexeption.apec.mixins.container;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.apec.gui.container.ContainerScreenBridge;

@Mixin(ContainerScreen.class)
public abstract class MixinContainerScreen {

    @Inject(
            method = "extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;IIF)V",
            at = @At("HEAD"),
            cancellable = true
    )
    private void apec$cancelChestBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY,
            float delta, CallbackInfo ci) {

        if ((Object) this instanceof ContainerScreenBridge screen && screen.apec$getOverlay() != null) {
            ci.cancel();
        }
    }
}
