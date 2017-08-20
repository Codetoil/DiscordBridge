package com.nguyenquyhy.discordbridge.config;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class RegistrationServiceConfig {
    @Setting
    private String channelId = "";
    @Setting
    private String optionName = "discord.userid";

    public String getChannelId() {
        return channelId;
    }

    public String getOptionName() {
        return optionName;
    }
}
