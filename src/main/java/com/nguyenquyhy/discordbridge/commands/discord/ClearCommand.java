package com.nguyenquyhy.discordbridge.commands.discord;

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

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class ClearCommand extends DiscordCommand {

    private static String template = "**%d messages removed!**";

    public ClearCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getClearCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting
        public String template;
        @Setting(value = "clear-pins")
        public boolean clearPins;

        public Config() {
            super(true, "clear");
            template = ClearCommand.template;
            clearPins = false;
        }
    }

    @Command(aliases = {"clear"}, description = "", usage = "")
    public void onClearCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        ClearCommand.Config config = (Config) this.config;

        // Delete the command message
        DiscordUtil.deleteMessageAfter(command, expiration, TimeUnit.SECONDS);

        String[] history;
        try {
            if (config.clearPins) {
                history = channel.getMessageHistory(100).get().getMessagesSorted().stream().map(Message::getId).toArray(String[]::new);
            }else {
                history = channel.getMessageHistory(100).get().getMessagesSorted().stream().filter(m -> !m.isPinned()).map(Message::getId).toArray(String[]::new);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return;
        }

        if (history.length > 0) {
            mod.getLogger().info(String.format("%s: %d messages removed.", channel.getName(), history.length));
            channel.bulkDelete(history);
        }

        ChannelUtil.sendSelfDestructingMessage(channel, String.format(config.template, history.length), expiration, TimeUnit.SECONDS);
    }
}