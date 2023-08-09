package com.xws.user;

import com.xws.user.entity.ERole;
import com.xws.user.entity.Role;
import com.xws.user.entity.User;
import com.xws.user.repo.AccommodationRepository;
import com.xws.user.repo.ReservationRepository;
import com.xws.user.repo.RoleRepository;
import com.xws.user.repo.UserRepository;
import com.xws.user.service.UserSerGrpc;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
@EnableDiscoveryClient
public class UserApplication implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private AccommodationRepository accommodationRepository;

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
        User admin = new User(
                "admin",
                "admin",
                "admin_address",
                "adminadmin",
                "admin@gmail.com",
                encoder.encode("adminadmin"),
                0);

        Set<Role> roles = new HashSet<>();

        Role modRole = roleRepository.findByName(ERole.ROLE_HOST)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(modRole);

        admin.setRoles(roles);
        userRepository.save(admin);

        User user = new User(
                "user",
                "user",
                "user_address",
                "useruser",
                "user@gmail.com",
                encoder.encode("useruser"),
                0);

        Set<Role> roles1 = new HashSet<>();

        Role modRole1 = roleRepository.findByName(ERole.ROLE_GUEST)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles1.add(modRole1);

        user.setRoles(roles1);
        userRepository.save(user);

        Server server= ServerBuilder
                .forPort(6565)
                .addService(new UserSerGrpc(reservationRepository, userRepository, accommodationRepository)).build();

        server.start();
        server.awaitTermination();
    }
}
