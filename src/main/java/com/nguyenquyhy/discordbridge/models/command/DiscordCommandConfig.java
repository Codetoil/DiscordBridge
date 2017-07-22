package com.nguyenquyhy.discordbridge.models.command;

import com.nguyenquyhy.discordbridge.commands.discord.*;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class DiscordCommandConfig {
    @Setting(value = "online")
    private OnlineCommand.Config onlineCommand;
    @Setting(value = "purge")
    private PurgeCommand.Config purgeCommand;
    @Setting(value = "reload")
    private ReloadCommand.Config reloadCommand;
    @Setting(value = "tps")
    private TpsCommand.Config tpsCommand;
    @Setting(value = "timings")
    private TimingsCommand.Config timingsCommand;

    public DiscordCommandConfig() {
        onlineCommand = new OnlineCommand.Config();
        purgeCommand = new PurgeCommand.Config();
        reloadCommand = new ReloadCommand.Config();
        tpsCommand = new TpsCommand.Config();
        timingsCommand = new TimingsCommand.Config();
    }

    public OnlineCommand.Config getOnlineCommand() {
        return onlineCommand;
    }

    public PurgeCommand.Config getPurgeCommand() {
        return purgeCommand;
    }

    public ReloadCommand.Config getReloadCommand() {
        return reloadCommand;
    }

    public TpsCommand.Config getTpsCommand() {
        return tpsCommand;
    }

    public TimingsCommand.Config getTimingsCommand() {
        return timingsCommand;
    }
}
