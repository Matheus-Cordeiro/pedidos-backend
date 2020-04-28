package com.matheuscordeiro.pedidos.services;

import java.util.Date;
import java.util.Random;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.exceptions.ObjectNotFoundException;
import com.matheuscordeiro.pedidos.repositories.ClienteRepository;

@Service
public class AuthService {
	
	@Autowired
	ClienteRepository clienteRepository;
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
    private JavaMailSender javaMailSender;
	
	
	private Random random = new Random();
	
	public void PrepareNewPassword(String email) throws MessagingException {
		
		Cliente cliente = clienteRepository.findByEmail(email);
		
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		String newPassword = newPassword();
		cliente.setSenha(bCryptPasswordEncoder.encode(newPassword));
		clienteRepository.save(cliente);
		
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false);  
		mimeMessageHelper.setTo(cliente.getEmail());
		mimeMessageHelper.setSubject("Redefinição de senha");
		mimeMessageHelper.setSentDate(new Date(System.currentTimeMillis()));
		mimeMessageHelper.setText("Uma nova senha foi solicitada em nosso sistema a partir de seu email. \n"
				+ "Nova senha: " + newPassword);
		javaMailSender.send(mimeMessage);
	}


	private String newPassword() {
		char[] vet = new char[10];
		for(int i=0; i<10; i++) {
			vet[i] = randomChar();
		}
		return new String(vet);
	
	
	}


	private char randomChar() {

		int opt = random.nextInt(3);
		if(opt == 0) {
			return (char) (random.nextInt(10) + 48);
		}else if(opt == 1) {
			return (char) (random.nextInt(26) + 65);
		}else {
			return (char) (random.nextInt(26) + 97);
		}
		
	}
	
}
