package uk.co.hexeption.apec.mixins;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.hud.ElementType;
import uk.co.hexeption.apec.hud.customization.CustomizationScreen;
import uk.co.hexeption.apec.hud.elements.ItemHotBar;

@Mixin(Gui.class)
public class MixinGui implements MC {

    @Shadow private int toolHighlightTimer;

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void renderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void renderScoreboardSidebar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void renderHearts(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void renderExperienceBar(GuiGraphics guiGraphics, int i, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void renderExperienceLevel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void renderArmor(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }
        ci.cancel();
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void renderFood(GuiGraphics guiGraphics, Player player, int i, int j, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        ci.cancel();
    }

    @WrapOperation(method = "renderHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Gui;renderItemHotbar(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"))
    private void renderItemHotbar(Gui instance, GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            original.call(instance, guiGraphics, deltaTracker);
            return;
        }

        var apecHotBar = ((ItemHotBar) Apec.apecMenu.getGuiComponent(ElementType.ITEM_HOT_BAR));
        var pos = apecHotBar.getCurrentAnchorPoint();
        var scale = apecHotBar.getScale();

        var translationX = (guiGraphics.guiWidth() / 2) - pos.x / scale - 91;
        var translationY = (guiGraphics.guiHeight()) - pos.y / scale - 22;

        guiGraphics.pose().pushPose();
        guiGraphics.pose().scale(scale, scale, scale);
        guiGraphics.pose().translate(-translationX, -translationY, 100);
        
        original.call(instance, guiGraphics, deltaTracker);
        
        guiGraphics.pose().popPose();
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        Apec.apecMenu.render(guiGraphics);

    }

    @ModifyVariable(method = "renderSelectedItemName", at = @At(value = "STORE"), ordinal = 1)
    private int modifyXPosition(int original, GuiGraphics guiGraphics) {
        var toolTipText = (uk.co.hexeption.apec.hud.elements.ToolTipText) Apec.apecMenu.getGuiComponent(ElementType.TOOL_TIP_TEXT);

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return original;
        }
        return toolTipText.getXOffset(guiGraphics);
    }

    @ModifyVariable(method = "renderSelectedItemName", at = @At(value = "STORE"), ordinal = 2)
    private int modifyYPosition(int original, GuiGraphics guiGraphics) {
        var toolTipText = (uk.co.hexeption.apec.hud.elements.ToolTipText) Apec.apecMenu.getGuiComponent(ElementType.TOOL_TIP_TEXT);

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return original;
        }
        return toolTipText.getYOffset(guiGraphics);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
          return;
        }

        if(mc.screen instanceof CustomizationScreen) {
            this.toolHighlightTimer = 255;
        }
    }
}
