package com.matheuscordeiro.pedidos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.matheuscordeiro.pedidos.domain.Categoria;
import com.matheuscordeiro.pedidos.dto.CategoriaDTO;
import com.matheuscordeiro.pedidos.exceptions.DataIntegrityException;
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
	
	public List<Categoria> findAll(){
		List<Categoria> categorias = new ArrayList<>();
		categorias = repository.findAll();
		return categorias;
	}
	
	public Categoria save(Categoria categoria) {
		return repository.save(categoria);
	}
	
	public Categoria update(Categoria categoria) {
		Categoria newData = findById(categoria.getId());
		updateAux(newData, categoria);
		findById(categoria.getId());
		return repository.save(newData);
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
		repository.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("A exclusão de uma categoria que possui produtos atrelados a ela é proibida :c");
		}
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage,String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO categoriaDTO) {
		return new Categoria(categoriaDTO.getId(), categoriaDTO.getNome());
	}
	
	private void updateAux(Categoria newData, Categoria oldData ) {
		newData.setNome(oldData.getNome());
	}
	
}
