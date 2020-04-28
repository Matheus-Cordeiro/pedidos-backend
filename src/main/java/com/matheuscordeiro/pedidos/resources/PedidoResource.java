package com.matheuscordeiro.pedidos.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheuscordeiro.pedidos.domain.Pedido;
import com.matheuscordeiro.pedidos.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> findById(@PathVariable (name = "id") Integer id) {
		Pedido pedido = service.findById(id);
		return ResponseEntity.ok().body(pedido);
	}
	
	@GetMapping(value = "")
	public ResponseEntity<Page<Pedido>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(name = "direction", defaultValue = "DESC") String direction, 
			@RequestParam(name = "orderBy", defaultValue = "instante") String orderBy){
		Page<Pedido> listaPedido = service.findPage(page, linesPerPage, direction ,orderBy);
		return ResponseEntity.ok().body(listaPedido);
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<Void> save(@Valid @RequestBody Pedido pedido){
		pedido = service.save(pedido);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(pedido.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
}
