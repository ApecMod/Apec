package uk.co.hexeption.apec.mixins;

//? if >= 26.2 {
import net.minecraft.client.gui.Hud;
//?}
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
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

//? if >= 26.2 {
@Mixin(Hud.class)
        //?} else {
/*@Mixin(Gui.class)
 *///?}
public abstract class MixinGui implements MC {

    @Shadow
    private int toolHighlightTimer;

    @Shadow
    protected abstract void extractSelectedItemName(GuiGraphicsExtractor guiGraphics);

    @Inject(method = "extractEffects", at = @At("HEAD"), cancellable = true)
    private void renderEffects(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

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

    @Inject(method = "extractScoreboardSidebar", at = @At("HEAD"), cancellable = true)
    private void renderScoreboardSidebar(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

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

    @Inject(method = "extractOverlayMessage", at = @At("HEAD"), cancellable = true)
    private void renderOverlayMessage(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

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

    @Inject(method = "extractHearts", at = @At("HEAD"), cancellable = true)
    private void renderHearts(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, int k, int l, float f, int m, int n, int o, boolean bl, CallbackInfo ci) {

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

    //? if >= 26.2 {
    @Inject(method = "extractHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBar;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"), cancellable = true)
    //?} else {
    /*@Inject(method = "extractHotbarAndDecorations", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/contextualbar/ContextualBarRenderer;extractBackground(Lnet/minecraft/client/gui/GuiGraphicsExtractor;Lnet/minecraft/client/DeltaTracker;)V"), cancellable = true)
     *///?}
    private void cancelExperienceRendering(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, CallbackInfo ci) {

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if (!Apec.apecMenu.shouldShowHUD()) {
            return;
        }

        ci.cancel();

        if (this.mc.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            this.extractSelectedItemName(guiGraphics);
        }
    }

    @Inject(method = "extractArmor", at = @At("HEAD"), cancellable = true)
    private static void renderArmor(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, int k, int l, CallbackInfo ci) {

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

    @Inject(method = "extractFood", at = @At("HEAD"), cancellable = true)
    private void renderFood(GuiGraphicsExtractor guiGraphics, Player player, int i, int j, CallbackInfo ci) {

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

    @WrapMethod(method = "extractItemHotbar")
    private void moveItemHotbar(GuiGraphicsExtractor guiGraphics, DeltaTracker deltaTracker, Operation<Void> original) {

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
        GuiGraphicsUtils.translate(guiGraphics, -translationX, -translationY);
        original.call(guiGraphics, deltaTracker);
        GuiGraphicsUtils.pop(guiGraphics);
    }

    @ModifyVariable(method = "extractSelectedItemName", at = @At(value = "STORE"), ordinal = 1)
    private int modifyXPosition(int original, GuiGraphicsExtractor guiGraphics) {

        var toolTipText = (uk.co.hexeption.apec.hud.elements.ToolTipText) Apec.apecMenu.getGuiComponent(ElementType.TOOL_TIP_TEXT);

        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return original;
        }

        if(Apec.INSTANCE.settingsManager.getSettingState(SettingID.ITEM_HIGHLIGHT_TEXT)){
            int textWidth = guiGraphics.guiWidth() - 2 * original;
            return toolTipText.getXOffset(guiGraphics) + (mc.font.width("hello") - textWidth) / 2;
        }

        return toolTipText.getXOffset(guiGraphics);
    }

    @ModifyVariable(method = "extractSelectedItemName", at = @At(value = "STORE"), ordinal = 2)
    private int modifyYPosition(int original, GuiGraphicsExtractor guiGraphics) {

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

        //? if >= 26.2 {
        if (mc.gui.screen() instanceof CustomizationScreen) {
        //?} else {
        /*if (mc.screen instanceof CustomizationScreen) {
         *///?}
            this.toolHighlightTimer = 255;
        }
    }

    @Inject(method = "extractAirBubbles", at = @At("HEAD"), cancellable = true)
    private void renderAirBubbles(GuiGraphicsExtractor guiGraphics, Player player, int vehicleMaxHealth, int y, int x, CallbackInfo ci) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        ci.cancel();
    }

}
