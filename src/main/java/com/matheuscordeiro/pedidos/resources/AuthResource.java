package com.matheuscordeiro.pedidos.resources;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheuscordeiro.pedidos.dto.EmailDTO;
import com.matheuscordeiro.pedidos.security.JWTUtil;
import com.matheuscordeiro.pedidos.security.UserSecurity;
import com.matheuscordeiro.pedidos.services.AuthService;
import com.matheuscordeiro.pedidos.services.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired
	AuthService authService;
	
	
	@PostMapping(value = "/refreshToken")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response){
		UserSecurity user = UserService.getUsuarioLogado();
		String token = jwtUtil.generateToken(user.getUsername());
		response.addHeader("Authorization", "Bearer" + token);
		return ResponseEntity.noContent().build();
		
	}
	
	@PostMapping(value = "/forgot")
	public ResponseEntity<Void> Forgot(@Valid @RequestBody EmailDTO emailDTO) throws MessagingException{

		authService.PrepareNewPassword(emailDTO.getEmail());
		
		return ResponseEntity.noContent().build();
		
	}
	
	
}
