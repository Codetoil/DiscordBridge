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
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

public class OnlineCommand extends DiscordCommand {

    private static String headerTemplate = "**Online Players:**```\n";
    private static String playerTemplate = "%a";
    private static String footerTemplate = "```";
    private static String offlineTemplate = "**No Online Players**";

    public OnlineCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getOnlineCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting(value = "headerTemplate", comment = "Template for the first line before listing players.")
        public String headerTemplate;
        @Setting(value = "playerTemplate", comment = "Template for each online player.")
        public String playerTemplate;
        @Setting(value = "footerTemplate", comment = "Template for the last line after listing players.")
        public String footerTemplate;
        @Setting(value = "offlineTemplate", comment = "Template for when no players are online.")
        public String offlineTemplate;

        public Config() {
            super(true, "online");
            headerTemplate = OnlineCommand.headerTemplate;
            playerTemplate = OnlineCommand.playerTemplate;
            footerTemplate = OnlineCommand.footerTemplate;
            offlineTemplate = OnlineCommand.offlineTemplate;
        }
    }

    @Command(aliases = {"online"}, description = "Shows a list of online players.", usage = "")
    public void onOnlineCommand(User user, Channel channel, Message command) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        OnlineCommand.Config config = (Config) this.config;

        // Delete the command message
        DiscordUtil.deleteMessageAfter(command, expiration, TimeUnit.SECONDS);

        Collection<Player> players = mod.getGame().getServer().getOnlinePlayers();

        if (players.isEmpty()) ChannelUtil.sendMessage(channel, config.offlineTemplate);
        String message = config.headerTemplate;
        for (Player player : players) {
            message += config.playerTemplate
                .replace("%a", player.getName())
                .replace("%u", player.getName())
                + "\n"; //TODO template support with placeholders (name, displayname, rank, etc)
        }
        message += config.footerTemplate;

        ChannelUtil.sendSelfDestructingMessage(channel, message, expiration, TimeUnit.SECONDS);
    }
}
