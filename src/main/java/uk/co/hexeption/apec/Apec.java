package uk.co.hexeption.apec;

//? if >=1.21.6 {
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//?} else if >=1.21.5 {
/*import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
*///?}
import net.minecraft.resources.ResourceLocation;
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
        //? if >=1.21.6 {
        HudElementRegistry.addLast(ResourceLocation.fromNamespaceAndPath(MOD_ID, "apec_menu"), apecMenu::render);
        //?} else if >=1.21.5 {
        /*HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.addLayer(IdentifiedLayer.of(ResourceLocation.fromNamespaceAndPath(MOD_ID, "apec_menu"), apecMenu::render));
        });
        *///?}
    }
}
