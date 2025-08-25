package uk.co.hexeption.apec.gui.container;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.network.chat.Component;
import uk.co.hexeption.apec.gui.container.impl.SkillViewOverlay;

public class ContainerGuiManager {

    private static final ContainerGuiManager INSTANCE = new ContainerGuiManager();
    private final List<ContainerGuiOverlay> overlays = new ArrayList<>();

    private ContainerGuiManager() {
        // Register overlays
        SkillViewOverlay skillViewOverlay = new SkillViewOverlay();
        overlays.add(skillViewOverlay);
    }

    public static ContainerGuiManager get() {

        return INSTANCE;
    }

    public ContainerGuiOverlay findForTitle(Component title) {

        for (ContainerGuiOverlay overlay : overlays) {
            if (overlay.matchesTitle(title)) {
                // Set up the slot click callback for overlays that need it
                if (overlay instanceof SkillViewOverlay) {
                    ((SkillViewOverlay) overlay).setSlotClickCallback(slotIndex -> {
                        // This will be called by the mixin - we need a way to get back to the screen
                        // For now, this is a placeholder that the mixin will handle
                    });
                }
                return overlay;
            }
        }
        return null;
    }

}
