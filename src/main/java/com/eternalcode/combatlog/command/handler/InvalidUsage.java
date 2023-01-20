package com.eternalcode.combatlog.command.handler;


import com.eternalcode.combatlog.NotificationAnnouncer;
import com.eternalcode.combatlog.config.implementation.MessageConfig;
import dev.rollczi.litecommands.command.LiteInvocation;
import dev.rollczi.litecommands.handle.InvalidUsageHandler;
import dev.rollczi.litecommands.schematic.Schematic;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import panda.utilities.text.Formatter;

public class InvalidUsage implements InvalidUsageHandler<CommandSender> {

    private final MessageConfig messagesConfig;
    private final NotificationAnnouncer notificationAnnouncer;

    public InvalidUsage(MessageConfig messagesConfig, NotificationAnnouncer notificationAnnouncer) {
        this.messagesConfig = messagesConfig;
        this.notificationAnnouncer = notificationAnnouncer;
    }

    @Override
    public void handle(CommandSender commandSender, LiteInvocation invocation, Schematic scheme) {
        for (String schematic : scheme.getSchematics()) {

            Formatter formatter = new Formatter()
                    .register("{USAGE}", schematic);

            this.notificationAnnouncer.announceMessage(commandSender, formatter.format(this.messagesConfig.invalidUsage));
        }
    }
}