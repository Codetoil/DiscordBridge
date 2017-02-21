package com.nguyenquyhy.discordbridge.commands.minecraft;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

/**
 * Created by Hy on 1/5/2016.
 */
public class ReloadCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource commandSource, CommandContext commandContext) throws CommandException {
        Text message = DiscordBridge.reload();
        commandSource.sendMessage(message);
        return (message.getColor().equals(TextColors.GREEN)) ? CommandResult.success() : CommandResult.empty();
    }
}
