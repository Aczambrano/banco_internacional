package com.bank.account.infrastructure.client;

import com.bank.account.domain.exception.ClientNotFoundException;
import com.bank.account.application.port.out.ClientPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class ClientHttpClient implements ClientPort {

    private final RestTemplate restTemplate;

    @Value("${bank.account.get.client.url}")
    private String clientUrl;

    public ClientHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void validateClientExists(Long clientId) {
        try {
            restTemplate.getForEntity(
                    clientUrl  + clientId,
                    ClientResponse.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new ClientNotFoundException(clientId);
        }
    }
}
