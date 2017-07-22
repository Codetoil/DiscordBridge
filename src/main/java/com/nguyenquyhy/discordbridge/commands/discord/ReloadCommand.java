package com.nguyenquyhy.discordbridge.commands.discord;

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
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Optional;

public class ReloadCommand extends DiscordCommand {

    public ReloadCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getReloadCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        public Config () {
            super(true, "reload");
        }
    }

    @Command(aliases = {"reload"}, description = "", usage = "")
    public void onReloadCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        // Delete the command message
        command.delete();

        ChannelUtil.sendMessage(channel, DiscordBridge.reload());
    }
}
