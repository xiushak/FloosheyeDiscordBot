package discord.bot.commands;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import model.FaceRecognitionFishEyeModel;
import model.SimpleModel;
import model.facerecognition.opencv.cat.CatRecognitionOPENCV;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageAttachment;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;
import view.BasicJPGView;
import view.SimpleView;

public class CatCommand implements MessageCreateListener {

  @Override
  public void onMessageCreate(MessageCreateEvent event) {
    Message message = event.getMessage();
    String content = message.getContent();
    if (content.equalsIgnoreCase("meow")) {
      for (MessageAttachment attachment : message.getAttachments()) {
        if (attachment.isImage()) {
          System.out.println("Image detected!");
          try {
            BufferedImage image = attachment.asImage().join();
            String name = attachment.getFileName();
            SimpleModel model = new FaceRecognitionFishEyeModel(new CatRecognitionOPENCV());
            SimpleView view = new BasicJPGView(model);
            model.setImage(image);
            model.processImage(0, 0);
            view.outputImage("discordImages/" + name);

            Random r = new Random();
            String nya = "nya";
            nya += "a".repeat(r.nextInt(10));
            new MessageBuilder()
                .append(nya)
                .addAttachment(new File("discordImages/" + name))
                .send(event.getChannel());

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            scheduler.scheduleAtFixedRate(() -> new File("discordImages/" + name).delete(), 1, 3L,
                TimeUnit.SECONDS);
            System.out.println("Image fisheyed by command (cat)");
          }
          catch (IOException | IllegalArgumentException e) {
            event.getChannel().sendMessage("image failed to fisheye :(");
          }
        }
      }
    }
  }
}
