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

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.dto.ClienteDTO;
import com.matheuscordeiro.pedidos.exceptions.DataIntegrityException;
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
	
	public List<Cliente> findAll(){
		List<Cliente> clientes = new ArrayList<>();
		clientes = repository.findAll();
		return clientes;
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newData = findById(cliente.getId());
		updateAux(newData, cliente);
		findById(cliente.getId());
		return repository.save(newData);
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
		repository.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir clientes com entidades relacionadas");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage,String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return repository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null);
		
	}
	
	private void updateAux(Cliente newData, Cliente oldData ) {
		newData.setNome(oldData.getNome());
		newData.setEmail(oldData.getEmail());
		
	}
	
}
