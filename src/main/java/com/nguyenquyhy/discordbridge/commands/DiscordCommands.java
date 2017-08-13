package com.nguyenquyhy.discordbridge.commands;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.commands.discord.*;
import com.nguyenquyhy.discordbridge.models.command.CommandConfig;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Server;
import de.btobastian.sdcf4j.CommandHandler;
import de.btobastian.sdcf4j.handler.JavacordHandler;

public class DiscordCommands {
    private static DiscordBridge mod = DiscordBridge.getInstance();

    public static void register(DiscordAPI api, Server server) {
        CommandConfig config = mod.getCommandConfig();
        if (config == null || !config.isEnabled()) return;
        if (api == null) {
            mod.getLogger().info("Discord commands unable to be registered.");
            return;
        }
        CommandHandler commandHandler = new JavacordHandler(api);

        commandHandler.setDefaultPrefix(config.getPrefix());
        commandHandler.registerCommand(new ClearCommand(server));
        commandHandler.registerCommand(new OnlineCommand(server));
        commandHandler.registerCommand(new PurgeCommand(server));
        commandHandler.registerCommand(new ReloadCommand(server));
        commandHandler.registerCommand(new TpsCommand(server));
        commandHandler.registerCommand(new TimingsCommand(server));
        mod.getLogger().info("Discord commands registered.");
    }
}
