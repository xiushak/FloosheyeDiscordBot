package discord.bot.commands;

import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

public class CheckOnlineCommand implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        String message = event.getMessageContent();
        if (message.equalsIgnoreCase("fish?")) {
            event.getChannel().sendMessage("IM ONLINE AND YOUR IMAGES AREN'T SAFE");
        }
    }
}
