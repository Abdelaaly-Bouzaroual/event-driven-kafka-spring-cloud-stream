package com.kafka.tuto.kafkaspringcloudstream.events;

import java.util.Date;

public record PageEvents (String name, String user, Date date, int duration) {

}
