package com.matheuscordeiro.pedidos.resources;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable (name = "id") Integer id) {
		Cliente cliente = service.findById(id);
		return ResponseEntity.ok().body(cliente);
	}
}
