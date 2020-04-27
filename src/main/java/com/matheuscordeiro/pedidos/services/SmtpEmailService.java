package com.matheuscordeiro.pedidos.services;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;

import com.matheuscordeiro.pedidos.domain.Pedido;

public class SmtpEmailService{


	public SimpleMailMessage  prepareEmail(Pedido pedido) {
		
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setSubject("Pedido Confirmado - Código: " + pedido.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(pedido.toString());
		return sm;
	}
	
}
