package com.nguyenquyhy.discordbridge.utils;

import de.btobastian.javacord.entities.Channel;
import org.spongepowered.api.text.Text;

import java.util.Random;

/**
 * Created by Hy on 12/4/2016.
 */
public class ChannelUtil {

    private static final String SPECIAL_CHAR = "\u2062";
    private static final String BOT_RANDOM = String.valueOf(new Random().nextInt(100000));

    public static void sendMessage(Channel channel, Text content) {
        sendMessage(channel, content.toPlain());
    }

    public static void sendMessage(Channel channel, String content) {
        channel.sendMessage(ColorUtil.removeColor(content), null, false, getNonce(), null);
    }

    public static void setDescription(Channel channel, String content) {
        channel.updateTopic(content);
    }

    public static String getNonce() {
        return SPECIAL_CHAR + BOT_RANDOM;
    }
}
