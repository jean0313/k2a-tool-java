package com.k2a.tool.k2a.registry;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.k2a.tool.k2a.RegistryClient;
import com.k2a.tool.k2a.registry.models.Schema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Component
public class RegistryClientImpl implements RegistryClient {

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
        try {
            HttpResponse<String> resp = getStringHttpResponse(URI.create(url), client);
            return om.readValue(resp.body(), clazz);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public <T> T httpGet(String url, TypeReference<T> reference) {
        try {
            HttpResponse<String> resp = getStringHttpResponse(URI.create(url), client);
            return om.readValue(resp.body(), reference);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private HttpResponse<String> getStringHttpResponse(URI url, HttpClient client) throws IOException, InterruptedException {
        HttpResponse<String> response = null;
        HttpRequest request = HttpRequest.newBuilder().uri(url).build();
        response = client.send(request, HttpResponse.BodyHandlers.ofString());

        Assert.notNull(response, "ERROR: request fail, url:" + url);
        Assert.isTrue(response.statusCode() == 200, "ERROR: request fail, status code:" + response.statusCode() + ", error msg:" + response.body());
        return response;
    }
}
