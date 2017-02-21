package com.nguyenquyhy.discordbridge;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.service.context.Context;
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
    private List<Text> messages = Lists.newArrayList();

    @Override
    public String getName() {
        return "Discord Bridge";
    }

    @Override
    public Optional<CommandSource> getCommandSource() {
        return Optional.empty();
    }

    @Override
    public SubjectCollection getContainingCollection() {
        return null;
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
    public String getIdentifier() {
        return "discordbridge";
    }

    @Override
    public Set<Context> getActiveContexts() {
        return ImmutableSet.of();
    }

    @Override
    public void sendMessage(Text message) {
        messages.add(message);
    }

    public List<Text> getMessages() {
        List<Text> m = messages;
        messages.clear();
        return m;
    }

    public Text getLastMessage() {
        if(messages.isEmpty()) return Text.EMPTY;
        Text message = messages.get(messages.size());
        messages.remove(message);
        return message;
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
