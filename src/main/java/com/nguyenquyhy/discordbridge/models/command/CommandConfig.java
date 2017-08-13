package com.nguyenquyhy.discordbridge.models.command;

import com.google.common.collect.Lists;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public class CommandConfig {
    @Setting(value = "enabled", comment = "Use to enabled/disable the DiscordBridge command module.")
    private boolean enabled = false;
    @Setting(value = "expiration", comment = "The number of seconds to wait before clearing a command message or response. -1 to disable.")
    private int expiration = 20;
    @Setting(value = "prefix", comment = "The default prefix to use for commands.")
    private String prefix = "!";
    @Setting(value = "channels", comment = "The default channels to listen for commands in.")
    private List<String> channels = Lists.newArrayList();
    @Setting(value = "roles", comment = "The default roles to allow using commands.")
    private List<String> roles = Lists.newArrayList();
    @Setting(value = "built-in")
    private DiscordCommandConfig discordCommands;
    @Setting(value = "custom")
    private CustomCommandConfig customCommands;

    public CommandConfig() {
        discordCommands = new DiscordCommandConfig();
        customCommands = new CustomCommandConfig();
    }

    public boolean isEnabled() {
        return enabled;
    }

    public int getExpiration() {
        return expiration;
    }

    public String getPrefix() {
        return prefix;
    }

    public List<String> getChannels() {
        return channels;
    }

    public List<String> getRoles() {
        return roles;
    }

    public DiscordCommandConfig getDiscordCommands() {
        return discordCommands;
    }

    public CustomCommandConfig getCustomCommands() {
        return customCommands;
    }
}
