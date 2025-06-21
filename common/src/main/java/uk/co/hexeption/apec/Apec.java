package uk.co.hexeption.apec;

import com.mojang.blaze3d.platform.InputConstants;
import dev.architectury.event.events.client.ClientGuiEvent;
import dev.architectury.event.events.client.ClientTickEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.utils.Env;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;
import uk.co.hexeption.apec.hud.ApecMenu;
import uk.co.hexeption.apec.settings.SettingsManager;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;
import uk.co.hexeption.apec.skyblock.SkyBlockInfo;

public final class Apec implements MC {
    public static final String MOD_ID = "apec";
    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    public static Apec INSTANCE;

    public static SkyBlockInfo SKYBLOCK_INFO = new SkyBlockInfo();

    public static ApecMenu apecMenu = new ApecMenu();

    public SettingsManager settingsManager = new SettingsManager();

    public static void init() {
        INSTANCE = new Apec();
        // Write common init code here.
        if(Platform.getEnvironment() == Env.CLIENT) {
            clientInit();
        }
    }

    @Environment(EnvType.CLIENT)
    public static void clientInit() {

        var settingKeybind = new KeyMapping("key.apec.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_RIGHT_SHIFT, "key.categories.apec");
        KeyMappingRegistry.register(settingKeybind);

        SKYBLOCK_INFO.init();

        INSTANCE.settingsManager.LoadSettings();

        ClientGuiEvent.INIT_POST.register((guiGraphics, deltaTracker) -> {
            apecMenu.init();
        });

        ClientTickEvent.CLIENT_POST.register((client) -> {
            if (settingKeybind.consumeClick()) {
                mc.setScreen(new SettingsMenu(0));
            }
        });


    }


}
