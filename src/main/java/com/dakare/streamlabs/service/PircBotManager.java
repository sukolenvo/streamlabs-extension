package com.dakare.streamlabs.service;

import lombok.RequiredArgsConstructor;
import org.pircbotx.PircBotX;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@RequiredArgsConstructor
public class PircBotManager implements Runnable {

  private final PircBotX bot;

  @PostConstruct
  public void setup() {
    new Thread(this).start();
  }

  @Override
  public void run() {
    try {
      bot.startBot();
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  @PreDestroy
  public void destroy() {
    bot.stopBotReconnect();
  }
}
