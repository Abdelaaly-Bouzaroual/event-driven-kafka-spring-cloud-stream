package com.kafka.tuto.kafkaspringcloudstream.handlers;

import com.kafka.tuto.kafkaspringcloudstream.events.PageEvents;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.TimeWindows;
import org.apache.kafka.streams.kstream.internals.TimeWindow;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Date;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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

    @Bean
    public Supplier<PageEvents> pageEventSupplier() {
        return () ->{
               return new PageEvents(
                Math.random() > 0.5 ? "P1" : "P2",
                Math.random() > 0.5 ? "U1" : "U2",
                new Date(),
                10 + new Random().nextInt(10000)
        );
        };
    }

    @Bean
    public Function<KStream<String, PageEvents>, KStream<String, Long>> kStreamFunction() {
        return (event) -> event
                .filter((k, v) -> v.duration() > 100)
                .map((k, v) -> new KeyValue<>(v.name(), v.duration() + 10L)) // Conversion en Long ici
                .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofMillis(5000))) // 1. Correction du 'of'
                .count(Materialized.as("count-store"))
                .toStream()
                .map((k, v  ) -> new KeyValue<>(k.key(), v));
    }
}
