package com.web.app.bancario.service;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.app.bancario.entity.Historial;
import com.web.app.bancario.entity.Usuario;
import com.web.app.bancario.utils.UsuarioNotFoundException;

@Service
public interface IServiceUsuario{
	
	List<Usuario>findAll();
	Usuario findById(Long id);
	Usuario save (Usuario usuario);
	void deleteById(Long id);
	Optional<Usuario>findByPasswordAndNumeroCuenta(String password,String numeroCuenta);
	Optional<Usuario>findByNumeroCuenta(String nuemroCuenta);
	Page<Usuario>findAll(Pageable p);
	Page<Usuario> findAllByUsernameLikeOrEmailLikeOrNumeroCuentaLike(String searchTerm, Pageable pageable);
	void savaAll(MultipartFile file);
	Optional<Usuario> findByEmail(String email);
	Optional<Usuario> findByCorreo(String email);
	
	
	
}
