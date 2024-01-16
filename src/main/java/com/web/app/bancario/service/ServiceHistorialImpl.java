package com.web.app.bancario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.web.app.bancario.dao.IRepositoryHistorial;
import com.web.app.bancario.entity.Historial;

@Service
public class ServiceHistorialImpl implements IServiceHistoria {
	
	@Autowired
	private IRepositoryHistorial hDao;

	@Override
	public List<Historial> finAllByIdUsuario(Long id) {
		
		return hDao.findAllByIdUser(id);
	}

	@Override
	public Historial save(Historial historial) {
		
		return hDao.save(historial);
	}

}
