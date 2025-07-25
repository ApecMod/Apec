package uk.co.hexeption.fabric.apec.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

public class ApecModMenuCompat implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {

        return parent -> new SettingsMenu(0);
    }

}
