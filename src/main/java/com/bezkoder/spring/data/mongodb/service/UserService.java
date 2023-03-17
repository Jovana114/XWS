package com.bezkoder.spring.data.mongodb.service;

import com.bezkoder.spring.data.mongodb.model.user;
import com.bezkoder.spring.data.mongodb.repository.RoleRepository;
import com.bezkoder.spring.data.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    public List<user> getAll(){
        return userRepository.findAll();
    }

}