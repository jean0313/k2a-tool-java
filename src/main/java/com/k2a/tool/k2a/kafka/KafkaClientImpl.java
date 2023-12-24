package com.k2a.tool.k2a.kafka;

import com.k2a.tool.k2a.KafkaClient;
import com.k2a.tool.k2a.kafka.exceptions.KafkaException;
import com.k2a.tool.k2a.kafka.models.Topic;
import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.TopicListing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class KafkaClientImpl implements KafkaClient {

    @Autowired
    private Admin adminClient;

    public List<Topic> listAllTopics() {
        try {
            Collection<TopicListing> topics = adminClient.listTopics().listings().get();
            if (!CollectionUtils.isEmpty(topics)) {
                return topics.stream()
                        .filter(v -> !v.name().startsWith("_"))
                        .map(v -> new Topic(v.name()))
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            throw new KafkaException(e);
        }
        return Collections.emptyList();
    }
}
