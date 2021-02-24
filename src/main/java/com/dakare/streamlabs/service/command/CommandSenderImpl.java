package com.dakare.streamlabs.service.command;

import com.dakare.streamlabs.config.properties.BotConfigurationProperties;

import org.apache.commons.lang3.StringUtils;
import org.pircbotx.PircBotX;

public class CommandSenderImpl implements CommandSender {

  private final PircBotX botX;
  private final BotConfigurationProperties configurationProperties;

  public CommandSenderImpl(PircBotX botX, BotConfigurationProperties configurationProperties) {
    this.botX = botX;
    this.configurationProperties = configurationProperties;
  }

  @Override
  public void sendCommand(String command) {
    doSend(command);
  }

  @Override
  public void sendMessage(String message) {
    if (isValidMessage(message)) {
      doSend(message);
    }
  }

  private void doSend(String message) {
    botX.sendIRC().message("#" + configurationProperties.getChannel(), message);
  }

  boolean isValidMessage(String message) {
    return !StringUtils.isEmpty(message) && (Character.isLetter(message.charAt(0))
        || Character.isDigit(message.charAt(0))
    || message.charAt(0) == '@');
  }
}
