package com.k2a.tool.k2a;

import com.k2a.tool.k2a.kafka.models.Topic;

import java.util.List;

public interface KafkaClient {

    List<Topic> listAllTopics();
    
}
