package com.nguyenquyhy.discordbridge.commands.discord;

import com.google.common.collect.Lists;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.DiscordUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

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
        DiscordUtil.deleteMessageAfter(command, expiration, TimeUnit.SECONDS);

        List<Message> history;
        try {
            history = Lists.reverse(channel.getMessageHistory(config.maxHistory).get().getMessagesSorted());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        String[] messages = history.stream()
            .filter(m -> m.getContent().matches("(\\*\\*Server has \\w*?\\.\\*\\*)|(_\\S*? just (?:(?:joined)|(?:left)) the server_)"))
            .limit(100)
            .map(Message::getId)
            .toArray(String[]::new);

        if (messages.length > 0) {
            mod.getLogger().info(String.format("%s: %d messages removed.", channel.getName(), messages.length));
            channel.bulkDelete(messages);
        }

        ChannelUtil.sendSelfDestructingMessage(channel, String.format(config.template, messages.length), expiration, TimeUnit.SECONDS);
    }
}