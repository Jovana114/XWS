package com.bezkoder.spring.data.mongodb.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.bezkoder.spring.data.mongodb.model.user;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<user, Long> {

  List<user> findByNameContaining(String name);

  Optional<user> findByUsername(String username);
  Optional<user> findByEmail(String email);


  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);

  List<user> findAll();

}
