//package com.web.app.bancario.service.jwt;
//
//
//
//import java.util.Collection;
//import java.util.stream.Collectors;
//
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.UserDetails;
//import com.web.app.bancario.entity.*;
//
//public class UsuarioDetallesImpl implements UserDetails {
//
//	/**
//	 * 
//	 */
//	private static final long serialVersionUID = 1L;
//	private Usuario usuario;
//
//	public UsuarioDetallesImpl(Usuario usuario) {
//		this.usuario = usuario;
//
//	}
//
//	@Override
//	public Collection<? extends GrantedAuthority> getAuthorities() {
//
//		Collection<? extends GrantedAuthority>authorities=usuario.getRoles().stream()
//				.map(role-> new SimpleGrantedAuthority("ROLE_".concat(role.getName().name())))
//				.collect(Collectors.toSet());
//		
//		System.err.println("AAAAAAAAAAAAAA "+ authorities);
//
//		return authorities;
//	}
//
//	@Override
//	public String getUsername() {
//		return usuario.getEmail();
//
//	}
//	
//	@Override
//	public String getPassword() {
//		return usuario.getPassword();
//	}
//
//	@Override
//	public boolean isAccountNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isAccountNonLocked() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isCredentialsNonExpired() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	@Override
//	public boolean isEnabled() {
//		// TODO Auto-generated method stub
//		return true;
//	}
//
//	public String getNombre() {
//		return usuario.getUsername();
//	}
//
//	
//	public Long getId() {
//		return usuario.getId();
//	}
//
//}
