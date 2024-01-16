package com.web.app.bancario;

import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.web.app.bancario.entity.ERole;
import com.web.app.bancario.entity.Role;
import com.web.app.bancario.entity.Usuario;
import com.web.app.bancario.service.IServiceUsuario;

@SpringBootApplication
public class WebAppBancarioApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebAppBancarioApplication.class, args);
	}
	
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	private IServiceUsuario usuarioService;
	
	@Bean
	CommandLineRunner init() {
		return args->{
			Usuario usuario=Usuario.builder()
					.email("admin@gmail.com")
					.username("admin")
					.apellido("apellidoAdmin")
					.telefono("3202134567")
					.saldo(50000)
					.password(passwordEncoder.encode("ad123"))
					.roles(Set.of(Role.builder()
							.name(ERole.valueOf(ERole.ADMIN.name()))
							.build()))
					.build();
			
			Usuario usuario2=Usuario.builder()
					.email("user@gmail.com")
					.username("user")
					.apellido("apellidoUser")
					.telefono("32021345688")
					.saldo(50000)
					.password(passwordEncoder.encode("usuario"))
					.roles(Set.of(Role.builder()
							.name(ERole.valueOf(ERole.USER.name()))
							.build()))
					.build();
			
			
			usuarioService.save(usuario);
			usuarioService.save(usuario2);
			
		};
	}
}
