package com.matheuscordeiro.pedidos.services;

import java.util.Date;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

import com.matheuscordeiro.pedidos.domain.Pedido;

@Component
public abstract class AbstractEmailService implements EmailService {
	
	private String sender = "matheus05paula@gmail.com";
	
	@Override
	public void sendOrderConfirmationEmail(Pedido pedido){
		SimpleMailMessage sm = PrepareSimpleEmailMessageFromPedido(pedido);
		sendEmail(sm);
	}

	protected SimpleMailMessage PrepareSimpleEmailMessageFromPedido(Pedido pedido) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(pedido.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido Confirmado - Código: " + pedido.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(pedido.toString());
		return sm;
	}


}
