package com.web.app.bancario.security.jwt;

import java.security.Key;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JwtUtils {

	@Value("${jwt.secret.key}")
	private String secretKey;

	@Value("${jwt.time.expiration}")
	private String timeExpiration;
	
	public String generateAccesToken(String username,Collection<? extends GrantedAuthority> collection) {
		System.err.println("GerarToken "+username );
		Map<String,List<String>>response=new HashMap<>();
		List<String>roles=new ArrayList<>();
		for(Object a:collection){
			System.out.println(a);
			roles.add(a.toString());
		}
		response.put("role", roles);
		return Jwts.builder().setClaims(response).
				setSubject(username)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + Long.parseLong(timeExpiration)))
				.signWith(getSignatureKey(), SignatureAlgorithm.HS256).compact();
				

	}

	// Validar el token de acceso 
	public boolean isTokenValied(String token) {
//		System.out.println("=== isTokenValied");
		try {
			Jwts.parserBuilder()
			.setSigningKey(getSignatureKey())
			.build()
			.parseClaimsJws(token)
			.getBody();

			return true;

		} catch (Exception e) {
			//@Slf4j=para que funcione el log
			log.error("Token invalido, error: ".concat(e.getMessage()));
			return false;
		}
	}

	// Obtener el username del token
	public String getUsername(String token) {
		return getClaim(token, Claims::getSubject);
	}

	// Obtener un solo claims
	public <T> T getClaim(String token, Function<Claims, T> claimsTFunction) {
		Claims claims = extractAllClaims(token);
		return claimsTFunction.apply(claims);
	}

	// Obtener todos los claims del token ej los datos que vienen
	public Claims extractAllClaims(String token) {
		return Jwts.parserBuilder().setSigningKey(getSignatureKey()).build().parseClaimsJws(token).getBody();
	}

	// obtener firma del token
	private Key getSignatureKey() {
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Keys.hmacShaKeyFor(keyBytes);
	}

}
