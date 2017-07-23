package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.command.CommandConfig;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.List;

public abstract class DiscordCommand implements CommandExecutor {

    protected static final DiscordBridge mod = DiscordBridge.getInstance();
    protected final CoreCommandConfig config;
    private final CommandConfig globalConfig;
    private Server server;

    protected DiscordCommand(CoreCommandConfig config, Server server) {
        this.globalConfig = mod.getCommandConfig();
        this.config = config;
        this.server = server;
    }

    protected boolean isEnabled() {
        return globalConfig.isEnabled() && config.isEnabled();
    }

    protected boolean isSupportedChannel(Channel channel) {
        return getChannels().isEmpty()
            || getChannels().contains(channel.getId())
            || getChannels().stream().anyMatch(c -> c.equalsIgnoreCase(channel.getName()));
    }

    private List<String> getChannels() {
        return (config.getChannels().isEmpty()) ? globalConfig.getChannels() : config.getChannels();
    }

    protected boolean hasPermission(User user) {
        return getRoles().stream()
            .anyMatch(r -> r.equalsIgnoreCase("everyone"))
            || user.getRoles(server).stream().anyMatch(userRole -> getRoles().contains(userRole.getId())
            || getRoles().stream().anyMatch(r -> r.equalsIgnoreCase(userRole.getName())));
    }

    private List<String> getRoles() {
        return (config.getRoles().isEmpty()) ? globalConfig.getRoles() : config.getRoles();
    }
}
