package com.nguyenquyhy.discordbridge.listeners;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.models.ChannelConfig;
import com.nguyenquyhy.discordbridge.models.GlobalConfig;
import com.nguyenquyhy.discordbridge.utils.ChannelUtil;
import com.nguyenquyhy.discordbridge.utils.ConfigUtil;
import de.btobastian.javacord.DiscordAPI;
import de.btobastian.javacord.entities.Channel;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.achievement.GrantAchievementEvent;
import org.spongepowered.api.event.filter.Getter;

public class AchievementListener {

    @Listener
    public void onAchievement(GrantAchievementEvent.TargetPlayer event, @Getter("getTargetEntity") Player player) {

        if (event.isMessageCancelled() || player.getAchievementData().achievements().contains(event.getAchievement())) return;

        DiscordBridge mod = DiscordBridge.getInstance();
        GlobalConfig config = mod.getConfig();
        DiscordAPI client = mod.getBotClient();
        if (client != null) {
            for (ChannelConfig channelConfig : config.channels) {
                if (StringUtils.isNotBlank(channelConfig.discordId) && channelConfig.discord != null) {
                    String template = ConfigUtil.get(channelConfig.discord.achievementTemplate, null);
                    if (StringUtils.isNotBlank(template)) {
                        Channel channel = client.getChannelById(channelConfig.discordId);
                        String message = template
                                .replace("%a", player.getName()) // name of player
                                .replace("%s", event.getAchievement().getName());
                        ChannelUtil.sendMessage(channel, message);
                    }
                }
            }
        }
    }
}
