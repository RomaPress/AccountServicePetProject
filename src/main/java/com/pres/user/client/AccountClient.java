package com.pres.user.client;

import com.pres.user.model.dto.NewAccountRequest;
import com.pres.user.model.dto.AccountResponse;
import com.pres.user.model.dto.TransactionRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class AccountClient {
    private static final RestClient restClient = RestClient.create();

    public ResponseEntity<AccountResponse> createAccount(NewAccountRequest accountRequest){
        return restClient.post()
                .uri("http://localhost:8080/api/accounts")
                .contentType(APPLICATION_JSON)
                .body(accountRequest)
                .retrieve().toEntity(AccountResponse.class);
    }

    public ResponseEntity<AccountResponse> sendTransaction(TransactionRequest transactionRequest){
        return restClient.post()
                .uri("http://localhost:8080/api/transactions")
                .contentType(APPLICATION_JSON)
                .body(transactionRequest)
                .retrieve().toEntity(AccountResponse.class);
    }
}
