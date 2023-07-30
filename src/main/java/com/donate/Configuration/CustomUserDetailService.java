package com.donate.Configuration;

import com.donate.dao.UserRepository;
import com.donate.entities.LoginUser;
import com.donate.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    public UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        return null;

        Optional<User> user = userRepository.findByUsername(username);

        return user.map(LoginUser::new)
                .orElseThrow(()->new UsernameNotFoundException("user not found" +username));
    }
}
