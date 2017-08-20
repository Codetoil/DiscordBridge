package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.config.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.DiscordUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.concurrent.TimeUnit;

public class TpsCommand extends DiscordCommand {

    private static String template = "`%f.2 TPS`";

    public TpsCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getTpsCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting
        public String template;
        @Setting
        public boolean worlds;

        public Config() {
            super(true, "tps");
            template = TpsCommand.template;
            worlds = true;
        }
    }

    @Command(aliases = {"tps"}, description = "", usage = "")
    public void onTpsCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        TpsCommand.Config config = (Config) this.config;

        // Delete the command message
        DiscordUtil.deleteMessageAfter(command, expiration, TimeUnit.SECONDS);

        ChannelUtil.sendSelfDestructingMessage(channel, String.format(config.template, mod.getGame().getServer().getTicksPerSecond()), expiration, TimeUnit.SECONDS);
    }
}