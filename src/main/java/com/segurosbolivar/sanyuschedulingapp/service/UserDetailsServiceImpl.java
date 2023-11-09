package com.segurosbolivar.sanyuschedulingapp.service;

import com.segurosbolivar.sanyuschedulingapp.entity.UserEntity;
import com.segurosbolivar.sanyuschedulingapp.repository.IUserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    public UserDetailsServiceImpl(IUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    /**
     * Load user details by username for authentication.
     *
     * @param username The username (in this case, email) of the user.
     * @return UserDetails representing the user.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserEntity user = this.userRepository.findFirstByEmailAndIsActive(username, true)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Collection<? extends GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().getName())
        );

        return new User(
                user.getEmail(),
                user.getPassword(),
                true,
                true,
                true,
                true,
                authorities
        );

    }

}