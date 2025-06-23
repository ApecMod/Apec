package uk.co.hexeption.fabric.apec;

import net.fabricmc.api.ClientModInitializer;
import uk.co.hexeption.apec.Apec;

public final class ApecFabric implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        Apec.init();
    }

}
