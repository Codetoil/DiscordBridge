package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.TextUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import de.btobastian.sdcf4j.Command;
import de.btobastian.sdcf4j.CommandExecutor;
import ninja.leaping.configurate.objectmapping.Setting;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.entity.living.player.Player;

import java.util.Collection;
import java.util.Optional;

public class OnlineCommand implements CommandExecutor {
    private static DiscordBridge mod = DiscordBridge.getInstance();

    private static String headerTemplate = "**Online Players:**\n```";
    private static String playerTemplate = "%a";
    private static String footerTemplate = "```";
    private static String offlineTemplate = "**No Online Players**";

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
    public String onOnlineCommand(User user, Channel channel, Message command) {
        Config config = mod.getCommandConfig().getDiscordCommands().getOnlineCommand();

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

        Collection<Player> players = mod.getGame().getServer().getOnlinePlayers();

        if (players.isEmpty()) return config.offlineTemplate;
        String message = config.headerTemplate;
        for (Player player : players) {
            message += config.playerTemplate
                    .replace("%a", player.getName())
                    .replace("%u", player.getName())
                    + "\n"; //TODO template support with placeholders (name, displayname, rank, etc)
        }
        message += config.footerTemplate;

        return message;
    }
}
