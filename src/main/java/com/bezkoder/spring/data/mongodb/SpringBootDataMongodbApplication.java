package com.bezkoder.spring.data.mongodb;

import com.bezkoder.spring.data.mongodb.model.ERole;
import com.bezkoder.spring.data.mongodb.model.Role;
import com.bezkoder.spring.data.mongodb.model.user;
import com.bezkoder.spring.data.mongodb.repository.RoleRepository;
import com.bezkoder.spring.data.mongodb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Set;

@EnableMongoRepositories
@SpringBootApplication
public class SpringBootDataMongodbApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	public static void main(String[] args) {
		SpringApplication.run(SpringBootDataMongodbApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		Role admin = new Role(ERole.ROLE_ADMIN);
		Role moderator = new Role(ERole.ROLE_MODERATOR);
		Role user = new Role(ERole.ROLE_USER);

		roleRepository.save(admin);
		roleRepository.save(moderator);
		roleRepository.save(user);

		List<Role> roles = roleRepository.findAll();
		System.out.println("===========================================================");
		for (Role r: roles) {
			System.out.println(r.toString());
		}
		System.out.println("===========================================================");

	}

}
