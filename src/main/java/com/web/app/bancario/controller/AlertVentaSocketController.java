package com.web.app.bancario.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.web.app.bancario.entity.Usuario;
import com.web.app.bancario.service.IServiceUsuario;


@CrossOrigin(origins = "*")
@Controller
public class AlertVentaSocketController {
	
	@Autowired
	private IServiceUsuario serviceUser;
	
//	@MessageMapping("/alert")
//	@SendTo("/topic/messages")
//	public Map<String,String>chat() {
//		Map<String,String>response=new HashMap<>();
//		response.put("message","Nuevo pedido");
//		return response;
//	}
	
	
	@MessageMapping("/length")
	@SendTo("/topic/messages")
	public Map<String,String> lentgh(Integer numeroCuenta) {
		
		
		
		System.err.println("kk"+ numeroCuenta);
		
		Map<String,String>response=new HashMap<>();

		Optional<Usuario> usuario=serviceUser.findByNumeroCuenta(numeroCuenta.toString());
		
		System.err.println("kk"+ usuario.get().getUsername());
		
		if(usuario.isPresent()) {
			String u=usuario.get().getUsername();
			response.put("message","Se te realizo una tranferencia");
			response.put("username",u);
			System.out.println("kk"+response.get("username"));
		}
		
	
		return response;
	}
}
