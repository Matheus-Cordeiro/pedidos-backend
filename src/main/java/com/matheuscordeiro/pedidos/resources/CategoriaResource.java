package com.matheuscordeiro.pedidos.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.matheuscordeiro.pedidos.domain.Categoria;
import com.matheuscordeiro.pedidos.dto.CategoriaDTO;
import com.matheuscordeiro.pedidos.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {
	
	@Autowired
	private CategoriaService service;
	
	@GetMapping(value = "")
	public ResponseEntity<List<CategoriaDTO>> findAll(){
		List<Categoria> ListaCategoria = service.findAll();
		List<CategoriaDTO> ListaCategoriaDTO = ListaCategoria.stream().map(categoria -> new CategoriaDTO(categoria)).collect(Collectors.toList());
		return ResponseEntity.ok().body(ListaCategoriaDTO);
	}
	
	@GetMapping(value = "/pagina")
	public ResponseEntity<Page<CategoriaDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction, 
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy){
		Page<Categoria> PaginaCategoria = service.findPage(page, linesPerPage, direction ,orderBy);
		Page<CategoriaDTO> ListaCategoriaDTO = PaginaCategoria.map(categoria -> new CategoriaDTO(categoria));
		return ResponseEntity.ok().body(ListaCategoriaDTO);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Categoria> findById(@PathVariable (name = "id") Integer id) {
		Categoria categoria = service.findById(id);
		return ResponseEntity.ok().body(categoria);
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<Void> save(@Valid @RequestBody CategoriaDTO categoriaDTO){
		Categoria categoria = service.fromDTO(categoriaDTO);
		categoria = service.save(categoria);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(categoria.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/atualizar/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO categoriaDTO, @PathVariable(name = "id") Integer id){
		Categoria categoria = service.fromDTO(categoriaDTO);
		categoria.setId(id);
		categoria = service.update(categoria);
		return ResponseEntity.noContent().build();
	}
	
	@DeleteMapping(value = "/deletar/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
