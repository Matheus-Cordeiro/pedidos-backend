package com.matheuscordeiro.pedidos.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheuscordeiro.pedidos.security.JWTUtil;
import com.matheuscordeiro.pedidos.security.UserSecurity;
import com.matheuscordeiro.pedidos.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@PostMapping(value = "/refreshToken")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		UserSecurity user = UserService.getUsuarioLogado();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer" + token);
		return ResponseEntity.noContent().build();
		
	}
	
	
}
