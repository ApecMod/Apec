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
import org.lwjgl.glfw.GLFW;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

@Entrypoint
@Environment(EnvType.CLIENT)
public class FabricClientLoader implements ClientModInitializer, MC {

    private static KeyMapping settingKeybind;

    @Override
    public void onInitializeClient() {
        // Initialize common code first
        Apec.init();

        // Fabric-specific client initialization
        settingKeybind = new KeyMapping("key.apec.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.apec");

        // Use Fabric-specific registration
        KeyBindingHelper.registerKeyBinding(settingKeybind);

        // Use Fabric-specific command registration
        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
        });

        Apec.SKYBLOCK_INFO.init();
        Apec.INSTANCE.settingsManager.LoadSettings();

        Apec.apecMenu.init();

        ClientTickEvents.END_CLIENT_TICK.register((client) -> {
            if (settingKeybind.consumeClick()) {
                mc.setScreen(new SettingsMenu(0));
            }
        });
    }
}
