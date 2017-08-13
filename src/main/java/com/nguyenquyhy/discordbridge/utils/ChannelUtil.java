package com.nguyenquyhy.discordbridge.utils;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import de.btobastian.javacord.entities.Channel;
import de.btobastian.javacord.entities.message.Message;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;

import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by Hy on 12/4/2016.
 */
public class ChannelUtil {

    private static final String SPECIAL_CHAR = "\u2062";
    private static final String BOT_RANDOM = String.valueOf(new Random().nextInt(100000));

    public static Future<Message> sendMessage(Channel channel, Text content) {
        return sendMessage(channel, content.toPlain());
    }

    public static Future<Message> sendMessage(Channel channel, String content) {
        return channel.sendMessage(ColorUtil.removeColor(content), null, false, getNonce(), null);
    }

    public static void sendSelfDestructingMessage(Channel channel, Text content, long delay, TimeUnit unit) {
        sendSelfDestructingMessage(channel, content.toPlain(), delay, unit);
    }

    public static void sendSelfDestructingMessage(Channel channel, String content, long delay, TimeUnit unit) {
        Sponge.getScheduler().createTaskBuilder()
            .async()
            .execute(task -> {
                Message message;
                try {
                    message = sendMessage(channel, content).get();
                    DiscordUtil.deleteMessageAfter(message, delay, unit);
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            })
            .submit(DiscordBridge.getInstance());
    }

    public static void setDescription(Channel channel, String content) {
        channel.updateTopic(content);
    }

    public static String getNonce() {
        return SPECIAL_CHAR + BOT_RANDOM;
    }
}
