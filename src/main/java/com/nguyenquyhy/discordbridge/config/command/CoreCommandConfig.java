package com.nguyenquyhy.discordbridge.config.command;

import com.google.common.collect.Lists;
import com.nguyenquyhy.discordbridge.DiscordBridge;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

@ConfigSerializable
public abstract class CoreCommandConfig {
    @Setting(value = "enabled")
    private boolean enabled = false;
//    @Setting(value = "alias")
//    private String alias = null;
    @Setting(value = "channels")
    private List<String> channels = Lists.newArrayList();
    @Setting(value = "roles")
    private List<String> roles = Lists.newArrayList();

    public CoreCommandConfig() {

    }

    public CoreCommandConfig(boolean enabled, String alias) {
        this.enabled = enabled;
//        this.alias = alias;
    }

    public boolean isEnabled() {
        return DiscordBridge.getInstance().getCommandConfig().isEnabled() && enabled;
    }

//    public String getAlias() {
//        return alias;
//    }

    public List<String> getChannels() {
        return (channels.isEmpty()) ? DiscordBridge.getInstance().getCommandConfig().getChannels() : channels;
    }

    public List<String> getRoles() {
        return (roles.isEmpty()) ? DiscordBridge.getInstance().getCommandConfig().getRoles() : roles;
    }
}
