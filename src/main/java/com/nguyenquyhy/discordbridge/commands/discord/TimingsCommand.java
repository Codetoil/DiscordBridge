package com.nguyenquyhy.discordbridge.commands.discord;

import co.aikar.timings.Timings;
import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.DiscordSource;
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
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class TimingsCommand extends DiscordCommand {

    public TimingsCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getTimingsCommand(), server);
    }

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
    public void onTimingsCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        TimingsCommand.Config config = (Config) this.config;

        // Delete the command message
        command.delete();

        if (Timings.isTimingsEnabled())
            Sponge.getScheduler().createTaskBuilder().execute(src -> Timings.generateReport(new DiscordSource(user, channel, config.directMessage))).submit(mod);
        else
            ChannelUtil.sendMessage(channel, "Timings are not enabled on this server!");
    }
}