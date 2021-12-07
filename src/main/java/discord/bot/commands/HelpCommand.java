package discord.bot.commands;

import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class HelpCommand implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String message = event.getMessageContent();
        if (message.equalsIgnoreCase("fish help")) {
            new MessageBuilder()
                    .append("- Chance of applying the fisheye effect on an image sent while the bot is online! (disabled)\n")
                    .append("- Use \'fish?\' to check if the bot is online\n")
                    .append("- Upload an image attached to a message saying \'fish\' to force apply the fisheye filter\n")
                    .append("- Upload an image attached to a message saying \'brain\' to force apply the big-brain filter\n")
                    .send(event.getChannel());
        }
    }
}
