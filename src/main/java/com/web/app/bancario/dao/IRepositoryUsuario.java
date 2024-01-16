package com.web.app.bancario.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.web.app.bancario.entity.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface IRepositoryUsuario extends JpaRepository<Usuario, Long>{
	Optional<Usuario>findByNumeroCuenta(String nuemroCuenta);
	Page<Usuario>findAll(Pageable p);
	@Query("SELECT u FROM Usuario u WHERE u.username LIKE %:searchTerm% OR u.email LIKE %:searchTerm% OR u.numeroCuenta LIKE %:searchTerm%")
	Page<Usuario> findAllByUsernameLikeOrEmailLikeOrNumeroCuentaLike(String searchTerm, Pageable pageable);
	Optional<Usuario> findByUsername(String nombre);
	Optional<Usuario> findByEmail(String email);
	 @Query("SELECT u FROM Usuario u WHERE u.password = :password AND u.numeroCuenta = :numeroCuenta")
	    Optional<Usuario> findByPasswordAndNumeroCuenta(String password,String numeroCuenta);
//	Optional<Usuario> findByUsername(String nombre);
	
}
