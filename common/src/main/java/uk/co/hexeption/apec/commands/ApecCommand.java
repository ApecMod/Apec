package uk.co.hexeption.apec.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import dev.architectury.event.events.client.ClientCommandRegistrationEvent;
import java.util.Arrays;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import uk.co.hexeption.apec.Apec;
import uk.co.hexeption.apec.MC;
import uk.co.hexeption.apec.settings.SettingID;
import uk.co.hexeption.apec.settings.menu.SettingsMenu;

/**
 * Command handler for the "apec" command which opens the settings menu
 */
public class ApecCommand implements MC {

    private static final SuggestionProvider<ClientCommandRegistrationEvent.ClientCommandSourceStack> SETTINGS_SUGGESTION_PROVIDER = (context, builder) -> SharedSuggestionProvider.suggest(Arrays.stream(SettingID.values()).map(Enum::name).map(String::toLowerCase), builder);

    private static final String PREFIX = ChatFormatting.GOLD + "✧ " + ChatFormatting.AQUA + "Apec" + ChatFormatting.GRAY + " » " + ChatFormatting.RESET;

    /**
     * Registers the apec command with the command dispatcher
     */
    public static void register(CommandDispatcher<ClientCommandRegistrationEvent.ClientCommandSourceStack> dispatcher, CommandBuildContext registryAccess) {

        LiteralArgumentBuilder<ClientCommandRegistrationEvent.ClientCommandSourceStack> apecCommand = ClientCommandRegistrationEvent.literal("apec");

        apecCommand.executes((ctx) -> {
            mc.schedule(() -> mc.setScreen(new SettingsMenu(0)));
            return 0;
        });

        apecCommand.then(ClientCommandRegistrationEvent.literal("toggle").then(ClientCommandRegistrationEvent.argument("setting", StringArgumentType.word()).suggests(SETTINGS_SUGGESTION_PROVIDER).executes(ctx -> {
            String settingName = StringArgumentType.getString(ctx, "setting").toUpperCase();
            try {
                SettingID settingID = SettingID.valueOf(settingName);
                boolean currentState = Apec.INSTANCE.settingsManager.getSettingState(settingID);
                Apec.INSTANCE.settingsManager.setSettingState(settingID, !currentState);

                String displayName = Apec.INSTANCE.settingsManager.settings.stream().filter(s -> s.settingID == settingID).findFirst().map(s -> s.name).orElse("");

                boolean newState = !currentState;
                String stateSymbol = newState ? "✓" : "✗";
                String stateColor = newState ? ChatFormatting.GREEN.toString() : ChatFormatting.RED.toString();

                ctx.getSource().arch$sendSuccess(() -> Component.literal(PREFIX +
                    ChatFormatting.YELLOW + displayName + ChatFormatting.GRAY + " has been " +
                    stateColor + stateSymbol + " " + (newState ? "enabled" : "disabled")), false);
                return 1;
            } catch (IllegalArgumentException e) {
                ctx.getSource().arch$sendSuccess(() -> Component.literal(PREFIX +
                    ChatFormatting.RED + "✗ " + "Unknown setting: " + ChatFormatting.YELLOW + settingName), false);
                return 0;
            }
        })).executes(ctx -> {
            ctx.getSource().arch$sendSuccess(() -> Component.literal(
                PREFIX + ChatFormatting.WHITE + "Available settings " +
                ChatFormatting.GRAY + "(Use " + ChatFormatting.YELLOW + "/apec toggle <setting>" +
                ChatFormatting.GRAY + " to toggle):"), false);

            ctx.getSource().arch$sendSuccess(() -> Component.literal(
                ChatFormatting.DARK_GRAY + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"), false);

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

                ctx.getSource().arch$sendSuccess(() -> message, false);
            }

            ctx.getSource().arch$sendSuccess(() -> Component.literal(
                ChatFormatting.DARK_GRAY + "⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯⎯"), false);

            return 1;
        }));

        dispatcher.register(apecCommand);
    }
}
