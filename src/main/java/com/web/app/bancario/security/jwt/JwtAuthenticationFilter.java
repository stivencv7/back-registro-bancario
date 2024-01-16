package com.web.app.bancario.security.jwt;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import com.web.app.bancario.entity.Usuario;
//import com.web.app.bancario.service.jwt.UsuarioDetallesImpl;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {


	// UsernamePasswordAuthenticationFilter: es quien nos va ayudar a authenticarnos en la aplicacion
	
	/**
	 * attemptAuthentication: intentar autenticarce 
	 * request: biene el usuario que intentamos authenticar
	 * ObjectMapper(): para mapiar y convertir Json en Objectos Java
	 * AuthenticationManager(): es el objecto que se encarga de administrar la autenticacion
	 */
	
	/*@Autowired*/
	private JwtUtils jwtUtils;
	
	public JwtAuthenticationFilter(JwtUtils jwtUtils) {
		this.jwtUtils=jwtUtils;
	}
	
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		Usuario userEntity=null;
		String username="";
		String password="";
		try {
			userEntity=new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
			username=userEntity.getUsername();
			password=userEntity.getPassword();
		}catch(StreamReadException e) {
			throw new RuntimeException(e);
		}catch(DatabindException e) {
			throw new RuntimeException(e);
		}catch (IOException e) {
			throw new RuntimeException(e);
		}
		//UsernamePasswordAuthenticationToken: para validar el username y el password
		UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username, password);
				
		return getAuthenticationManager().authenticate(authenticationToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		//*cabiada
		User userDetailsCore=(User)authResult.getPrincipal();
		//estamos generando el token de acceso para dar authorizacion **
//		UsuarioDetallesImpl userDetailsCore=(UsuarioDetallesImpl) authResult.getPrincipal();
		String token=jwtUtils.generateAccesToken(userDetailsCore.getUsername(),userDetailsCore.getAuthorities());
		
		response.addHeader("Authorization",token);
		//response.addHeader("Authorization","Bearer "+ token);
		
		Map<String,Object>httpResponse=new HashMap<>();
		httpResponse.put("token",token);
		httpResponse.put("Message","Autenticacion Correcta");
		httpResponse.put("Username",userDetailsCore.getUsername());
		
		
//		 HttpSession session=request.getSession();
//		 session.setAttribute("username",userDetailsCore.getUsername());
		 
		//estariamos escribiendo  (httpResponse) como un Json en la respuesta
		response.getWriter().write(new ObjectMapper().writeValueAsString(httpResponse));
		response.setStatus(HttpStatus.OK.value());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);//MediaType.APPLICATION_JSON_VALUE= application/json
		 response.getWriter().flush(); //Con esto nosotros estamos asegurando que todo se escriba correcta mente
		
		super.successfulAuthentication(request, response, chain, authResult);
	}
	
	
	
}
