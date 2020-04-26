package com.matheuscordeiro.pedidos.services;

import org.springframework.mail.SimpleMailMessage;

import com.matheuscordeiro.pedidos.domain.Pedido;

public interface EmailService {
	
	void sendOrderConfirmationEmail(Pedido pedido);
	
	void sendEmail(SimpleMailMessage msg);

}
