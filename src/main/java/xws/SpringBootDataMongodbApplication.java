package xws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import xws.model.ERole;
import xws.model.Role;
import xws.model.User;
import xws.repository.RoleRepository;
import xws.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class SpringBootDataMongodbApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataMongodbApplication.class, args);
	}

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Override
	public void run(String... args) throws Exception {

		roleRepository.deleteAll();

		roleRepository.save(new Role(ERole.ROLE_HOST));
		roleRepository.save(new Role(ERole.ROLE_GUEST));

		System.out.println("Roles found with findAll():");
		System.out.println("-------------------------------");
		for (Role role : roleRepository.findAll()) {
			System.out.println(role.toString());
		}
		System.out.println();

		userRepository.deleteAll();
		User admin = new User("admin",
				"admin@gmail.com",
				encoder.encode("adminadmin"));

		Set<Role> roles = new HashSet<>();

		Role modRole = roleRepository.findByName(ERole.ROLE_HOST)
				.orElseThrow(() -> new RuntimeException("Error: Role is not found."));
		roles.add(modRole);

		admin.setRoles(roles);
		userRepository.save(admin);

	}
}