package com.xws.user;

import com.xws.user.entity.ERole;
import com.xws.user.entity.Role;
import com.xws.user.entity.User;
import com.xws.user.repo.RoleRepository;
import com.xws.user.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
                "adminUserService",
                "admin@gmail.com",
                encoder.encode("123456789"));

        Set<Role> roles = new HashSet<>();

        Role modRole = roleRepository.findByName(ERole.ROLE_HOST)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(modRole);

        admin.setRoles(roles);
        userRepository.save(admin);
    }

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(false);
        config.setMaxAge(3600L);
        config.addExposedHeader("Authorization"); // Add this to expose the Authorization header to the frontend

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return new CorsFilter(source);
    }
}