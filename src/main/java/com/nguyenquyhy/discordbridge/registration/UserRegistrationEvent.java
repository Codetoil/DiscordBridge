package com.nguyenquyhy.discordbridge.registration;

import de.btobastian.javacord.entities.User;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.entity.living.humanoid.player.TargetPlayerEvent;

import javax.annotation.Nonnull;

public class UserRegistrationEvent implements TargetPlayerEvent {

    private Player player;
    private User user;

    public UserRegistrationEvent(Player player, User user) {
        this.player = player;
        this.user = user;
    }

    public User getDiscordUser() {
        return user;
    }

    @Nonnull
    @Override
    public Player getTargetEntity() {
        return player;
    }

    @Nonnull
    @Override
    public Cause getCause() {
        return Cause.builder().suggestNamed("player", player).build();
    }
}
