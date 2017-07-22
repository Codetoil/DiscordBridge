package com.nguyenquyhy.discordbridge;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.User;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.permission.Subject;
import org.spongepowered.api.service.permission.SubjectCollection;
import org.spongepowered.api.service.permission.SubjectData;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.channel.MessageChannel;
import org.spongepowered.api.util.Tristate;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class DiscordSource implements CommandSource {

    private User user;
    private Channel channel;
    private boolean dm;

    // For text channel messages
    public DiscordSource(User user, Channel channel, boolean dm) {
        this.user = user;
        this.channel = channel;
        this.dm = dm;
    }

    @Override
    public String getName() {
        return "Discord Bridge";
    }

    @Override
    public String getIdentifier() {
        return "discordbridge";
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.of(this);
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return Sponge.getGame().getServiceManager().provideUnchecked(PermissionService.class).getGroupSubjects();
    }

    @Override
    public SubjectData getSubjectData() {
        return null;
    }

    @Override
    public SubjectData getTransientSubjectData() {
        return null;
    }

    @Override
    public Tristate getPermissionValue(Set<Context> contexts, String permission) {
        return Tristate.TRUE;
    }

    @Override
    public boolean isChildOf(Set<Context> contexts, Subject parent) {
        return false;
    }

    @Override
    public List<Subject> getParents(Set<Context> contexts) {
        return  ImmutableList.of();
    }

    @Override
    public Optional<String> getOption(Set<Context> contexts, String key) {
        return Optional.empty();
    }

    @Override
    public Set<Context> getActiveContexts() {
        return ImmutableSet.of();
    }

    @Override
    public void sendMessage(Text message) {
        if (dm) {
            user.sendMessage(message.toPlain());
        } else {
            ChannelUtil.sendMessage(channel, message.toPlain());
        }
    }

    @Override
    public MessageChannel getMessageChannel() {
        return () -> ImmutableSet.of(this);
    }

    @Override
    public void setMessageChannel(MessageChannel channel) {
        // NO OP
    }
}
