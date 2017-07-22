package com.nguyenquyhy.discordbridge.commands.discord;

import com.google.common.collect.Lists;
import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.TextUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PurgeCommand extends DiscordCommand {

    private static String template = "**%d messages removed!**";

    public PurgeCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getPurgeCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting
        public String template;
        @Setting(value = "max-history")
        public int maxHistory;

        public Config() {
            super(true, "purge");
            template = PurgeCommand.template;
            maxHistory = 1000;
        }
    }

    @Command(aliases = {"purge"}, description = "", usage = "")
    public void onPurgeCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        PurgeCommand.Config config = (Config) this.config;

        // Delete the command message
        command.delete();

        List<Message> history;
        try {
            history = Lists.reverse(channel.getMessageHistory(config.maxHistory).get().getMessagesSorted());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        Message[] messages = history.stream()
            .filter(m -> m.getContent().matches("(\\*\\*Server has \\w*?\\.\\*\\*)|(_\\S*? just (?:(?:joined)|(?:left)) the server_)"))
            .limit(100)
            .toArray(Message[]::new);

        if (messages.length > 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");
            mod.getLogger().info(String.format("%s: %d messages removed from %s - %s.",
                channel.getName(),
                messages.length,
                sdf.format(messages[messages.length - 1].getCreationDate().getTime()),
                sdf.format(messages[0].getCreationDate().getTime())
            ));
            channel.bulkDelete(messages);
        }

        ChannelUtil.sendMessage(channel, String.format(config.template, messages.length));
    }
}