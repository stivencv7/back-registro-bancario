package com.web.app.bancario.dao;



import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.web.app.bancario.entity.Historial;
@Repository
public interface IRepositoryHistorial extends JpaRepository<Historial, Long>{	
	List<Historial> findAllByIdUser(Long id);
}
