package xws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import xws.model.ERole;
import xws.model.Role;
import xws.repository.RoleRepository;

@SpringBootApplication
public class SpringBootDataMongodbApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataMongodbApplication.class, args);
	}

	@Autowired
	private RoleRepository repository;

	@Override
	public void run(String... args) throws Exception {

		repository.deleteAll();

		// save a couple of customers
		repository.save(new Role(ERole.ROLE_ADMIN));
		repository.save(new Role(ERole.ROLE_MODERATOR));
		repository.save(new Role(ERole.ROLE_USER));

		// fetch all customers
		System.out.println("Roles found with findAll():");
		System.out.println("-------------------------------");
		for (Role role : repository.findAll()) {
			System.out.println(role.toString());
		}
		System.out.println();

	}
}
