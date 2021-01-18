package com.interview.geobyte.service.serviceImpl;

import com.interview.geobyte.dao.PortalUserRepository;
import com.interview.geobyte.exception.GeoByteException;
import com.interview.geobyte.model.PortalUser;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final PortalUserRepository portalUserRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        PortalUser portalUser = portalUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email %s does not exist", email)));
        if (!portalUser.isEnabled()){
            throw new GeoByteException(String.format("Account with email %s has not been verified", email));
        }
        return new User(portalUser.getEmail(), portalUser.getPassword(), getAuthorities("USER"));
    }

    private Collection<? extends GrantedAuthority> getAuthorities(String role){
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
}
