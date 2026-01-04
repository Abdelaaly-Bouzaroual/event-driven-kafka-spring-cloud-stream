package com.kafka.tuto.kafkaspringcloudstream.controller;

import com.kafka.tuto.kafkaspringcloudstream.events.PageEvents;
import lombok.AllArgsConstructor;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Windowed;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.apache.kafka.streams.state.ReadOnlyWindowStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.binder.kafka.streams.InteractiveQueryService;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

    @GetMapping(value = "/analytics", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Long>> analytics() {
        return Flux.interval(Duration.ofSeconds(1))
                .map(seq -> {
                    Map<String, Long> stringLongMap = new HashMap<>();

                    // 1. On demande un WindowStore au lieu d'un KeyValueStore
                    ReadOnlyWindowStore<String, Long> stats = interactiveQueryService.getQueryableStore(
                            "count-store",
                            QueryableStoreTypes.windowStore() // <--- Changement ici
                    );

                    // 2. L'itérateur retourne des clés de type Windowed<String>
                    KeyValueIterator<Windowed<String>, Long> keyValueIterator = stats.all();

                    while (keyValueIterator.hasNext()) {
                        KeyValue<Windowed<String>, Long> next = keyValueIterator.next();
                        // 3. On extrait la vraie clé (String) de l'objet Windowed
                        stringLongMap.put(next.key.key(), next.value);
                    }
                    return stringLongMap;
                });
    }
}
