package com.dakare.streamlabs.service.command;

import org.pircbotx.hooks.ListenerAdapter;
import org.pircbotx.hooks.events.MessageEvent;
import org.pircbotx.hooks.events.UnknownEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;
import org.springframework.context.ApplicationEventPublisher;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandListenerAdapter extends ListenerAdapter {

  private static final Pattern WHISPER_PATTERN = Pattern.compile(":(\\w+)!.* WHISPER \\w+ :(.+)");

  private final ApplicationEventPublisher publisher;

  public CommandListenerAdapter(ApplicationEventPublisher publisher) {
    this.publisher = publisher;
  }

  @Override
  public void onGenericMessage(GenericMessageEvent event) {
    String message = event.getMessage();
    processMessage(event.getUser().getNick(), message, isSubscriber(event));
  }

  private boolean isSubscriber(GenericMessageEvent event) {
    return event instanceof MessageEvent && "1".equals(((MessageEvent) event).getTags().get("subscriber"));
  }

  private void processMessage(String from, String message, boolean subscriber) {
    if (isCommand(message)) {
      String[] command = message.split(" ", 2);
      ChatCommand chatCommand = new ChatCommand();
      chatCommand.setFrom(from.toLowerCase());
      chatCommand.setCommand(command[0].substring(1));
      chatCommand.setSubscriber(subscriber);
      if (command.length == 2) {
        chatCommand.setArgs(command[1].trim());
      }
      publisher.publishEvent(chatCommand);
    }
  }

  @Override
  public void onUnknown(UnknownEvent event) {
    String line = event.getLine();
    Matcher matcher = WHISPER_PATTERN.matcher(line);
    if (matcher.matches()) {
      String from = matcher.group(1).toLowerCase();
      String message = matcher.group(2);
      processMessage(from, message, false);
    }
  }

  boolean isCommand(String message) {
    return message != null && message.matches("![\\p{L}\\p{Digit}_]+( .*)?");
  }
}
