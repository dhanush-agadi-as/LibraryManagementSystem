package com.example.LibraryManagement.service;

import com.example.LibraryManagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// here it has only one method it is run only once at the first login time it directly asks the db weather this user present if yes continue the jwt token generation or else through runtime exception and don't produce any type of the jwt token

@Service
public class CustomUserDeatilsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByName(username)
                .orElseThrow(() -> new RuntimeException(username));
    }
}
