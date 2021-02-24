package com.dakare.streamlabs.service.command;

public interface CommandSender {

  default void timeout(String user, int duration) {
    sendCommand("/timeout " + user + " " + duration);
  }

  void sendCommand(String command);

  void sendMessage(String message);

  default void sendMessageToUser(String user, String message) {
    sendMessage("@" + user + " " + message);
  }
}
