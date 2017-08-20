package com.nguyenquyhy.discordbridge.commands.discord;

import com.nguyenquyhy.discordbridge.registration.UserRegistration;
import com.nguyenquyhy.discordbridge.config.command.CoreCommandConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.DiscordUtil;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.Server;
import de.btobastian.javacord.entities.User;
import de.btobastian.javacord.entities.message.Message;
import de.btobastian.sdcf4j.Command;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.service.permission.PermissionService;
import org.spongepowered.api.service.user.UserStorageService;

import java.util.Optional;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class RegisterCommand extends DiscordCommand {

    private static String template = "`To complete registration, log in to Minecraft and run /discord register %s`";
    private static String invalidArguments = "Incorrect command usage. Correct usage is %sregister <minecraft username>";
    private static String alreadyRegistered = "%s is already registered!";

    public RegisterCommand(Server server) {
        super(mod.getCommandConfig().getDiscordCommands().getTpsCommand(), server);
    }

    @ConfigSerializable
    public static class Config extends CoreCommandConfig {
        @Setting
        public String template;
        @Setting
        public String invalidArguments;
        @Setting
        public String alreadyRegistered;
        @Setting(value = "Code-Length")
        public int code = 6;

        public Config() {
            super(true, "tps");
            template = RegisterCommand.template;
            invalidArguments = RegisterCommand.invalidArguments;
            alreadyRegistered = RegisterCommand.alreadyRegistered;
        }
    }

    @Command(aliases = {"register"}, description = "", usage = "")
    public void onRegisterCommand(User user, Channel channel, Message command, String[] parameters) {

        if (!isEnabled() || !isSupportedChannel(channel) || !hasPermission(user))
            return;

        RegisterCommand.Config config = (Config) this.config;

        if (parameters.length < 1 || parameters.length > 1) {
            ChannelUtil.sendSelfDestructingMessage(channel, String.format(config.invalidArguments, globalConfig.getPrefix()), expiration, TimeUnit.SECONDS);
            return;
        }
        String username = parameters[0];

        UserStorageService userStorageService = Sponge.getServiceManager().provideUnchecked(UserStorageService.class);
        if(userStorageService.get(username).isPresent()){
            PermissionService permissionService = Sponge.getServiceManager().provideUnchecked(PermissionService.class);
            org.spongepowered.api.entity.living.player.User user1 = userStorageService.get(username).get();
            if (permissionService.getUserSubjects().get(user1.getIdentifier()).getOption("discord.user").isPresent()) {
                ChannelUtil.sendSelfDestructingMessage(channel, String.format(config.alreadyRegistered, user1.getName()), expiration, TimeUnit.SECONDS);
                return;
            }
        }

        String code;
        Optional<UserRegistration> oUserRegistration = mod.getRegistrationService().getRegistrations().stream().filter(r -> r.getMinecraftUsername().equals(username)).findAny();
        if (oUserRegistration.isPresent()) {
            code = oUserRegistration.get().getCode();
        } else {
            code = StringUtils.leftPad(String.valueOf(new Random().nextInt((int) Math.pow(10, config.code))), config.code, '0');
            mod.getRegistrationService().getRegistrations().add(new UserRegistration(user, username, code));
        }

        // Delete the command message
        DiscordUtil.deleteMessageAfter(command, expiration, TimeUnit.SECONDS);
        user.sendMessage(String.format(config.template, code));
    }
}
