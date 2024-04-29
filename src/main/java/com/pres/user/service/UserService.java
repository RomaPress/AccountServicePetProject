package com.pres.user.service;

import com.pres.user.model.dao.User;
import com.pres.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService{
    private final UserRepository repository;

    public User create(User user) {
        if (repository.existsByUsername(user.getUsername())) {
            throw new UsernameNotFoundException("Username " + user.getUsername() + " already exists");
        }
        if (repository.existsByEmail(user.getEmail())) {
            throw new UsernameNotFoundException("Email " + user.getEmail() + " already exists");
        }
        return repository.saveAndFlush(user);
    }

    public User getByUsername(String username) {
        return repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username " + username + " not found"));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return getByUsername(username);
    }
}
