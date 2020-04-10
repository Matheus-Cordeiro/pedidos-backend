package com.matheuscordeiro.pedidos.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matheuscordeiro.pedidos.domain.Categoria;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@GetMapping
	public List<Categoria> listar() {
		
		Categoria categoria1 = new Categoria(1, "informática");
		Categoria categoria2 = new Categoria(2, "escritório");
		
		List<Categoria> listaCategoria = new ArrayList<>();
		listaCategoria.add(categoria1);
		listaCategoria.add(categoria2);
		
		return listaCategoria;
		
	}
}
