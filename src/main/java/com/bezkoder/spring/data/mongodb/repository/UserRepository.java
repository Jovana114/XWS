package com.bezkoder.spring.data.mongodb.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bezkoder.spring.data.mongodb.model.user;

public interface UserRepository extends MongoRepository<user, String> {
  List<user> findByNameContaining(String name);
}
