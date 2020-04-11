package com.matheuscordeiro.pedidos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.exceptions.ObjectNotFoundException;
import com.matheuscordeiro.pedidos.repositories.ClienteRepository;

@Service
public class ClienteService {
	
	@Autowired
	private ClienteRepository repository;
	
	public Cliente findById(Integer id){
		Optional<Cliente> cliente = repository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + " | Tipo: " + Cliente.class.getName()));
	}
}
