package com.pres.user.service;

import com.pres.user.client.AccountClient;
import com.pres.user.model.dto.NewAccountRequest;
import com.pres.user.model.dto.AccountResponse;
import com.pres.user.model.dto.TransactionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class UserAccountService {
    private final AccountClient client;

    public AccountResponse createNewAccount(NewAccountRequest request) {
        ResponseEntity<AccountResponse> response = client.createAccount(request);
        if (response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        }
        throw new ResponseStatusException(response.getStatusCode(), "Client error while creating new account");
    }

    public AccountResponse sendTransaction(TransactionRequest request) {
        ResponseEntity<AccountResponse> response = client.sendTransaction(request);
        if (response.getStatusCode().is2xxSuccessful()){
            return response.getBody();
        }
        throw new ResponseStatusException(response.getStatusCode(), "Client error while creating transaction");
    }
}
