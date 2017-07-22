package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.command.CommandConfig;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.CommandExecutor;

import java.util.Collection;
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

    public boolean isEnabled() {
        return globalConfig.isEnabled() && config.isEnabled();
    }

    public boolean isSupportedChannel(Channel channel) {
        return getChannels().isEmpty() || getChannels().contains(channel.getId());
    }

    private List<String> getChannels() {
        return (config.getChannels().isEmpty()) ? globalConfig.getChannels() : config.getChannels();
    }

    public boolean hasPermission(User user) {
        Collection<Role> userRoles = user.getRoles(server);

        if (config.getRoles().isEmpty()) { // Check Global Roles
            return globalConfig.getRoles().stream()
                .anyMatch(r -> r.equalsIgnoreCase("everyone")) || userRoles.stream()
                .anyMatch(role -> globalConfig.getRoles().contains(role.getId())
                    || globalConfig.getRoles().contains(role.getName().toLowerCase()));
        } else { // Check Command Roles
            return config.getRoles().stream()
                .anyMatch(r -> r.equalsIgnoreCase("everyone")) || userRoles.stream()
                .anyMatch(role -> config.getRoles().contains(role.getId())
                    || config.getRoles().contains(role.getName().toLowerCase()));
        }
    }
}
