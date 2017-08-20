package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.config.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.DiscordUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.concurrent.TimeUnit;

public class ReloadCommand extends DiscordCommand {

    public ReloadCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getReloadCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        public Config() {
            super(true, "reload");
        }
    }

    @Command(aliases = {"reload"}, description = "", usage = "")
    public void onReloadCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        // Delete the command message
        DiscordUtil.deleteMessageAfter(command, expiration, TimeUnit.SECONDS);

        ChannelUtil.sendSelfDestructingMessage(channel, DiscordBridge.reload(), expiration, TimeUnit.SECONDS);
    }
}
