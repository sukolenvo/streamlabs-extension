package com.dakare.streamlabs.service.command;

import org.junit.Test;
import org.mockito.Mockito;
import org.pircbotx.hooks.events.UnknownEvent;
import org.springframework.context.ApplicationEventPublisher;

import static org.assertj.core.api.Assertions.assertThat;

public class CommandListenerAdapterTest {

    @Test
    public void isCommand() throws Exception {
        CommandListenerAdapter adapter = new CommandListenerAdapter(null);
        assertThat(adapter.isCommand("some"))
                .isFalse();
        assertThat(adapter.isCommand("!some"))
                .isTrue();
        assertThat(adapter.isCommand("!some arg"))
                .isTrue();
        assertThat(adapter.isCommand("some arg"))
                .isFalse();
        assertThat(adapter.isCommand("!кот"))
                .isTrue();
    }

    @Test
    public void whisperMessage() throws Exception {
        ApplicationEventPublisher mock = Mockito.mock(ApplicationEventPublisher.class);
        CommandListenerAdapter commandListenerAdapter = new CommandListenerAdapter(mock);

        commandListenerAdapter.onUnknown(new UnknownEvent(null,
                ":dakara1!dakara1@dakara1.tmi.twitch.tv WHISPER dakare1 :1"));

        Mockito.verify(mock, Mockito.never()).publishEvent(Mockito.any());
    }

    @Test
    public void whisperCommand() throws Exception {
        String line = ":dakara1!dakara1@dakara1.tmi.twitch.tv WHISPER Dakare1 :!buy_aegis aaaa";

        ApplicationEventPublisher mock = Mockito.mock(ApplicationEventPublisher.class);
        CommandListenerAdapter commandListenerAdapter = new CommandListenerAdapter(mock);

        commandListenerAdapter.onUnknown(new UnknownEvent(null, line));

        ChatCommand chatCommand = new ChatCommand();
        chatCommand.setFrom("dakara1");
        chatCommand.setCommand("buy_aegis");
        chatCommand.setArgs("aaaa");

        Mockito.verify(mock, Mockito.times(1)).publishEvent(chatCommand);
    }
}