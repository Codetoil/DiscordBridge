package com.nguyenquyhy.discordbridge;

import com.google.inject.Inject;
import com.nguyenquyhy.discordbridge.commands.MinecraftCommands;
import com.nguyenquyhy.discordbridge.database.IStorage;
import com.nguyenquyhy.discordbridge.listeners.AchievementListener;
import com.nguyenquyhy.discordbridge.listeners.ChatListener;
import com.nguyenquyhy.discordbridge.listeners.ClientConnectionListener;
import com.nguyenquyhy.discordbridge.listeners.DeathListener;
import com.nguyenquyhy.discordbridge.logics.ConfigHandler;
import com.nguyenquyhy.discordbridge.logics.LoginHandler;
import com.nguyenquyhy.discordbridge.config.ChannelConfig;
import com.nguyenquyhy.discordbridge.config.GlobalConfig;
import com.nguyenquyhy.discordbridge.config.command.CommandConfig;
import com.nguyenquyhy.discordbridge.registration.UserRegistrationService;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.ErrorMessages;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.spongepowered.api.Game;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartingServerEvent;
import org.spongepowered.api.event.game.state.GameStoppingServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hy on 1/4/2016.
 */
@Plugin(id = "discordbridge", name = "Discord Bridge", version = "2.4.2",
    description = "A Sponge plugin to connect your Minecraft server with Discord", authors = {"Hy", "Mohron"})
public class DiscordBridge {

    private DiscordAPI consoleClient = null;
    private final Map<UUID, DiscordAPI> humanClients = new HashMap<>();
    private DiscordAPI botClient = null;
    private UserRegistrationService registrationService;

    private final Set<UUID> unauthenticatedPlayers = new HashSet<>(100);

    @Inject
    private Logger logger;

    @Inject
    @ConfigDir(sharedRoot = false)
    private Path configDir;

    private GlobalConfig config;
    private CommandConfig commandConfig;

    @Inject
    private Game game;

    @Inject
    private PluginContainer pluginContainer;

    private IStorage storage;

    private static DiscordBridge instance;
    private int playerCount = 0;

    @Listener
    public void onPreInitialization(GamePreInitializationEvent event) throws IOException, ObjectMappingException {
        instance = this;
        config = ConfigHandler.loadConfiguration();
        commandConfig = ConfigHandler.loadCommandConfiguration();

        Sponge.getEventManager().registerListeners(this, new ChatListener());
        Sponge.getEventManager().registerListeners(this, new ClientConnectionListener());
        Sponge.getEventManager().registerListeners(this, new AchievementListener());
        Sponge.getEventManager().registerListeners(this, new DeathListener());
    }

    @Listener
    public void onServerStarting(GameStartingServerEvent event) {
        MinecraftCommands.register();
        LoginHandler.loginBotAccount();
        registrationService = new UserRegistrationService();
    }


    @Listener
    public void onServerStop(GameStoppingServerEvent event) {
        if (botClient != null) {
            for (ChannelConfig channelConfig : config.channels) {
                if (StringUtils.isNotBlank(channelConfig.discordId)
                    && channelConfig.discord != null
                    && StringUtils.isNotBlank(channelConfig.discord.serverDownMessage)) {
                    Channel channel = botClient.getChannelById(channelConfig.discordId);
                    if (channel != null) {
                        ChannelUtil.sendSelfDestructingMessage(channel, channelConfig.discord.serverDownMessage, 15, TimeUnit.MINUTES);
                        // TODO Update with template
                        // ChannelUtil.setDescription(channel, "Offline");
                    } else {
                        ErrorMessages.CHANNEL_NOT_FOUND.log(channelConfig.discordId);
                    }
                }
            }


        }
    }

    public static Text reload() {
        try {
            GlobalConfig config = ConfigHandler.loadConfiguration();
            DiscordBridge.getInstance().setConfig(config);
            CommandConfig commandConfig = ConfigHandler.loadCommandConfiguration();
            DiscordBridge.getInstance().setConfig(commandConfig);
            getInstance().getLogger().info("Configuration reloaded!");
            return Text.of(TextColors.GREEN, "Configuration reloaded!");
        } catch (Exception e) {
            getInstance().getLogger().error("Cannot reload configuration!", e);
            return Text.of(TextColors.RED, "Unable to reload configuration!");
        }
    }

    public static DiscordBridge getInstance() {
        return instance;
    }

    public UserRegistrationService getRegistrationService() {
        return registrationService;
    }

    public Game getGame() {
        return game;
    }

    public Path getConfigDir() {
        return configDir;
    }

    public GlobalConfig getConfig() {
        return config;
    }

    public CommandConfig getCommandConfig() {
        return commandConfig;
    }

    public void setConfig(GlobalConfig config) {
        this.config = config;
    }

    public void setConfig(CommandConfig commandConfig) {
        this.commandConfig = commandConfig;
    }

    public Logger getLogger() {
        return logger;
    }

    public IStorage getStorage() {
        return storage;
    }

    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    public DiscordAPI getBotClient() {
        return botClient;
    }

    public void setBotClient(DiscordAPI botClient) {
        this.botClient = botClient;
    }

    public Map<UUID, DiscordAPI> getHumanClients() {
        return humanClients;
    }

    public Set<UUID> getUnauthenticatedPlayers() {
        return unauthenticatedPlayers;
    }

    public void addClient(UUID player, DiscordAPI client) {
        if (player == null) {
            consoleClient = client;
        } else {
            humanClients.put(player, client);
        }
    }

    public void updatePlayerCount(int change) {
        playerCount += change;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void removeAndLogoutClient(UUID player) {
        if (player == null) {
            consoleClient.disconnect();
            consoleClient = null;
        } else {
            if (humanClients.containsKey(player)) {
                DiscordAPI client = humanClients.get(player);
                client.disconnect();
                humanClients.remove(player);
            }
        }
    }
}
