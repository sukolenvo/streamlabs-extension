package com.dakare.streamlabs.service.command;

import lombok.Data;

@Data
public class ChatCommand {

  private String from;
  private String command;
  private String args;
  private boolean subscriber;
}
