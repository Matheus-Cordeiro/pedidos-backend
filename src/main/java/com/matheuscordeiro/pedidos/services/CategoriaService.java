package com.matheuscordeiro.pedidos.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.matheuscordeiro.pedidos.domain.Categoria;
import com.matheuscordeiro.pedidos.exceptions.ObjectNotFoundException;
import com.matheuscordeiro.pedidos.repositories.CategoriaRepository;

@Service
public class CategoriaService {
	
	@Autowired
	private CategoriaRepository repository;
	
	public Categoria findById(Integer id){
		Optional<Categoria> categoria = repository.findById(id);
		return categoria.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + " | Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria save(Categoria categoria) {
		return repository.save(categoria);
	}
	
	public Categoria update(Categoria categoria) {
		findById(categoria.getId());
		return repository.save(categoria);
	}
	
}
