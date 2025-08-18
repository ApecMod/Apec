package uk.co.hexeption.apec.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

@Entrypoint("modmenu")
@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return SettingsMenu::createConfigScreen;
    }
}
