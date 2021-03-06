package com.matheuscordeiro.pedidos.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.dto.ClienteDTO;
import com.matheuscordeiro.pedidos.dto.ClienteNovoDTO;
import com.matheuscordeiro.pedidos.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteService service;
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "")
	public ResponseEntity<List<ClienteDTO>> findAll(){
		List<Cliente> ListaCliente = service.findAll();
		List<ClienteDTO> ListaClienteDTO = ListaCliente.stream().map(cliente -> new ClienteDTO(cliente)).collect(Collectors.toList());
		return ResponseEntity.ok().body(ListaClienteDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "/pagina")
	public ResponseEntity<Page<ClienteDTO>> findPage(
			@RequestParam(name = "page", defaultValue = "0") Integer page, 
			@RequestParam(name = "linesPerPage", defaultValue = "24") Integer linesPerPage,
			@RequestParam(name = "direction", defaultValue = "ASC") String direction, 
			@RequestParam(name = "orderBy", defaultValue = "nome") String orderBy){
		Page<Cliente> PaginaCliente = service.findPage(page, linesPerPage, direction ,orderBy);
		Page<ClienteDTO> ListaClienteDTO = PaginaCliente.map(cliente -> new ClienteDTO(cliente));
		return ResponseEntity.ok().body(ListaClienteDTO);
	}
	
	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> findById(@PathVariable (name = "id") Integer id) {
		Cliente cliente = service.findById(id);
		return ResponseEntity.ok().body(cliente);
	}
	
	@PostMapping(value = "/novo")
	public ResponseEntity<Void> save(@Valid @RequestBody ClienteNovoDTO clienteNovoDTO){
		Cliente cliente = service.fromDTO(clienteNovoDTO);
		cliente = service.save(cliente);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(cliente.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@PutMapping(value = "/atualizar/{id}")
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO clienteDTO, @PathVariable(name = "id") Integer id){
		Cliente cliente = service.fromDTO(clienteDTO);
		cliente.setId(id);
		cliente = service.update(cliente);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/deletar/{id}")
	public ResponseEntity<Void> delete(@PathVariable(name = "id") Integer id){
		service.delete(id);
		return ResponseEntity.noContent().build();
	}
}
