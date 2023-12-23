package com.k2a.tool.k2a;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k2a.tool.k2a.asyncapi.ModelUtil;
import com.k2a.tool.k2a.asyncapi.models.*;
import com.k2a.tool.k2a.kafka.KafkaClient;
import com.k2a.tool.k2a.kafka.models.Topic;
import com.k2a.tool.k2a.registry.RegistryClient;
import com.k2a.tool.k2a.registry.models.Schema;
import com.k2a.tool.k2a.registry.models.SchemaType;
import com.k2a.tool.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class SpecGenerateService {
    @Autowired
    private RegistryClient registryClient;

    @Autowired
    private KafkaClient kafkaClient;

    @Autowired
    private ObjectMapper om;

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String kafkaBrokerUrl;

    @Value("${schema-registry-url:http://localhost:8081}")
    private String registryUrl;

    public String genSpec(Set<String> topics) {
        AsyncAPI api = new AsyncAPI();
        createInfo(api);
        createServers(api);

        List<Topic> tps = kafkaClient.listAllTopics();
        List<String> subjects = registryClient.listSubjects();
        for (Topic topic : tps) {
            if (!topics.contains(topic.getName())) {
                continue;
            }
            for (String subject : subjects) {
                if (subject.equals(String.format("%s-value", topic.getName()))) {
                    Message message = createMessage(api, topic, subject);

                    ChannelItem channel = new ChannelItem();
                    Operation publishOperation = new Operation();
                    publishOperation.setMessage(ModelUtil.ref("#/components/messages/" + message.getName()));
                    publishOperation.setOperationId(String.format("%s%s", topic.getName(), "PublishMessage"));

                    Operation subscribeOperation = new Operation();
                    subscribeOperation.setMessage(message);
                    subscribeOperation.setMessage(ModelUtil.ref("#/components/messages/" + message.getName()));
                    subscribeOperation.setOperationId(String.format("%s%s", topic.getName(), "SubscribeMessage"));

                    channel.setPublish(publishOperation);
                    channel.setSubscribe(subscribeOperation);

                    api.getChannels().put(topic.getName(), channel);
                }
            }
        }

        try {
            return om.writeValueAsString(api);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private Message createMessage(AsyncAPI api, Topic topic, String subject) {
        Schema schema = registryClient.getSubjectLatestSchema(subject);
        Message message = new Message();
        message.setContentType("application/json");
        if (SchemaType.SCHEMA_TYPE_AVRO.equals(schema.getSchemaType())) {
            message.setSchemaFormat("application/vnd.apache.avro+json;version=1.9.0");
        } else if (SchemaType.SCHEMA_TYPE_JSON.equals(schema.getSchemaType())) {
            message.setSchemaFormat("application/vnd.aai.asyncapi+json;version=2.6.0");
        }

        Map<String, Object> payload = schema.getSchema();
        String name = (String) payload.get("name");
        if (StringUtils.hasLength(name)) {
            message.setName(name);
        } else {
            message.setName(Utils.toLowerCamelCase(topic.getName()));
        }
        message.setPayload(payload);
        api.addComponentsMessage(message.getName(), message);
        return message;
    }

    private static void createInfo(AsyncAPI api) {
        Info info = new Info();
        info.setVersion("1.0");
        info.setTitle("Async API Specification Document for Kafka Application");
        info.setDescription("Async API Specification Document for Kafka Application");
        info.setContact(new Contact("", "https://asyncapi.com", "asyncapi@gmail.com"));
        info.setLicense(new License("Apache 2.0", "https://www.apache.org/licenses/LICENSE-2.0"));
        api.setInfo(info);
    }

    private void createServers(AsyncAPI api) {
        api.getServers().put(ModelUtil.SERVER_KAFKA_NAME,
                new Server(
                        kafkaBrokerUrl,
                        "kafka",
                        "1.0",
                        "kafka broker instance",
                        Collections.emptyList(),
                        Collections.EMPTY_MAP));
        api.getServers().put(ModelUtil.SERVER_REGISTRY_NAME,
                new Server(
                        registryUrl,
                        "kafka",
                        "1.0",
                        "kafka schema registry server",
                        Collections.emptyList(),
                        Collections.EMPTY_MAP));
    }
}
