package uk.co.hexeption.apec.loaders;

import com.mojang.blaze3d.platform.InputConstants;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.ResourceLocation;
import org.lwjgl.glfw.GLFW;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.commands.ApecCommands;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

@Entrypoint
@Environment(EnvType.CLIENT)
public class FabricClientLoader implements ClientModInitializer, MC {

    //? if > 1.21.8 {
    private static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(ResourceLocation.tryBuild("apec", "apec"));
    //?}

    private static KeyMapping settingKeybind;
    private static KeyMapping hudToggleKeybind;

    @Override
    public void onInitializeClient() {
        // Initialize common code first
        Apec.init();

        // Fabric-specific client initialization
        //? if > 1.21.8 {
        settingKeybind = new KeyMapping("key.apec.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, CATEGORY);
        hudToggleKeybind = new KeyMapping("key.apec.toggle_hud", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_CONTROL, CATEGORY);
        //?} else {
        /*settingKeybind = new KeyMapping("key.apec.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.apec");
        hudToggleKeybind = new KeyMapping("key.apec.toggle_hud", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_CONTROL, "key.categories.apec");
        *///?}

        // Use Fabric-specific registration
        KeyBindingHelper.registerKeyBinding(settingKeybind);
        KeyBindingHelper.registerKeyBinding(hudToggleKeybind);

        // Use Fabric-specific command registration
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            ApecCommands.register(dispatcher);
        });

        Apec.SKYBLOCK_INFO.init();
        Apec.INSTANCE.settingsManager.LoadSettings();

        Apec.apecMenu.init();

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (settingKeybind.consumeClick()) {
                mc.setScreen(new SettingsMenu(0));
            }

            if (hudToggleKeybind.consumeClick()) {
                if (Apec.apecMenu != null) {
                    Apec.apecMenu.toggleHUD();
                }
            }
        });
    }
}
