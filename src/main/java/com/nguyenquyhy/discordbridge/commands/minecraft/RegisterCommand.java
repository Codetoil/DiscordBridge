package com.nguyenquyhy.discordbridge.commands.minecraft;

import com.nguyenquyhy.discordbridge.DiscordBridge;
import com.nguyenquyhy.discordbridge.registration.UserRegistration;
import com.nguyenquyhy.discordbridge.registration.UserRegistrationEvent;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class RegisterCommand implements CommandExecutor {

    DiscordBridge mod = DiscordBridge.getInstance();

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (!(src instanceof Player)) {
            throw new CommandException(Text.of("This command can only be used by a player!"));
        }

        Player player = (Player) src;
        String code = args.<String>getOne("code")
            .orElseThrow(() -> new CommandException(Text.of(TextColors.RED, "A registration code is required.")));

        Optional<UserRegistration> oUserRegistration = mod.getRegistrationService().getRegistrations().stream().filter(r -> r.getCode().equals(code)).findAny();
        if (!oUserRegistration.isPresent()) {
            throw new CommandException(Text.of(TextColors.RED, "Invalid registration code."));
        } else if (oUserRegistration.get().getMinecraftUsername().equalsIgnoreCase(player.getName())) {
            Sponge.getEventManager().post(new UserRegistrationEvent(player, oUserRegistration.get().getDiscordUser()));
            player.sendMessage(Text.of(TextColors.GREEN, "Successfully registered Discord account."));

            return CommandResult.success();
        } else {
            throw new CommandException(Text.of(TextColors.RED, "Invalid registration code."));
        }
    }
}
