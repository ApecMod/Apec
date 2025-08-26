package uk.co.hexeption.apec.mixins;

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
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
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;
//? if >= 1.21.8 {
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
//?}

@Mixin(Gui.class)
public class MixinGui implements MC {

    @Shadow
    private int toolHighlightTimer;

    @Inject(method = "renderEffects", at = @At("HEAD"), cancellable = true)
    private void renderEffects(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_EFFECTS)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void renderScoreboardSidebar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_SCOREBOARD)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void renderOverlayMessage(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_OVERLAY_MESSAGE)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderHearts", at = @At("HEAD"), cancellable = true)
    private void renderHearts(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_HEARTS)) {
            ci.cancel();
        }
    }

    //? if >= 1.21.8 {
    @Inject(method = "renderHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;renderBackground(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/client/DeltaTracker;)V"), cancellable = true)
    private void cancelExperienceRendering(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        ci.cancel();
    }
    //?} else {
    /*@Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
    private void renderExperienceBar(GuiGraphics guiGraphics, int i, CallbackInfo ci) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_EXPERIENCE_BAR)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderExperienceLevel", at = @At("HEAD"), cancellable = true)
    private void renderExperienceLevel(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_EXPERIENCE_LEVEL)) {
            ci.cancel();
        }
    }
    *///?}

    @Inject(method = "renderArmor", at = @At("HEAD"), cancellable = true)
    private static void renderArmor(GuiGraphics guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_ARMOR)) {
            ci.cancel();
        }
    }

    @Inject(method = "renderFood", at = @At("HEAD"), cancellable = true)
    private void renderFood(GuiGraphics guiGraphics, Player player, int i, int j, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.HIDE_VANILLA_FOOD)) {
            ci.cancel();
        }
    }

    //? if >= 1.21.8 {
    @WrapMethod(method = "renderItemHotbar")
    private void moveItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            original.call(guiGraphics, deltaTracker);
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            original.call(guiGraphics, deltaTracker);
            return;
        }

        var apecHotBar = ((ItemHotBar) Apec.apecMenu.getGuiComponent(ElementType.ITEM_HOT_BAR));
        var pos = apecHotBar.getCurrentAnchorPoint();
        var scale = apecHotBar.getScale();

        var translationX = ((float) guiGraphics.guiWidth() / 2) - pos.x / scale - 91;
        var translationY = (guiGraphics.guiHeight()) - pos.y / scale - 22;

        GuiGraphicsUtils.push(guiGraphics);
        GuiGraphicsUtils.scale(guiGraphics, scale);
        GuiGraphicsUtils.translate(guiGraphics, -translationX, -translationY, 100);
        original.call(guiGraphics, deltaTracker);
        GuiGraphicsUtils.pop(guiGraphics);
    }
    //?} else {
    /*@Inject(method = "renderItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;pose()Lcom/mojang/blaze3d/vertex/PoseStack;", ordinal = 0))
    private void renderItemHotbar(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        var apecHotBar = ((ItemHotBar) Apec.apecMenu.getGuiComponent(ElementType.ITEM_HOT_BAR));
        var pos = apecHotBar.getCurrentAnchorPoint();
        var scale = apecHotBar.getScale();

        var translationX = ((float) guiGraphics.guiWidth() / 2) - pos.x / scale - 91;
        var translationY = (guiGraphics.guiHeight()) - pos.y / scale - 22;

        GuiGraphicsUtils.push(guiGraphics);
        GuiGraphicsUtils.scale(guiGraphics, scale);
        GuiGraphicsUtils.translate(guiGraphics, -translationX, -translationY, 100);
    }

    @Inject(method = "renderItemHotbar", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Options;attackIndicator()Lnet/minecraft/client/OptionInstance;"))
    private void renderItemHotbarReturn(GuiGraphics guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        GuiGraphicsUtils.pop(guiGraphics);
    }
    *///?}

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

    @Inject(method = "tick()V", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (mc.screen instanceof CustomizationScreen) {
            this.toolHighlightTimer = 255;
        }
    }

}
