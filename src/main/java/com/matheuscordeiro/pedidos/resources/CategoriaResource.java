package com.matheuscordeiro.pedidos.resources;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheuscordeiro.pedidos.domain.Categoria;
import com.matheuscordeiro.pedidos.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<?> findById(@PathVariable (name = "id") Integer id) {
		Categoria categoria = service.findById(id);
		return ResponseEntity.ok().body(categoria);
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<Void> save(@RequestBody Categoria categoria){
		categoria = service.save(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
}
