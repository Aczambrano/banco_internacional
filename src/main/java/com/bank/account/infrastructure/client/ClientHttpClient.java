package com.bank.account.infrastructure.client;

import com.bank.account.domain.exception.ClientNotFoundException;
import com.bank.account.domain.service.ClientPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

@Component
public class ClientHttpClient implements ClientPort {

    private final RestTemplate restTemplate;

    public ClientHttpClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public void validateClientExists(Long clientId) {
        try {
            restTemplate.getForEntity(
                    "http://localhost:8089/clients/" + clientId,
                    ClientResponse.class
            );
        } catch (HttpClientErrorException.NotFound e) {
            throw new ClientNotFoundException(clientId);
        }
    }
}
