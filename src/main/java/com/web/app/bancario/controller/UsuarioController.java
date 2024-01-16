package com.web.app.bancario.controller;

import java.io.IOException;
//import java.util.Collection;
import java.util.HashMap;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.app.bancario.dto.UsuarioDTO;
import com.web.app.bancario.entity.ERole;
import com.web.app.bancario.entity.Historial;
import com.web.app.bancario.entity.Role;
import com.web.app.bancario.entity.Usuario;
import com.web.app.bancario.security.jwt.JwtUtils;
import com.web.app.bancario.service.IServiceHistoria;
import com.web.app.bancario.service.IServiceUsuario;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api")
public class UsuarioController {

	@Autowired
	private IServiceUsuario servicio;
	
	@Autowired
	private IServiceHistoria servicioH;
	
	@Autowired
	private JwtUtils jwtUtils;

	
	@GetMapping("/get")
	public List<Usuario> getUsuarios() {
		return servicio.findAll();
	}
	
	
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@GetMapping("/findAll/pageable/searchTerm")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> getUsuariosPageableSearch(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size, @RequestParam(required = false) String searchTerm) {
		System.out.println("nombre==> " + searchTerm);
		Page<Usuario> usuarios = null;
		if (!searchTerm.isBlank()) {
			getUsuariosPageable(0, 5);
		}

		Map<String, Object> response = new HashMap<>();
		try {
			usuarios = servicio.findAllByUsernameLikeOrEmailLikeOrNumeroCuentaLike(searchTerm,PageRequest.of(page, size));
		} catch (DataAccessException e) {
			response.put("error", "no se encuentran datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		System.out.println(usuarios);
		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping("/findAll/pageable")
	public ResponseEntity<?> getUsuariosPageable(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "5") int size) {
		System.err.println("page " + page);
		Page<Usuario> usuarios = null;

		Map<String, Object> response = new HashMap<>();
		try {
			usuarios = servicio.findAll(PageRequest.of(page, size));
		} catch (DataAccessException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return new ResponseEntity<Page<Usuario>>(usuarios, HttpStatus.OK);
	}

	@GetMapping("/search/cuenta/{numeroCuenta}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<?> getUserCuenta(@PathVariable String numeroCuenta) {
		Map<String, Object> response = new HashMap<>();

		System.out.println(" numeroCuenta " + numeroCuenta);
		Optional<Usuario> usuario = servicio.findByNumeroCuenta(numeroCuenta);

		if (usuario.isPresent()) {
			return new ResponseEntity<Usuario>(usuario.get(), HttpStatus.OK);
		}

		response.put("error", "No se encontraron registros");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.BAD_REQUEST);
	}

	@GetMapping("/search/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<?> getUser(@PathVariable Long id) {
		Map<String, Object> response = new HashMap<>();
		Usuario usuario = null;
		System.out.println("id getUser= " + id);
		try {
			usuario = servicio.findById(id);
			System.out.println("get " + usuario);
		} catch (DataAccessException e) {
			response.put("error", "Datos no validos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (usuario == null) {
			response.put("error", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
	
	@GetMapping("/search/email/{email}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> getUserEmail(@PathVariable String email) {
		System.out.println("ESTE es el Email "+email);
		
		Map<String, Object> response = new HashMap<>();
		Usuario usuario = new Usuario();
		System.out.println("id email= " + email);
		try {
//			usuario = servicio.findByCorreo(email).get();
			usuario = servicio.findByEmail(email).get();

			System.out.println("get " + usuario);
		} catch (DataAccessException e) {
			response.put("error", "Datos no validos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		if (usuario == null) {
			response.put("error", "No se encontraron datos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
	}
//=========================================================
//	@PostMapping("/add")
//	@PreAuthorize("hasRole('ADMIN')")
//	public ResponseEntity<?> add(@RequestBody Usuario u) {
//		Map<String, Object> response = new HashMap<>();
//		Usuario usuario = new Usuario();
//		System.out.println(u);
//		try {
//			usuario = servicio.save(u);
//		} catch (DataAccessException e) {
//			response.put("error", "el correo tienen que ser unicos");
//			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
//		}
//		response.put("mensaje", "exito al guardar");
//		response.put("usuario", usuario);
//		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
//	}
	
	@PostMapping("/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> registrarUsuario(@RequestBody UsuarioDTO usuario){
	/*Set<Role>roles=usuario.getRoles().stream()
				.map(role->Role.builder().name(ERole.valueOf(role))
						.build()
						).collect(Collectors.toSet());*/
	
		Usuario newUsuario=Usuario.builder()
				.username(usuario.getUsername())
				.apellido(usuario.getApellido())
				.telefono(usuario.getTelefono())
				.email(usuario.getEmail())
				.password(passwordEncoder.encode(usuario.getPassword()))
				.saldo(usuario.getSaldo())
				.roles(Set.of(Role.builder()
						.name(ERole.valueOf(ERole.USER.name()))
						.build()))
				.build();
		
		Map<String,Object>response=new HashMap<>();
		try {
			newUsuario=servicio.save(newUsuario);
		}catch (DataAccessException e) {
			response.put("mensaje", "no se realizo el inser");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("usuario",newUsuario);
		response.put("mensaje","se realizó la accion exitosamente");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
//===============================================================000

	@PostMapping("/multiples/add")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> addMultipleMedicamento(@RequestParam("file") MultipartFile file) {
		System.out.println("ARchivo nuevo " + file.getContentType());
		Map<String, Object> response = new HashMap<>();
		try {
			servicio.savaAll(file);
		} catch (DataAccessException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		response.put("mensaje", "Registro guardado Correcta mente");
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);

	}

	@PutMapping("/set")
	@PreAuthorize("hasAnyRole('ADMIN','USER')")
	public ResponseEntity<?> setUser(@RequestBody Usuario u, HttpServletResponse respons) {
		
		
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String username = authentication.getName();
	 
	    
	    Set<String> roless = authentication.getAuthorities()
	            .stream()
	            .map(GrantedAuthority::getAuthority)
	            .collect(Collectors.toSet());
	    
	    System.out.println("username=> "+username);
	    System.out.println("roles=> "+roless);
	    Set<Role>roles=new HashSet<>();
	    
	    Optional<Usuario>user=servicio.findByEmail(username);
	    if(user.isPresent()) {
	    	Usuario us=user.get();
	
	    if(us.getId()==u.getId() && roless.contains("ROLE_ADMIN") ) {
	    	roles.add(new Role((long) 1,ERole.ADMIN));
	    }else {
	    	roles.add(new Role((long) 2,ERole.USER));
	    }
	    
	    }
		System.err.println(u.getPassword());
		
		
		Map<String, Object> response = new HashMap<>();
		
		Usuario usuario = new Usuario();
		System.out.println("kkk" + u.getSaldo());
		try {
			usuario.setId(u.getId());
			usuario.setUsername(u.getUsername());
			usuario.setApellido(u.getApellido());
			usuario.setEmail(u.getEmail());
			usuario.setTelefono(u.getTelefono());
			usuario.setSaldo(u.getSaldo());
			usuario.setRoles(roles);
			usuario.setPassword(passwordEncoder.encode(u.getPassword()));
			usuario.setNumeroCuenta(u.getNumeroCuenta());
			
			String newToken=jwtUtils.generateAccesToken(usuario.getUsername(), authentication.getAuthorities());
			
			System.err.println(newToken);
			
			
			respons.addHeader("Authorization",newToken);
			//response.addHeader("Authorization","Bearer "+ token);
			
			response.put("token", newToken);


		} catch (DataAccessException e) {
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		usuario = servicio.save(usuario);
		response.put("mensaje", "exito al actualizar");
		response.put("usuario", usuario);
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.CREATED);
	}

	@PutMapping("/set/transaccion/{cuenta}/{monto}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> setTransaccion(@RequestBody Usuario u, @PathVariable String cuenta,
			@PathVariable int monto) {
		
		Map<String, Object> response = new HashMap<>();
		
		Optional<Usuario> optionalUser  = servicio.findByNumeroCuenta(cuenta);
		
		if (!optionalUser.isPresent()) {
			response.put("error", "Usuario beneficiario no encontrado");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}
		monto = Math.abs(monto);
		
		

		;
		if (u.getSaldo() <= 0) {
			response.put("error", "saldo");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}

		if (u.getSaldo() < monto) {
			response.put("error", "saldo Insuficiente");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
		}

		

		try {

//			usuario.setId(u.getId());
//			usuario.setUsername(u.getUsername());
//			usuario.setApellido(u.getApellido());
//			usuario.setEmail(u.getEmail());
//			usuario.setTelefono(u.getTelefono());
//			usuario.setPassword(passwordEncoder.encode(u.getPassword()));
//			usuario.setRoles(u.getRoles());
//			usuario.setSaldo(u.getSaldo() - monto);
//			usuario.setNumeroCuenta(u.getNumeroCuenta());

			if (u.getNumeroCuenta().equals(optionalUser.get().getNumeroCuenta())) {
				response.put("error", "Transferencia no valida");
				return new ResponseEntity<Map<String, Object>>(response, HttpStatus.NOT_FOUND);
			}

			Usuario receptor = optionalUser.get();
			receptor.setSaldo(receptor.getSaldo() + monto);
			servicio.save(receptor); // Asegurar que ambas operaciones sean atómicas

		} catch (DataAccessException e) {

			response.put("error", "Datos invalidos");
			return new ResponseEntity<Map<String, Object>>(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}

		response.put("mensaje", "el usuario "+u.getUsername()+" realizo transferencia al usuario "+optionalUser.get().getUsername());
		return new ResponseEntity<Map<String, Object>>(response, HttpStatus.OK);
	}

	 
	@PostMapping("/add/historial/{idUser}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public ResponseEntity<?> registrarHistorial(@PathVariable Long idUser,@RequestBody Historial historial){
		System.out.println(historial.getMonto()+" m "+historial.getDescripcion());
		Map<String,Object>response=new HashMap<>();
		if(historial.getDescripcion().isBlank() || historial.getMonto()==0  ) {
			response.put("error","El campo descripcion y el campo monto no puede ser nulo");
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.BAD_REQUEST);
		}
		
		Historial h=new Historial();
		System.err.println("MMonto "+historial.getMonto());
		Usuario newUsuario=new Usuario();
		Usuario user=servicio.findById(idUser);
		if(historial.getMonto()>0) {
			System.err.println("MMont if "+historial.getMonto());
			user.setSaldo(user.getSaldo()+historial.getMonto());
		}else if(historial.getMonto()<0) {
			 int numeroPositivo = Math.abs(historial.getMonto());
			int operacion=user.getSaldo()-numeroPositivo;
			user.setSaldo(operacion);
		}
		
		newUsuario.setId(user.getId());
		newUsuario.setUsername(user.getUsername());
		newUsuario.setApellido(user.getApellido());
		newUsuario.setEmail(user.getEmail());
		newUsuario.setPassword(user.getPassword());
		newUsuario.setRoles(user.getRoles());
		newUsuario.setNumeroCuenta(user.getNumeroCuenta());
		newUsuario.setSaldo(user.getSaldo());
		
		
		
		
		System.err.println("add H "+idUser+ " "+historial.getDescripcion());
		
		try {
			historial.setIdUser(idUser);
			h=servicioH.save(historial);
			servicio.save(newUsuario);
		}catch (DataAccessException e) {
			response.put("mensaje", "no se realizo el inser");
			response.put("error", e.getMessage());
			return new ResponseEntity<Map<String,Object>>(response,HttpStatus.INTERNAL_SERVER_ERROR);
		}
		response.put("historial",h);
		response.put("mensaje","se realizó la accion exitosamente");
		
		return new ResponseEntity<Map<String,Object>>(response,HttpStatus.CREATED);
	}
	
	@GetMapping("/get/historial/{id}")
	@PreAuthorize("hasAnyRole('USER','ADMIN')")
	public List<Historial> getUserHistorial(@PathVariable Long id) {
		System.out.println("Get list");
		return servicioH.finAllByIdUsuario(id);
	}
	
	@DeleteMapping("/remove/{id}/{usuario}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUser(@PathVariable Long id,@PathVariable String usuario) {
		System.err.println("estoy en eliminar"+ usuario);
		Map<String,String>response=new HashMap<>();
		
		
		Optional<Usuario>usuarioOp=servicio.findByEmail(usuario);
		if(usuarioOp.isPresent()) {
			System.err.println("estoy en if1 eliminar");
			Usuario u=usuarioOp.get();
			if(id==u.getId()) {
				System.err.println("estoy en if2 eliminar");
				response.put("error","En estos momentos esta utilizando estas credenciales" );
				return new ResponseEntity<Map<String,String>>(response,HttpStatus.BAD_REQUEST);
			}
		}
		
		
		
		System.out.println("id " + id);
		servicio.deleteById(id);
		
		response.put("menaje","Usuario eliminado con exito");
		
		return new ResponseEntity<Map<String,String>>(response,HttpStatus.OK);
		
		

	}

}
