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
public interface IServiceHistoria{
	
	List<Historial> finAllByIdUsuario(Long id);
	Historial save (Historial historial);
	

	
	
	
}
