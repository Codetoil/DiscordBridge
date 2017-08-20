package com.nguyenquyhy.discordbridge.registration;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;
import com.nguyenquyhy.discordbridge.DiscordBridge;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.javacord.listener.message.MessageCreateListener;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;

import java.util.Set;

public class UserRegistrationService {

    private static DiscordBridge mod = DiscordBridge.getInstance();
    private final Channel channel;
    private final Set<UserRegistration> registrations = Sets.newHashSet();

    public UserRegistrationService() {
        this.channel = mod.getBotClient().getChannelById(mod.getConfig().getRegistrationService().getChannelId());
        mod.getBotClient().registerListener((MessageCreateListener) this::onMessageReceived);
        Sponge.getEventManager().registerListeners(mod, this);
    }

    private void onMessageReceived(DiscordAPI client, Message message) {
        if (message.getChannelReceiver().equals(channel)) {
            try {
                registrations.add(UserRegistration.of(message));
            } catch (NullPointerException | IllegalArgumentException e) {
                mod.getLogger().info("Invalid User Registration: " + message.getContent());
            }
        }
    }

    @Listener
    public void onUserRegistration(UserRegistrationEvent event) {
        PermissionService permissionService = Sponge.getServiceManager().provideUnchecked(PermissionService.class);
        Subject subject = permissionService.getUserSubjects().get(event.getTargetEntity().getIdentifier());
        Set<Context> global = ImmutableSet.of();

        subject.getSubjectData().setOption(global, mod.getConfig().getRegistrationService().getOptionName(), event.getDiscordUser().getId());
    }

    public Set<UserRegistration> getRegistrations() {
        return registrations;
    }
}
