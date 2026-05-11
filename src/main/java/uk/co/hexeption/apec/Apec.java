package uk.co.hexeption.apec;

import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import uk.co.hexeption.apec.hud.ApecMenu;
import uk.co.hexeption.apec.settings.SettingsManager;
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

        INSTANCE.addHudLayer();
    }

    private void addHudLayer() {
        HudElementRegistry.attachElementAfter(VanillaHudElements.MISC_OVERLAYS, Identifier.fromNamespaceAndPath(MOD_ID, "apec_menu"), apecMenu::render);
    }
}
