package com.web.app.bancario.service.jwt;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.web.app.bancario.entity.*;
import com.web.app.bancario.service.*;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private IServiceUsuario serviceUsuario;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		System.err.println("UserDetails " + username);

		Usuario usuario = serviceUsuario.findByEmail(username)
				.orElseThrow(() -> new UsernameNotFoundException("El usuario '" + username + "' no existe."));
		
		System.err.println("UserDetails " + usuario.getRoles());
		
//SimpleGrantedAuthority es la athorizacion de 
		Collection<? extends GrantedAuthority> authorities = usuario.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
				.collect(Collectors.toSet());

//		return new UsuarioDetallesImpl(usuario);

		return new User(usuario.getUsername(),
				usuario.getPassword(),
				true,true,true,true,
				authorities) ;
	}

}
