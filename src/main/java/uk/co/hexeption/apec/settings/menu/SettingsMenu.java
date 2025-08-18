package uk.co.hexeption.apec.settings.menu;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.hud.customization.CustomizationScreen;
import uk.co.hexeption.apec.mixins.accessors.ScreenAccessor;
import uk.co.hexeption.apec.settings.Setting;
import uk.co.hexeption.apec.settings.SettingsManager;
import uk.co.hexeption.apec.utils.ApecUtils;

public class SettingsMenu extends Screen implements MC {

    Integer pageNumber;

    List<Setting> settings = new ArrayList<>();

    SettingNavigationButton settingNavigationButton;

    EditBox searchBox;

    int SearchBoxAnimationStartingWidth = 30;
    /**
     * Used for the sole reason that the width is stored as an int and decimal increments are needed for it to be smooth
     */
    float WidthSearchBoxAnimation = SearchBoxAnimationStartingWidth;

    public SettingsMenu(Integer pageNumber) {
        super(Component.nullToEmpty("Settings Menu"));
        this.pageNumber = pageNumber;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(new SettingNavigationButton(width / 2 - 265, height / 2 - 130, 20, 250, button -> ((SettingNavigationButton) button).runAction(), font, NavigationAction.BACK));
        addRenderableWidget(new SettingNavigationButton(width / 2 + 245, height / 2 - 130, 20, 250, button -> ((SettingNavigationButton) button).runAction(), font, NavigationAction.NEXT));
        // breaks if not on Skyblock
        if (Apec.SKYBLOCK_INFO.isOnSkyblock()) {
            addRenderableWidget(new SettingNavigationButton(0, 0, 85, 23, button -> ((SettingNavigationButton) button).runAction(), font, NavigationAction.OPEN_GUI_EDITING));
        }
        settingNavigationButton = addRenderableWidget(new SettingNavigationButton(width / 2 - 265, height / 2 - 145, 120, 15, button -> ((SettingNavigationButton) button).runAction(), font, NavigationAction.SEARCH));

        searchBox = addRenderableWidget(new EditBox(font, width / 2 - 265, height / 2 - 145, SearchBoxAnimationStartingWidth, 15, Component.nullToEmpty("Search")));
        searchBox.visible = false;

        searchBox.setResponder(this::SearchFor);

        settings.clear();
        settings.addAll(Apec.INSTANCE.settingsManager.settings);

        loadPage(pageNumber);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int i, int j, float f) {
        // Text is (for some reason) offset on the z axis in 1.21.5 rendering. In order to not cause any problems, with the 1.21.8 move, let's just keep this how it was yeah?
        // ideally, this is fixed properly (https://discord.com/channels/1002229552921202759/1143261334100775023/1406880714095333377)
        //? if 1.21.5
        /*super.render(guiGraphics, i, j, f);*/

        guiGraphics.fill(width / 2 - 245, height / 2 - 130, width / 2 + 245, height / 2 + 120, 0x990a0a0a);

        final int spaceBetweenLines = 60;

        for (int ik = pageNumber * 12; ik < settings.size() && ik < (pageNumber + 1) * 12; ik++) {
            Setting s = settings.get(ik);
            boolean enabled = settings.get(ik).enabled;

            int x = width / 2 - 235 + ((ik % 12) % 3) * 160;
            int y = height / 2 - 120 + spaceBetweenLines * ((ik % 12) / 3);

            drawRectangleAt(guiGraphics, x, y, 15, 5, i, j);
        }

        if (searchBox.visible) {
            if (searchBox.getWidth() < 150) {
                float delta = 120.0f / Minecraft.getInstance().getFps();
                WidthSearchBoxAnimation += 3f * delta;
                searchBox.setWidth((int) WidthSearchBoxAnimation);
            }
            if (searchBox.getWidth() > 150) {
                searchBox.setWidth(150);
            }
            searchBox.render(guiGraphics, i, j, f);
        }

        // ensure we render the boxes first. again, ideally this should be fixed
        //? if >= 1.21.8
        super.render(guiGraphics, i, j, f);
    }

    public void drawRectangleAt(GuiGraphics graphics, int x, int y, int w, int h, int mX, int mY) {
        graphics.fill(x, y, x + w * 10, y + h * 10, 0x99151515);
        for (int i = 0; i < w; i++) {
            drawLineComponent(graphics, x + i * 10, y - 1, x + (i + 1) * 10, y + 1, mX, mY);
            drawLineComponent(graphics, x + i * 10, y - 1 + h * 10, x + (i + 1) * 10, y + 1 + h * 10, mX, mY);
        }
        for (int i = 0; i < h; i++) {
            drawLineComponent(graphics, x - 1, y + i * 10, x + 1, y + (i + 1) * 10, mX, mY);
            drawLineComponent(graphics, x - 1 + w * 10, y + i * 10, x + 1 + w * 10, y + (i + 1) * 10, mX, mY);
        }
    }

    private void drawLineComponent(GuiGraphics graphics, int left, int top, int right, int bottom, int mX, int mY) {
        double range = 45;
        double dist = Math.sqrt(Math.pow(left - mX, 2) + Math.pow(top - mY, 2));
        if (dist > range) dist = range;
        graphics.fill(left, top, right, bottom, 0xffffff | ((int) (0xff * ((range - dist) / range)) << 24));
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void runAction(NavigationAction action) {
        switch (action) {
            case BACK:
                if (pageNumber > 0)
                    pageNumber = pageNumber - 1;
                this.loadPage(pageNumber);
                break;
            case NEXT:
                if (pageNumber < Apec.INSTANCE.settingsManager.settings.size() / 12)
                    pageNumber = pageNumber + 1;
                this.loadPage(pageNumber);
                break;
            case OPEN_GUI_EDITING:
                mc.setScreen(new CustomizationScreen());
                break;
            case SEARCH:
                OpenSearchBox();
                break;
        }
    }

    private void OpenSearchBox() {
        searchBox.visible = true;
        settingNavigationButton.visible = false;
    }

    private void loadPage(Integer pageNumber) {
        List<SettingButton> settingButtonsToRemove = new ArrayList<>();
        ((ScreenAccessor) this).getChildren().forEach(child -> {
            if (child instanceof SettingButton) {
                settingButtonsToRemove.add((SettingButton) child);
            }
        });
        settingButtonsToRemove.forEach(this::removeWidget);
        for (int i = pageNumber * 12; i < settings.size() && i < (pageNumber + 1) * 12; i++) {
            addRenderableWidget(new SettingButton(width / 2 - 235 + ((i % 12) % 3) * 160, height / 2 - 120 + 60 * ((i % 12) / 3), 15 * 10, 5 * 10, settings.get(i)));
        }
    }

    private void SearchFor(String searchTerm) {
        if (!searchTerm.isEmpty()) {
            Integer[] Scores = new Integer[Apec.INSTANCE.settingsManager.settings.size()];
            int SettingCount = Apec.INSTANCE.settingsManager.settings.size();
            String[] SearchWords = searchTerm.split(" ");

            // Use the actual settings manager list for search, not the local list
            List<Setting> sourceSettings = Apec.INSTANCE.settingsManager.settings;

            for (int i = 0; i < SettingCount; i++) {
                String[] SettingWords = SettingsManager.settingData.get(sourceSettings.get(i).settingID).getA().toLowerCase().split(" ");

                int TotalScore = 0;
                for (String searchWord : SearchWords) {
                    char[] SearchTermC = searchWord.toCharArray();
                    if (SearchTermC.length == 0) continue;
                    for (int j = 0; j < SettingWords.length; j++) {
                        char[] Word = SettingWords[j].toCharArray();
                        if (Word.length == 0) continue;
                        int Score = 0;
                        int CharactersFound = 0;
                        boolean LastChracterVald = false;
                        for (int l = 0; l < Word.length; l++) {
                            if (Word[l] == SearchTermC[CharactersFound]) {
                                // Give a large score to names that match portions of the exact sequence
                                Score += 1 + (LastChracterVald ? 99 : 0);
                                // Gives a bonus if it's the start
                                if (l == 0) {
                                    Score += Math.max(10 - j, 0);
                                }
                                CharactersFound++;
                                LastChracterVald = true;
                            } else {
                                LastChracterVald = false;
                                Score--;
                            }
                            if (CharactersFound == SearchTermC.length) break;
                        }
                        TotalScore += Score;
                    }
                }
                Scores[i] = TotalScore;
            }

            // Clear and create fresh sorted list based on search results
            settings.clear();
            settings.addAll(sourceSettings);
            ApecUtils.bubbleSort(Lists.newArrayList(Scores), settings);

        } else {
            // Reset to original order - always refresh from settings manager
            settings.clear();
            settings.addAll(Apec.INSTANCE.settingsManager.settings);
        }
        pageNumber = 0;
        this.loadPage(pageNumber);
    }

    public static Screen createConfigScreen(Screen screen) {
        return new SettingsMenu(0);
    }
}
