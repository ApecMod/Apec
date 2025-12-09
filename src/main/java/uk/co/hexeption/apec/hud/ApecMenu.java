package uk.co.hexeption.apec.hud;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.ChatFormatting;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.joml.Vector2f;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.hud.customization.CustomizationScreen;
import uk.co.hexeption.apec.hud.elements.BottomBar;
import uk.co.hexeption.apec.hud.elements.ExtraInfo;
import uk.co.hexeption.apec.hud.elements.ItemHotBar;
import uk.co.hexeption.apec.hud.elements.ToolTipText;
import uk.co.hexeption.apec.hud.elements.WarningIcons;
import uk.co.hexeption.apec.hud.elements.air.AirBar;
import uk.co.hexeption.apec.hud.elements.air.AirText;
import uk.co.hexeption.apec.hud.elements.pressure.PressureBar;
import uk.co.hexeption.apec.hud.elements.pressure.PressureText;
import uk.co.hexeption.apec.hud.elements.drillfuel.DrillFuelBar;
import uk.co.hexeption.apec.hud.elements.drillfuel.DrillFuelText;
import uk.co.hexeption.apec.hud.elements.health.HPBar;
import uk.co.hexeption.apec.hud.elements.health.HPText;
import uk.co.hexeption.apec.hud.elements.mana.MPBar;
import uk.co.hexeption.apec.hud.elements.mana.MPText;
import uk.co.hexeption.apec.hud.elements.skill.SkillBar;
import uk.co.hexeption.apec.hud.elements.skill.SkillText;
import uk.co.hexeption.apec.hud.elements.xp.XPBar;
import uk.co.hexeption.apec.hud.elements.xp.XPText;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.utils.ApecUtils;
import uk.co.hexeption.apec.utils.GuiGraphicsUtils;

public class ApecMenu implements MC, HudRenderLayer  {

    private boolean hudEnabled = true;

    public List<Element> guiElements = new ArrayList<>() {
        {
            addAll(List.of(
//                    new DebugText(),
                    new HPText(),
                    new HPBar(),
                    new MPText(),
                    new MPBar(),
                    new XPText(),
                    new XPBar(),
                    new DrillFuelText(),
                    new DrillFuelBar(),
                    new AirText(),
                    new AirBar(),
                    new PressureText(),
                    new PressureBar(),
                    new ExtraInfo(),
                    new BottomBar(),
                    new ItemHotBar(),
                    new ToolTipText(),
                    new SkillBar(),
                    new SkillText(),
                    new WarningIcons()
            ));
        }
    };


    public void init() {

        applyDeltas();

        for (Element element : guiElements) {
            element.init(this);
        }

        ClientTickEvents.START_CLIENT_TICK.register(client -> {
            if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
                return;
            }
            for (Element element : guiElements) {
                element.tick();
            }
        });

    }

    public void CustomizationMenuOpened() {
        for (Element element : guiElements) {
            element.editInit();
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker tickCounter) {
        if (!Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            return;
        }

        if(!shouldShowHUD()) {
            return;
        }

        for (Element element : guiElements) {
            GuiGraphicsUtils.push(guiGraphics);
            GuiGraphicsUtils.scale(guiGraphics, element.scale);
            element.drawText(guiGraphics, mc.screen instanceof CustomizationScreen);
            GuiGraphicsUtils.pop(guiGraphics);
        }
    }

    public Vector2f applyGlobalChanges(Element element, Vector2f anchorPoint) {
        boolean isbbUp = Apec.INSTANCE.settingsManager.getSettingState(SettingID.BB_ON_TOP);
        if (isbbUp && element.getDeltaPosition().length() == 0)
            anchorPoint = ApecUtils.addVec(anchorPoint, new Vector2f(0, 20));
        return anchorPoint;
    }

    private void applyDeltas() {
        try {
            File file = new File("config/Apec/GuiDeltas.json");
            if (!file.exists()) return;
            Gson gson = new Gson();
            String jsonContent = new String(java.nio.file.Files.readAllBytes(file.toPath()));
            JsonArray jsonArray = JsonParser.parseString(jsonContent).getAsJsonArray();
            for (JsonElement el : jsonArray) {
                JsonObject obj = el.getAsJsonObject();
                String typeName = obj.get("type").getAsString();
                ElementType elementType = ElementType.valueOf(typeName);
                Element element = getGuiComponent(elementType);
                float deltaX = obj.get("deltaX").getAsFloat();
                float deltaY = obj.get("deltaY").getAsFloat();
                float scale = obj.get("scale").getAsFloat();
                element.setDeltaPosition(new org.joml.Vector2f(deltaX, deltaY));
                element.setScale(scale);
                if (obj.has("subComponents")) {
                    JsonArray subArray = obj.getAsJsonArray("subComponents");
                    for (JsonElement subEl : subArray) {
                        JsonObject subObj = subEl.getAsJsonObject();
                        int subIdx = subObj.get("index").getAsInt();
                        float subDeltaX = subObj.get("deltaX").getAsFloat();
                        float subDeltaY = subObj.get("deltaY").getAsFloat();
                        element.setSubElementDeltaPosition(subIdx, new org.joml.Vector2f(subDeltaX, subDeltaY));
                    }
                }
            }
        } catch (Exception e) {
            //ApecUtils.showMessage("[\u00A72Apec\u00A7f] There was an error reading GUI deltas!");
            try {
                new File("config/Apec/GuiDeltas.json").delete();
            } catch (Exception err) {
                err.printStackTrace();
            }
        }
    }

    public <T extends Element> T getGuiComponent(ElementType type) {
        for (Element component : guiElements) {
            if (component.type == type) return (T) component;
        }
        return null;
    }

    public void toggleHUD() {
        hudEnabled = !hudEnabled;

        String stateText = hudEnabled ? "enabled" : "disabled";
        String stateSymbol = hudEnabled ? "✓" : "✗";
        ChatFormatting stateColor = hudEnabled ? ChatFormatting.GREEN : ChatFormatting.RED;

        if (mc.player != null) {
            mc.player.displayClientMessage(Component.literal(
                ChatFormatting.GOLD + "✧ " + ChatFormatting.AQUA + "Apec" + ChatFormatting.GRAY + " » " +
                ChatFormatting.RESET + "HUD has been " + stateColor + stateSymbol + " " + stateText
            ), false);
        }

        Apec.LOGGER.info("HUD toggled: {}", stateText);
    }

    public void setHUDEnabled(boolean enabled) {
        this.hudEnabled = enabled;

        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.AUTO_ENABLE)) {
            String stateText = enabled ? "enabled" : "disabled";
            Apec.LOGGER.info("HUD auto-toggled: {}", stateText);
        }
    }

    public boolean isHUDEnabled() {
        return hudEnabled;
    }

    public boolean shouldShowHUD() {
        if (Apec.INSTANCE.settingsManager.getSettingState(SettingID.AUTO_ENABLE)) {
            return hudEnabled && Apec.SKYBLOCK_INFO.isOnSkyblock();
        }

        return hudEnabled;
    }
}
