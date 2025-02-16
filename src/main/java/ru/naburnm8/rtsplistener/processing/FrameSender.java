package ru.naburnm8.rtsplistener.processing;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class FrameSender {
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${secondaryServiceUrl}")
    private String secondaryServiceURL;

    public void sendFrame(FrameData frameData){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<FrameData> request = new HttpEntity<>(frameData, headers);
        //System.out.println(frameData);
        restTemplate.postForObject(secondaryServiceURL, request, String.class);
    }
}
