package com.pres.user.controller;

import com.pres.user.config.JwtAuthenticationFilter;
import com.pres.user.model.dao.User;
import com.pres.user.model.dto.NewAccountRequest;
import com.pres.user.model.dto.AccountResponse;
import com.pres.user.model.dto.TransactionRequest;
import com.pres.user.service.JwtService;
import com.pres.user.service.UserAccountService;
import com.pres.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/accounts")
@RequiredArgsConstructor
public class UserAccountController {
    private final UserAccountService userAccountService;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping()
    public AccountResponse createAccount(@NonNull HttpServletRequest httpServletRequest,
                                         @RequestBody NewAccountRequest request) {
        Long userId = extractUserId(httpServletRequest);
        request.setUserId(userId);
        return userAccountService.createNewAccount(request);
    }

    @PostMapping("/transaction")
    public AccountResponse sendTransaction(@NonNull HttpServletRequest httpServletRequest,
                                           @RequestBody TransactionRequest request) {
        Long userId = extractUserId(httpServletRequest);
        request.setFromUserId(userId);
        return userAccountService.sendTransaction(request);
    }


    private Long extractUserId(HttpServletRequest request) {
        String authHeader = request.getHeader(JwtAuthenticationFilter.HEADER_NAME);
        String jwt = authHeader.substring(JwtAuthenticationFilter.BEARER_PREFIX.length());
        String username = jwtService.extractUserName(jwt);
        User user = (User) userService.loadUserByUsername(username);
        return user.getId();
    }
}
