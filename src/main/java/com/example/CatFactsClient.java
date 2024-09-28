package com.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CatFactsClient {

    private static final String URL = "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) throws IOException {
        List<CatFact> catFacts = fetchCatFacts();

        List<CatFact> filteredFacts = catFacts.stream()
                .filter(fact -> fact.getUpvotes() != null && fact.getUpvotes() > 0)
                .collect(Collectors.toList());

        filteredFacts.forEach(System.out::println);
    }

    public static List<CatFact> fetchCatFacts() throws IOException {
        CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)
                        .setSocketTimeout(30000)
                        .setRedirectsEnabled(false)
                        .build())
                .build();

        HttpGet request = new HttpGet(URL);
        CloseableHttpResponse response = httpClient.execute(request);

        try {
            String json = EntityUtils.toString(response.getEntity());

            ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(json, new TypeReference<List<CatFact>>() {});

        } finally {
            response.close();
        }
    }
}
