package com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.service;


import com.github.Real_TimeDeliveryTrackingSystem.Real_TimeDeliveryTrackingSystem.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class JwtDetailsService implements UserDetailsService {

    private static final String USER_NOT_FOUND_MESSAGE = "An User with that email %s was not found!";
//    private static final String ROLE_PREFIX = "ROLE_";

    private final UserRepository userRepository;

    @Autowired
    public JwtDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        return this.userRepository.findUserByEmail(username).map( user->
        {
            SimpleGrantedAuthority authorities = new SimpleGrantedAuthority(user.getUserProfile().getProfile());

            return new User(user.getEmail(), user.getPassword(), Collections.singleton(authorities));
        }).orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MESSAGE, username)));
    }
}
