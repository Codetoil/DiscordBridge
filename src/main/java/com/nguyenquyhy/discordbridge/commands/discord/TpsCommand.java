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
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Optional;

public class TpsCommand extends DiscordCommand {

    private static String template = "`%s TPS`";

    public TpsCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getTpsCommand(), server);
    }

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
    public void onTpsCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        TpsCommand.Config config = (Config) this.config;

        // Delete the command message
        command.delete();

        ChannelUtil.sendMessage(channel, String.format(config.template, mod.getGame().getServer().getTicksPerSecond()));
    }
}