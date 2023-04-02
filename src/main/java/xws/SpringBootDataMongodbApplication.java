package xws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import xws.model.ERole;
import xws.model.Flight;
import xws.model.Role;
import xws.model.User;
import xws.repository.FlightRepository;
import xws.repository.RoleRepository;
import xws.repository.UserRepository;

import java.text.SimpleDateFormat;
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
	private FlightRepository flightRepository;

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


		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

		Flight flight1 = new Flight(dateFormat.parse("2020-05-19T14:10:30Z"), "New York", dateFormat.parse("2020-05-19T16:35:30Z"), "Los Angeles", 15, 60, 500);
		Flight flight2 = new Flight(dateFormat.parse("2020-06-01T10:15:00Z"), "London", dateFormat.parse("2020-06-01T17:30:00Z"), "Paris", 10, 40, 350);
		Flight flight3 = new Flight(dateFormat.parse("2020-07-10T08:45:00Z"), "Tokyo", dateFormat.parse("2020-07-10T18:20:00Z"), "Sydney", 19, 20, 800);
		Flight flight4 = new Flight(dateFormat.parse("2020-08-20T12:30:00Z"), "Dubai", dateFormat.parse("2020-08-20T20:10:00Z"), "Mumbai", 30, 35, 450);
		Flight flight5 = new Flight(dateFormat.parse("2020-09-05T16:00:00Z"), "Toronto", dateFormat.parse("2020-09-05T19:30:00Z"), "Vancouver", 18, 30, 600);

		flightRepository.save(flight1);
		flightRepository.save(flight2);
		flightRepository.save(flight3);
		flightRepository.save(flight4);
		flightRepository.save(flight5);



	}
}