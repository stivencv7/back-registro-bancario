package com.web.app.bancario.service;

//import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.web.app.bancario.dao.IRepositoryUsuario;
import com.web.app.bancario.entity.Historial;
import com.web.app.bancario.entity.Usuario;
import com.web.app.bancario.utils.UsuarioNotFoundException;

@Service
public class ServiceUsuarioImpl implements IServiceUsuario {

	@Autowired
	private IRepositoryUsuario userDao;

	@Override
	public List<Usuario> findAll() {

		return userDao.findAll();
	}

	@Override
	public Usuario save(Usuario t) {
		return userDao.save(t);
	}

	@Override
	public Usuario findById(Long id) {
		return userDao.findById(id).orElse(null);
	}

	@Override
	public void deleteById(Long id) {
		userDao.deleteById(id);

	}

//	@Override
//	public Optional<Usuario> findByContraseniaAndNumeroCuenta(String contrasenia, String nuemroCuenta) {
//		return userDao.findByContraseniaAndNumeroCuenta(contrasenia, nuemroCuenta);
//	}

	@Override
	public Optional<Usuario> findByNumeroCuenta(String nuemroCuenta) {
		// TODO Auto-generated method stub
		return userDao.findByNumeroCuenta(nuemroCuenta);
	}

	@Override
	public Page<Usuario> findAll(Pageable p) {
		return userDao.findAll(p);
	}

//	@Override
//	public void savaAll(MultipartFile file) {
//		System.out.println("hola saveAll");
//		if(ExcelUploadService.isValidExcelFile(file)) {
////			System.out.println("saveAll if");
//			try {
////				System.out.println("saveAll try");
//				List<Usuario>usuarios=ExcelUploadService.getUserDataFromExcel(file.getInputStream());
//				this.userDao.saveAll(usuarios);
//			} catch (IOException e) {
//				throw new IllegalArgumentException("El archivo no es valido");
//			}
//		}else {
//			throw new IllegalArgumentException("El archivo no es valido");
//		}
//		
//	}

	@Override
	public Page<Usuario> findAllByUsernameLikeOrEmailLikeOrNumeroCuentaLike(String searchTerm, Pageable pageable) {

		return userDao.findAllByUsernameLikeOrEmailLikeOrNumeroCuentaLike("%" + searchTerm + "%", pageable);
	}

	@Override
	public Optional<Usuario> findByPasswordAndNumeroCuenta(String password, String numeroCuenta) {
		// TODO Auto-generated method stub
		return userDao.findByPasswordAndNumeroCuenta(password, numeroCuenta) ;
	}

	@Override
	public void savaAll(MultipartFile file) {
		// TODO Auto-generated method stub

	}

	@Override
	public Optional<Usuario> findByEmail(String nombre) {
		// TODO Auto-generated method stub
		return userDao.findByUsername(nombre);
	}

	@Override
	public Optional<Usuario> findByCorreo(String email) {
		
		return userDao.findByEmail(email);
	}

	

}
