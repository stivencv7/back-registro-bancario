package com.web.app.bancario.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.web.app.bancario.entity.Usuario;
import com.web.app.bancario.service.IServiceUsuario;


@CrossOrigin(origins = "*")
@Controller
public class AlertVentaSocketController {
	
	@Autowired
	private IServiceUsuario serviceUser;
	
	
	@MessageMapping("/length")
	@SendTo("/topic/messages")
	public Map<String,String> lentgh(Map<String,Object>message) {
		
		
		System.err.println("kk cuenta"+message.get("numeroCuenta")+ " user"+message.get("user"));
		
		Map<String,String>response=new HashMap<>();
		Optional<Usuario> usuario=null;
		Usuario user=null;
		try {
			usuario=serviceUser.findByNumeroCuenta(message.get("numeroCuenta").toString());
			System.out.println("websocket usuario ==> "+usuario);
			
			
			
		}catch (Exception e) {
			response.put("error", "Numero no valido");
			return response;
		}
		if(usuario.isPresent()) {
			
			 ObjectMapper objectMapper = new ObjectMapper();
			  user=objectMapper.convertValue(message.get("user"), Usuario.class);
			
			System.err.println("err username " +usuario.get().getUsername());
			
			
		
			response.put("username",usuario.get().getUsername());
			
			System.err.println("kk res "+response.get("username"));
			}else {
				response.put("No found","No se encontro usuario");
				return response;
			}
		
		response.put("message","El usario "+ user.getUsername() +"con # de cuenta "+ user.getNumeroCuenta()+" te realizo una tranferencia");
		
		
		
	
		return response;
	}
}
