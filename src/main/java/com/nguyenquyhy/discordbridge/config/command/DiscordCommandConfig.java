package com.nguyenquyhy.discordbridge.config.command;

import com.nguyenquyhy.discordbridge.commands.discord.*;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class DiscordCommandConfig {
    @Setting(value = "clear")
    private ClearCommand.Config clearCommand = new ClearCommand.Config();
    @Setting(value = "online")
    private OnlineCommand.Config onlineCommand = new OnlineCommand.Config();
    @Setting(value = "purge")
    private PurgeCommand.Config purgeCommand= new PurgeCommand.Config();
    @Setting(value = "reload")
    private ReloadCommand.Config reloadCommand = new ReloadCommand.Config();
    @Setting(value = "register")
    private RegisterCommand.Config registerCommand = new RegisterCommand.Config();
    @Setting(value = "tps")
    private TpsCommand.Config tpsCommand = new TpsCommand.Config();
    @Setting(value = "timings")
    private TimingsCommand.Config timingsCommand = new TimingsCommand.Config();

    public ClearCommand.Config getClearCommand() {
        return clearCommand;
    }

    public OnlineCommand.Config getOnlineCommand() {
        return onlineCommand;
    }

    public PurgeCommand.Config getPurgeCommand() {
        return purgeCommand;
    }

    public RegisterCommand.Config getRegisterCommand() {
        return registerCommand;
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
