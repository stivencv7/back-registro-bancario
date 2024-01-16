package com.web.app.bancario.entity;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.JoinColumn;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="usuarios")
public class Usuario implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String username;
	
	private String apellido;
	private String telefono;
	
	@Column(unique = true)
	private String email;
	
	private String password;
	
	@Column(name = "numero_cuenta",unique = true)
	private String numeroCuenta;
	
	private int saldo;
	
	@ManyToMany(fetch = FetchType.EAGER,targetEntity = Role.class,cascade = CascadeType.PERSIST)
	@JoinTable(name="user_roles",joinColumns = @JoinColumn(name="id_usuario"),
	inverseJoinColumns = @JoinColumn(name="id_role"))
	private Set<Role>roles;
	
	@PostConstruct
	public void addRole() {
		roles=new HashSet<>();
	}
	
	@PrePersist
	public void crearNumeroCuenta() {
		String numero="";
		for (int i = 0; i <4; i++) {
			numero+=(int)Math.floor(Math.random()*9-1)+1;
		}
		numeroCuenta=numero;
	}
}

