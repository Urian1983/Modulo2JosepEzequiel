package com.JosepEzequiel.Modulo2Inserta.security;

import com.JosepEzequiel.Modulo2Inserta.entity.Roles;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {

    //Le decimos que nuestra aplicación tome las credenciales a las
    //que apunta application.properties que es el DataSource.
    @Bean
    UserDetailsManager userDetailsManager(DataSource dataSource)
    {
        return new JdbcUserDetailsManager(dataSource);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        http.authorizeHttpRequests(configurer ->
                configurer
                        .requestMatchers(HttpMethod.GET, "/api/profesores").hasAnyRole(
                                Roles.RECTOR.name(), Roles.PROFESOR.name(), Roles.ALUMNO.name()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/profesores/{id}").hasAnyRole(
                                Roles.RECTOR.name(), Roles.PROFESOR.name(), Roles.ALUMNO.name()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/profesores/{id}/materias").hasAnyRole(
                                Roles.RECTOR.name(), Roles.PROFESOR.name(), Roles.ALUMNO.name()
                        )
                        .requestMatchers(HttpMethod.POST, "/api/profesores").hasRole(Roles.RECTOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/profesores/{id}").hasRole(Roles.RECTOR.name())
                        .requestMatchers(HttpMethod.PUT, "/api/profesores")
                        .hasAnyRole(Roles.RECTOR.name(), Roles.PROFESOR.name())
                        .requestMatchers(HttpMethod.GET, "/api/materias").hasAnyRole(
                                Roles.RECTOR.name(), Roles.PROFESOR.name(), Roles.ALUMNO.name()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/materias/{id}").hasAnyRole(
                                Roles.RECTOR.name(), Roles.PROFESOR.name(), Roles.ALUMNO.name()
                        )
                        .requestMatchers(HttpMethod.GET, "/api/materias/{id}/profesor").hasAnyRole(
                                Roles.RECTOR.name(), Roles.PROFESOR.name(), Roles.ALUMNO.name()
                        )
                        .requestMatchers(HttpMethod.POST, "/api/materias").hasRole(Roles.RECTOR.name())
                        .requestMatchers(HttpMethod.DELETE, "/api/materias/{id}").hasRole(Roles.RECTOR.name())
                        .requestMatchers(HttpMethod.PUT, "/api/materias")
                        .hasAnyRole(Roles.RECTOR.name(), Roles.PROFESOR.name())

                        .anyRequest().authenticated()
        );

        http.csrf(csrf -> csrf.disable());
        http.httpBasic(Customizer.withDefaults());

        return http.build();
    }


    }
