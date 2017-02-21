package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.TextUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Optional;

public class TpsCommand implements CommandExecutor {
    private static DiscordBridge mod = DiscordBridge.getInstance();

    private static String template = "`%s TPS`";

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting
        public String template;

        public Config() {
            super(true, "tps");
            template = TpsCommand.template;
        }
    }

    @Command(aliases = {"tps"}, description = "", usage = "")
    public String onTpsCommand(User user, Channel channel, Message command) {
        Config config = mod.getCommandConfig().getDiscordCommands().getTpsCommand();

        // Check if the command is disabled
        if (!config.isEnabled())
            return null;
        // Check if the command is allowed in this channel
        if (config.getChannels().isEmpty() || !config.getChannels().contains(channel.getId())) // No channels set or channel is not enabled
            return null;
        // Check if the user has permission to use the command, if required
        Optional<Role> role = TextUtil.getHighestRole(user, channel.getServer());
        String roleId = (role.isPresent()) ? role.get().getId() : "everyone";
        String roleName = (role.isPresent()) ? role.get().getName().toLowerCase() : "everyone";
        if (config.getRoles().isEmpty()
                || !config.getRoles().contains(roleId)
                && config.getRoles().stream().noneMatch(r -> r.equalsIgnoreCase("everyone"))
                && config.getRoles().stream().noneMatch(r -> r.equalsIgnoreCase(roleName)))
            return null;
        // Delete the command message
        command.delete();


        return String.format(config.template, mod.getGame().getServer().getTicksPerSecond());
    }
}