package com.kafka.tuto.kafkaspringcloudstream.handlers;

import com.kafka.tuto.kafkaspringcloudstream.events.PageEvents;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PageEventHandler {

    @Bean
    public Consumer<PageEvents> pageEventConsumer() {
        return event -> {
            System.out.println("**********************");
            System.out.println(event.date());
            System.out.println(event.name());
            System.out.println(event.toString());
            System.out.println("**********************");
        };
    }
}
