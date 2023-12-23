package com.k2a.tool.k2a.registry;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k2a.tool.k2a.registry.models.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class RegistryClient {

    @Value("${schema-registry-url:http://localhost:8081}")
    private String registryUrl;

    @Autowired
    private ObjectMapper om;

    private HttpClient client = HttpClient.newBuilder().build();

    public List<String> listSubjects() {
        return httpGet(registryUrl + "/subjects", new TypeReference<>() {
        });
    }

    public Schema getSubjectLatestSchema(String subject) {
        return httpGet(registryUrl + "/subjects/" + subject + "/versions/latest", Schema.class);
    }

    public <T> T httpGet(String url, Class<T> clazz) {
        HttpResponse<String> resp = getStringHttpResponse(URI.create(url), client);
        if (resp.statusCode() != 200) {
            throw new RuntimeException("request fail, status code:" + resp.statusCode() + ", error msg:" + resp.body());
        }
        try {
            return om.readValue(resp.body(), clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T httpGet(String url, TypeReference<T> reference) {
        HttpResponse<String> resp = getStringHttpResponse(URI.create(url), client);
        if (resp.statusCode() != 200) {
            throw new RuntimeException("request fail, status code:" + resp.statusCode() + ", error msg:" + resp.body());
        }
        try {
            return om.readValue(resp.body(), reference);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> getStringHttpResponse(URI url, HttpClient client) {
        try {
            HttpRequest request = HttpRequest.newBuilder().uri(url).build();
            return client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
}
