package com.donate.services;

import com.donate.dao.UserRepository;
import com.donate.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public PasswordEncoder passwordEncoder;

    public User registerUser(User user){
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);

    }

//    public String forgotPassword(String email) {
//       User user =  userRepository.findByEmail(email)
//                .orElseThrow(
//                ()-> new RuntimeException("User not Found with this email" + email)
//        );
//        return null;
//    }
}
