package com.dakare.streamlabs.service.command;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;


public class CommandSenderImplTest {
  @Test
  public void isValidMessage() throws Exception {
    assertThat(new CommandSenderImpl(null, null).isValidMessage("asdsdad"))
        .isTrue();
    assertThat(new CommandSenderImpl(null, null).isValidMessage("!asdsdad"))
        .isFalse();
    assertThat(new CommandSenderImpl(null, null).isValidMessage(".asdsdad"))
        .isFalse();
    assertThat(new CommandSenderImpl(null, null).isValidMessage("/asdsdad"))
        .isFalse();
    assertThat(new CommandSenderImpl(null, null).isValidMessage(" asdsdad"))
        .isFalse();
    assertThat(new CommandSenderImpl(null, null).isValidMessage(" asdsdad"))
        .isFalse();
    assertThat(new CommandSenderImpl(null, null).isValidMessage("ывыв"))
        .isTrue();
    assertThat(new CommandSenderImpl(null, null).isValidMessage("中文"))
        .isTrue();
    assertThat(new CommandSenderImpl(null, null).isValidMessage("01asd"))
        .isTrue();
    assertThat(new CommandSenderImpl(null, null).isValidMessage("@01asd"))
        .isTrue();
  }

}