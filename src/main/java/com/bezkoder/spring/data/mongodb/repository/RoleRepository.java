package com.bezkoder.spring.data.mongodb.repository;

import com.bezkoder.spring.data.mongodb.model.ERole;
import com.bezkoder.spring.data.mongodb.model.Role;
import com.bezkoder.spring.data.mongodb.model.user;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
