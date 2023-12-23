package com.k2a.tool.k2a.kafka;

import com.k2a.tool.k2a.kafka.exceptions.KafkaException;
import com.k2a.tool.k2a.kafka.models.Topic;
import org.apache.kafka.clients.admin.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Component
public class KafkaClient {

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

    private static Admin admin() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        properties.put("sasl.jaas.config", "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"admin\" password=\"admin-secret\";");
        properties.put("security.protocol", "SASL_PLAINTEXT");
        properties.put("sasl.mechanism", "PLAIN");
        return Admin.create(properties);
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ListTopicsResult ret = admin().listTopics();
        Set<String> strings = ret.names().get();
        System.out.println(strings);

//        Collection<TopicListing> topics = ret.listings().get();
//        System.out.println(topics);

        DescribeTopicsResult describeTopicsResult = admin().describeTopics(strings);
        Map<String, TopicDescription> map = describeTopicsResult.allTopicNames().get();
        System.out.println(map);
    }


}
