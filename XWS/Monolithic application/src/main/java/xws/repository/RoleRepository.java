package xws.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import xws.model.ERole;
import xws.model.Role;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, String> {
  Optional<Role> findByName(ERole name);
}
