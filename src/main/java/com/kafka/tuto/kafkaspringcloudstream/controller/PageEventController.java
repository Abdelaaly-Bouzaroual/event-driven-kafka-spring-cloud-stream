package com.kafka.tuto.kafkaspringcloudstream.controller;

import com.kafka.tuto.kafkaspringcloudstream.events.PageEvents;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.Random;

@RestController
@AllArgsConstructor
public class PageEventController {
    private StreamBridge streamBridge;
    private InteractiveQueryService interactiveQueryService;
    @GetMapping("/publish")
    public PageEvents publish(String name, String topic){
        PageEvents event = new PageEvents(
                name,
                Math.random()>0.5?"U1":"U2",
                new Date(),
                 10+new Random().nextInt(1000));

        streamBridge.send(topic, event);
        return event;
    }
}
