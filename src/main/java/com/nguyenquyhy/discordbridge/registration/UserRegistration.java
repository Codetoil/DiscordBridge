package com.nguyenquyhy.discordbridge.registration;

import com.google.common.base.Preconditions;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import org.apache.commons.lang3.text.StrBuilder;

public class UserRegistration {

    private static final String separator = "\u2015";

    private User discordUser;
    private String minecraftUsername;
    private String code;

    public UserRegistration(User discordUser, String minecraftUser, String code) {
        this.minecraftUsername = minecraftUser;
        this.discordUser = discordUser;
        this.code = code;
    }

    public static UserRegistration of(Message message) {
        Preconditions.checkNotNull(message);
        String[] values = message.getContent().split(separator);
        return new UserRegistration(message.getMentions().get(0), values[1], values[2]);
    }

    public String getMinecraftUsername() {
        return minecraftUsername;
    }

    public User getDiscordUser() {
        return discordUser;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return new StrBuilder(discordUser.getMentionTag())
            .append(separator).append(minecraftUsername)
            .append(separator).append(code)
            .build();
    }
}
