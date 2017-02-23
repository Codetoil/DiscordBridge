package com.nguyenquyhy.discordbridge.commands.discord;

import co.aikar.timings.Timings;
import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.TextUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class TimingsCommand implements CommandExecutor {
    private static DiscordBridge mod = DiscordBridge.getInstance();

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting
        public boolean directMessage;

        public Config() {
            super(true, "timings");
            directMessage = true;
        }
    }

    @Command(aliases = {"timings"}, description = "", usage = "")
    public String onTimingsCommand(User user, Channel channel, Message command) {
        Config config = mod.getCommandConfig().getDiscordCommands().getTimingsCommand();

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

        if (!Timings.isTimingsEnabled())
            return null;

        Sponge.getScheduler().createTaskBuilder().execute(src -> Timings.generateReport(mod.getCommandSource())).submit(mod);
        String message = "";
        for (Text text : mod.getCommandSource().getMessages()) {
            message += text.toPlain() + "\n";
        }

        if (!config.directMessage)
            return ChannelUtil.SPECIAL_CHAR + message;
        user.sendMessage(message);
        return null;
    }
}