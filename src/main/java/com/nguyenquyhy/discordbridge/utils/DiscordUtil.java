package com.nguyenquyhy.discordbridge.utils;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.entities.permissions.Role;
import org.spongepowered.api.Sponge;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class DiscordUtil {

    /**
     * @param name   The name to search the server for valid a User
     * @param server The server to search through Users
     * @return The User, if any, that matches the name supplied
     */
    static Optional<User> getUserByName(String name, Server server) {
        for (User user : server.getMembers()) {
            if (user.getName().equalsIgnoreCase(name) || (user.getNickname(server) != null && user.getNickname(server).equalsIgnoreCase(name))) {
                return Optional.of(user);
            }
        }
        return Optional.empty();
    }

    /**
     * @param name   The name to search the server for valid a Role
     * @param server The server to search through Roles
     * @return The Role, if any, that matches the name supplied
     */
    static Optional<Role> getRoleByName(String name, Server server) {
        for (Role role : server.getRoles()) {
            if (role.getName().equalsIgnoreCase(name)) {
                return Optional.of(role);
            }
        }
        return Optional.empty();
    }

    /**
     * @param name   The name to search the server for valid a Channel
     * @param server The server to search through Roles
     * @return The Channel, if any, that matches the name supplied
     */
    static Optional<Channel> getChannelByName(String name, Server server) {
        for (Channel channel : server.getChannels()) {
            if (channel.getName().equalsIgnoreCase(name)) {
                return Optional.of(channel);
            }
        }
        return Optional.empty();
    }

    public static void deleteMessageAfter(Message message, long delay, TimeUnit unit) {
        if (delay < 0) return;
        Sponge.getScheduler().createTaskBuilder()
            .async()
            .delay(delay, unit)
            .execute(message::delete)
            .submit(DiscordBridge.getInstance());
    }
}
