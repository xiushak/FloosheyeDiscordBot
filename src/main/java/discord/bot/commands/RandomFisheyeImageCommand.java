package discord.bot.commands;

import model.FaceRecognitionBigBrainModel;
import model.FaceRecognitionFishEyeModel;
import model.SimpleModel;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import view.BasicJPGView;
import view.SimpleView;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RandomFisheyeImageCommand implements MessageCreateListener {
    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        Message message = event.getMessage();
        String content = message.getContent();
        if (content.equalsIgnoreCase("fish") || content.equalsIgnoreCase("brain")) {
            return;
        }
        for (MessageAttachment attachment : message.getAttachments()) {
            if (!event.getMessageAuthor().isBotUser() && attachment.isImage()) {
                System.out.println("Image detected!");
                Random r = new Random();
                if (r.nextInt(2) != 0) {
                    String[] avoid = {"you live this time...",
                            "I'll get you next time >:)",
                            "send it again, I DARE YOU"};
                    System.out.println("no processing made");
                    new MessageBuilder()
                            .append(avoid[r.nextInt(avoid.length)])
                            .send(event.getChannel());
                    return;
                }
                try {
                    BufferedImage image = attachment.downloadAsImage().join();
                    String name = attachment.getFileName();
                    SimpleModel model;
                    if (r.nextInt(2) != 0) {
                        model = new FaceRecognitionFishEyeModel();
                    } else {
                        model = new FaceRecognitionBigBrainModel();
                    }
                    SimpleView view = new BasicJPGView(model);
                    model.setImage(image);
                    model.processImage(0, 0);
                    view.outputImage("discordImages/" + name);

                    String[] messages = {"GOTTEM! NOT SAFE TO SEND IT HERE HAHAHAA",
                            "SUcks to suck, enjoy!",
                            "whoops, missclicked ;)",
                            ":levitate:"};
                    new MessageBuilder()
                            .append(messages[r.nextInt(messages.length)])
                            .addAttachment(new File("discordImages/" + name))
                            .send(event.getChannel());

                    ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                    scheduler.scheduleAtFixedRate(() -> new File("discordImages/" + name).delete(), 1, 3L, TimeUnit.SECONDS);
                    System.out.println("Image fisheyed");
                } catch (IOException | IllegalArgumentException e) {
                    event.getChannel().sendMessage("image failed to fisheye :(");
                }
            }
        }
    }
}
