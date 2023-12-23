package com.k2a.tool.k2a.kafka.config;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.AdminClientConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Properties;

@Component
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers:localhost:9092}")
    private String kafkaBrokerUrl;

    @Value("${sasl.jaas.config:}")
    private String saslJaasConfig;

    @Value("${security.protocol:SASL_PLAINTEXT}")
    private String securityProtocol;

    @Value("${sasl.mechanism:PLAIN}")
    private String saslMechanism;

    @Bean
    public Admin admin() {
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokerUrl);
        properties.put("sasl.jaas.config", saslJaasConfig);
        properties.put("security.protocol", securityProtocol);
        properties.put("sasl.mechanism", saslMechanism);
        return Admin.create(properties);
    }
}
