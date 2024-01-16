package com.web.app.bancario.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.web.app.bancario.security.jwt.JwtAuthenticationFilter;
import com.web.app.bancario.security.jwt.JwtAuthorizationFilter;
import com.web.app.bancario.security.jwt.JwtUtils;
import com.web.app.bancario.service.jwt.UserDetailsServiceImpl;



@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImple;
	
	@Autowired
	private JwtUtils jwtUtils;
	
	@Autowired
	private JwtAuthorizationFilter jwtAuthorizationFilter;
	
	//================= Sin utilizar jwt ===========
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,AuthenticationManager authenticationManager) throws Exception {
		
		//JwtAuthenticationFilter jwtAuthenticationFilter=new JwtAuthenticationFilter(jwtUtils);
		JwtAuthenticationFilter jwtAuthenticationFilter=new JwtAuthenticationFilter(jwtUtils);
		jwtAuthenticationFilter.setAuthenticationManager(authenticationManager);
		jwtAuthenticationFilter.setFilterProcessesUrl("/api/login");//colocamos la url que nosotro queramos normalmente es login
		
		httpSecurity
			
		 	.cors(cors->{
		 	})
			.csrf(csr->{
				csr.disable();
			})
			.authorizeHttpRequests(authorize->{
		
				authorize.requestMatchers("api/findAll/pageable","/ws/**","/topic/message","/alert","/topic","/app/alert").permitAll();
				//authorize.requestMatchers("/productos/{nombre}").hasRole("ADMIN");//para manejar los roles
				//authorize.requestMatchers("/productos/{nombre}").hasanyRole("ADMIN");
				authorize.anyRequest().authenticated();
			})
			.sessionManagement(session->{
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			})
			.addFilter(jwtAuthenticationFilter)
			//esto quiere decir que primero se va ajecutar jwtAuthenticationFilter antes de UsernamePasswordAuthenticationFilter
			.addFilterBefore(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
			
		return httpSecurity.build();
	}
	
	/*
	//Para crear un usuario de UserDetails en memoria
	@Bean
	UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager =new InMemoryUserDetailsManager();
		manager.createUser(User.withUsername("santiago")
				.password("1234")
				.roles()
				.build());
		return manager;
	}
	
	//AuthenticationManager es el objecto de alministrar la autenticacion 
	@SuppressWarnings("removal")
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity,PasswordEncoder passwordEncoder) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsService())
				.passwordEncoder(passwordEncoder)
				.and().build();
	}
	*/
	
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	//AuthenticationManager es el objecto de administrar la autenticacion 
	@SuppressWarnings("removal")
	@Bean
	AuthenticationManager authenticationManager(HttpSecurity httpSecurity,PasswordEncoder passwordEncoder) throws Exception {
		return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
				.userDetailsService(userDetailsServiceImple)
				.passwordEncoder(passwordEncoder)
				.and()
				.build();
	}
	
	/*public static void main(String[] args) {
		System.out.println(new BCryptPasswordEncoder().encode("1234"));
	}*/
	//===========================================================

}
