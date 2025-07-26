//? if neoforge {
/*package uk.co.hexeption.apec.loaders;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientCommandsEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.ClientTickEvent;
import org.lwjgl.glfw.GLFW;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.commands.ApecCommand;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

@EventBusSubscriber(modid = Apec.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class NeoForgeClientLoader implements MC {

    private static KeyMapping settingKeybind;

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        // Initialize common code first
        CommonLoader.init();

        event.enqueueWork(() -> {
            Apec.SKYBLOCK_INFO.init();
            Apec.INSTANCE.settingsManager.LoadSettings();
        });

        // Register NeoForge-specific client events
        NeoForge.EVENT_BUS.addListener(NeoForgeClientLoader::onRenderGui);
        NeoForge.EVENT_BUS.addListener(NeoForgeClientLoader::onClientTick);
    }

    @SubscribeEvent
    public static void onRegisterKeyMappings(RegisterKeyMappingsEvent event) {
        settingKeybind = new KeyMapping("key.apec.open_menu", InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_M, "key.categories.apec");
        event.register(settingKeybind);
    }

    @SubscribeEvent
    public static void onRegisterClientCommands(RegisterClientCommandsEvent event) {
        ApecCommand.registerNeoForge(event.getDispatcher());
    }

    public static void onRenderGui(RenderGuiEvent.Post event) {
        Apec.apecMenu.render(event.getGuiGraphics(), event.getPartialTick());
    }

    public static void onClientTick(ClientTickEvent.Post event) {
        if (settingKeybind.consumeClick()) {
            mc.setScreen(new SettingsMenu(0));
        }
    }
}
*///?}
