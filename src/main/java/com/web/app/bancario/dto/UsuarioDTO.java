package com.web.app.bancario.dto;

import java.io.Serializable;
import java.util.Set;

import com.web.app.bancario.entity.Role;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsuarioDTO implements Serializable {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String apellido;
	private String telefono;
	private String email;
	private String password;
	private String numeroCuenta;
	private int saldo;
	private Set<Role>roles;
	
	
	
}
