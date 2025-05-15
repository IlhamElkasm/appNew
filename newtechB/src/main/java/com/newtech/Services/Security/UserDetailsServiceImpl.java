package com.newtech.Services.Security;


import com.newtech.Model.User;
import com.newtech.Repositories.UserRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    // Dans UserDetailsServiceImpl.java - Ne pas ajouter "ROLE_"
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.toString()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getMotDePasse(),
                user.isActive(),
                true,
                true,
                true,
                authorities
        );
    }
//    @Override
//    @Transactional(readOnly = true)
//    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
//
//        Set<SimpleGrantedAuthority> authorities = user.getRoles().stream()
//                .map(role -> new SimpleGrantedAuthority("" + role))
//                .collect(Collectors.toSet());
//
//        return new org.springframework.security.core.userdetails.User(
//                user.getEmail(),
//                user.getMotDePasse(),
//                user.isActive(),
//                true,
//                true,
//                true,
//                authorities
//        );
//    }
}