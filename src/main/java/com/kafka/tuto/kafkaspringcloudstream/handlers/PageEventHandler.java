package com.kafka.tuto.kafkaspringcloudstream.handlers;

import com.kafka.tuto.kafkaspringcloudstream.events.PageEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

@Component
public class PageEventHandler {

    @Bean
    public Consumer<PageEvent> pageEventConsumer() {
        return (input)-> {
            System.out.println("*******************");
            System.out.println(input.toString());
            System.out.println("*******************");
        };
    }
}
