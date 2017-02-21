package com.nguyenquyhy.discordbridge.commands;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.commands.discord.OnlineCommand;
import com.nguyenquyhy.discordbridge.commands.discord.ReloadCommand;
import com.nguyenquyhy.discordbridge.commands.discord.TimingsCommand;
import com.nguyenquyhy.discordbridge.commands.discord.TpsCommand;
import com.nguyenquyhy.discordbridge.models.command.CommandConfig;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;

public class DiscordCommands {
    private static DiscordBridge mod = DiscordBridge.getInstance();

    public static void register(DiscordAPI api) {
        CommandConfig config = mod.getCommandConfig();
        if (config == null || !config.isEnabled()) return;
        if (api == null) {
            mod.getLogger().info("Discord commands unable to be registered.");
            return;
        }
        CommandHandler commandHandler = new JavacordHandler(api);

        commandHandler.setDefaultPrefix(config.getPrefix());
        commandHandler.registerCommand(new OnlineCommand());
        commandHandler.registerCommand(new ReloadCommand());
        commandHandler.registerCommand(new TpsCommand());
//        commandHandler.registerCommand(new TimingsCommand());
        mod.getLogger().info("Discord commands registered.");
    }
}
