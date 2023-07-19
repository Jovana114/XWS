package com.xws.user.config;

import net.devh.boot.grpc.server.security.authentication.BasicGrpcAuthenticationReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcServerConfig {

    @Bean
    public BasicGrpcAuthenticationReader grpcAuthenticationReader() {

        return new BasicGrpcAuthenticationReader();
    }
}