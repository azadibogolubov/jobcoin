package com.gemini.jobcoin.service;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class GeminiAPIService {
    @Autowired
    RestTemplate restTemplate;

    // Get address info API call.
    public JSONObject getAddress(String address) {
        String addressInfo = restTemplate.getForObject(
                "http://jobcoin.gemini.com/clammy-backward/api/addresses/" + address, String.class);
        return new JSONObject(addressInfo);
    }

    // Post the transaction. Can be used to post to the deposit account, the house, or to the source addresses.
    public void postTransaction(String fromAddress, String toAddress, int amount) {
        String url = "http://jobcoin.gemini.com/clammy-backward/api/transactions";

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("fromAddress", fromAddress);
        map.put("toAddress", toAddress);
        map.put("amount", String.valueOf(amount));

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        this.restTemplate.postForEntity(url, entity, String.class);
    }
}
