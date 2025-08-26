package uk.co.hexeption.apec.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.util.Arrays;
import java.util.List;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

/**
 * Command handler for the "apec" command which opens the settings menu and allows toggling settings
 */
public class ApecCommands implements MC {

    private static final SuggestionProvider<FabricClientCommandSource> SETTINGS_SUGGESTION_PROVIDER =
        (context, builder) -> SharedSuggestionProvider.suggest(
            Arrays.stream(SettingID.values()).map(Enum::name).map(String::toLowerCase), builder);

    private static final String PREFIX = ChatFormatting.GOLD + "✧ " + ChatFormatting.AQUA + "Apec" + ChatFormatting.GRAY + " » " + ChatFormatting.RESET;

    /**
     * Registers the apec command with the command dispatcher
     */
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        LiteralArgumentBuilder<FabricClientCommandSource> apecCommand = ClientCommandManager.literal("apec");

        // Base command - opens settings menu
        apecCommand.executes((ctx) -> {
            ctx.getSource().sendFeedback(Component.literal(PREFIX +
                ChatFormatting.YELLOW + "Opening settings menu..."));
            mc.schedule(() -> mc.setScreen(new SettingsMenu(0)));
            return 1;
        });

        // Help command
        apecCommand.then(ClientCommandManager.literal("help")
            .executes(ctx -> {
                ctx.getSource().sendFeedback(Component.literal(PREFIX +
                    ChatFormatting.WHITE + "Apec Commands Help"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.DARK_GRAY + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.YELLOW + "/apec " + ChatFormatting.GRAY + "- Opens the settings menu"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.YELLOW + "/apec help " + ChatFormatting.GRAY + "- Shows this help message"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.YELLOW + "/apec toggle " + ChatFormatting.GRAY + "- Lists all available settings"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.YELLOW + "/apec toggle <setting> " + ChatFormatting.GRAY + "- Toggles a specific setting"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.DARK_GRAY + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.AQUA + "Example:"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.GRAY + "  " + ChatFormatting.YELLOW + "/apec toggle hp_bar " +
                    ChatFormatting.GRAY + "- Toggles the HP bar"));

                return 1;
            }));

        // Toggle subcommand with setting argument
        apecCommand.then(ClientCommandManager.literal("toggle")
            .then(ClientCommandManager.argument("setting", StringArgumentType.word())
                .suggests(SETTINGS_SUGGESTION_PROVIDER)
                .executes(ctx -> {
                    String settingName = StringArgumentType.getString(ctx, "setting").toUpperCase();
                    try {
                        SettingID settingID = SettingID.valueOf(settingName);
                        boolean currentState = Apec.INSTANCE.settingsManager.getSettingState(settingID);
                        Apec.INSTANCE.settingsManager.setSettingState(settingID, !currentState);

                        // Get display name from settings data
                        String displayName = Apec.INSTANCE.settingsManager.settings.stream()
                            .filter(s -> s.settingID == settingID)
                            .findFirst()
                            .map(s -> s.name)
                            .orElse(settingID.name());

                        boolean newState = !currentState;
                        String stateSymbol = newState ? "✓" : "✗";
                        String stateColor = newState ? ChatFormatting.GREEN.toString() : ChatFormatting.RED.toString();

                        ctx.getSource().sendFeedback(Component.literal(PREFIX +
                            ChatFormatting.YELLOW + displayName + ChatFormatting.GRAY + " has been " +
                            stateColor + stateSymbol + " " + (newState ? "enabled" : "disabled")));
                        return 1;
                    } catch (IllegalArgumentException e) {
                        ctx.getSource().sendFeedback(Component.literal(PREFIX +
                            ChatFormatting.RED + "✗ " + "Unknown setting: " + ChatFormatting.YELLOW + settingName));
                        return 0;
                    }
                }))
            .executes(ctx -> {
                // Show available settings when no setting specified
                ctx.getSource().sendFeedback(Component.literal(
                    PREFIX + ChatFormatting.WHITE + "Available settings " +
                    ChatFormatting.GRAY + "(Use " + ChatFormatting.YELLOW + "/apec toggle <setting>" +
                    ChatFormatting.GRAY + " to toggle):"));

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.DARK_GRAY + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

                List<SettingID> allSettings = Arrays.asList(SettingID.values());

                int chunkSize = 8;
                for (int i = 0; i < allSettings.size(); i += chunkSize) {
                    int end = Math.min(i + chunkSize, allSettings.size());
                    List<SettingID> chunk = allSettings.subList(i, end);

                    MutableComponent message = Component.literal("");

                    for (SettingID id : chunk) {
                        boolean isEnabled = Apec.INSTANCE.settingsManager.getSettingState(id);
                        String symbol = isEnabled ? "✓" : "✗";
                        ChatFormatting color = isEnabled ? ChatFormatting.GREEN : ChatFormatting.RED;

                        message.append(Component.literal(color + symbol + " " +
                            ChatFormatting.GRAY + id.name().toLowerCase() + "  "));
                    }

                    ctx.getSource().sendFeedback(message);
                }

                ctx.getSource().sendFeedback(Component.literal(
                    ChatFormatting.DARK_GRAY + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"));

                return 1;
            }));

        dispatcher.register(apecCommand);
    }
}
