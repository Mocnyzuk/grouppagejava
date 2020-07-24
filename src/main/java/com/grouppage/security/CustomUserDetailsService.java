package com.grouppage.security;

import com.grouppage.domain.entity.User;
import com.grouppage.domain.repository.UserRepository;
import com.grouppage.service.auth.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(!userOptional.isPresent()){
            throw new UsernameNotFoundException("User with email "+ email + " doesnt exists!");
        }
        User user = userOptional.get();
        return new Principal(
                user.getEmail(),
                user.getPassword(),
                user.isActivated(),
                true,
                true,
                !user.isDeleted(),
                user.getAuthorities(),
                user.getId()
        );
    }
}
