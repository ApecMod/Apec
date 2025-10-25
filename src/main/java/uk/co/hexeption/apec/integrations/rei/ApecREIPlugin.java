//? if <= 1.21.8 {
/*package uk.co.hexeption.apec.integrations.rei;

import dev.kikugie.fletching_table.annotation.fabric.Entrypoint;
import me.shedaniel.rei.api.client.plugins.REIClientPlugin;
import me.shedaniel.rei.api.client.registry.screen.OverlayDecider;
import me.shedaniel.rei.api.client.registry.screen.ScreenRegistry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.InteractionResult;
import uk.co.hexeption.apec.gui.container.ContainerGuiManager;

@Entrypoint("rei_client")
/^ REI Plugin to manage overlay rendering on custom container screens to hide REI ^/
public class ApecREIPlugin implements REIClientPlugin {

    @Override
    public void registerScreens(ScreenRegistry registry) {

        registry.registerDecider(new OverlayDecider() {

            @Override
            public <T extends Screen> boolean isHandingScreen(Class<T> screen) {

                return AbstractContainerScreen.class.isAssignableFrom(screen);
            }

            @Override
            public <T extends Screen> InteractionResult shouldScreenBeOverlaid(T screen) {

                return isCustomGuiActive(screen) ? InteractionResult.FAIL : InteractionResult.PASS;
            }
        });
    }

    private boolean isCustomGuiActive(Screen screen) {

        try {
            if (screen instanceof AbstractContainerScreen<?> containerScreen) {
                return ContainerGuiManager.INSTANCE != null &&
                        ContainerGuiManager.INSTANCE.hasActiveOverlay(containerScreen);
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

}

*///?}
