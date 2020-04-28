package com.matheuscordeiro.pedidos.services;

import org.springframework.security.core.context.SecurityContextHolder;

import com.matheuscordeiro.pedidos.security.UserSecurity;

public class UserService {

	public static UserSecurity getUsuarioLogado() {
		try {
			return (UserSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}catch(Exception e) {
			return null;
		}
		
	}
	
}
