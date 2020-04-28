package com.matheuscordeiro.pedidos.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.matheuscordeiro.pedidos.domain.Cidade;
import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.domain.Endereco;
import com.matheuscordeiro.pedidos.domain.enums.TipoCliente;
import com.matheuscordeiro.pedidos.dto.ClienteDTO;
import com.matheuscordeiro.pedidos.dto.ClienteNovoDTO;
import com.matheuscordeiro.pedidos.exceptions.DataIntegrityException;
import com.matheuscordeiro.pedidos.exceptions.ObjectNotFoundException;
import com.matheuscordeiro.pedidos.repositories.ClienteRepository;
import com.matheuscordeiro.pedidos.repositories.EnderecoRepository;

@Service
public class ClienteService {
	
	@Autowired
	BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private EnderecoRepository enderecoRepository;
	
	public Cliente findById(Integer id){
		Optional<Cliente> cliente = clienteRepository.findById(id);
		return cliente.orElseThrow(() -> new ObjectNotFoundException(
		"Objeto não encontrado! Id: " + id + " | Tipo: " + Cliente.class.getName()));
	}
	
	public List<Cliente> findAll(){
		List<Cliente> clientes = new ArrayList<>();
		clientes = clienteRepository.findAll();
		return clientes;
	}
	
	public Cliente save(Cliente cliente) {
		clienteRepository.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
	}
	
	public Cliente update(Cliente cliente) {
		Cliente newData = findById(cliente.getId());
		updateAux(newData, cliente);
		findById(cliente.getId());
		return clienteRepository.save(newData);
	}
	
	public void delete(Integer id) {
		findById(id);
		try {
			clienteRepository.deleteById(id);
		} catch(DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir clientes com pedidos relacionados");
		}
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage,String direction, String orderBy){
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		return clienteRepository.findAll(pageRequest);
	}
	
	public Cliente fromDTO(ClienteDTO clienteDTO) {
		return new Cliente(clienteDTO.getId(), clienteDTO.getNome(), clienteDTO.getEmail(), null, null, null);
	}
	
	public Cliente fromDTO(ClienteNovoDTO clienteNovoDTO) {
		Cliente cliente = new Cliente(null, clienteNovoDTO.getNome(), clienteNovoDTO.getEmail(), clienteNovoDTO.getDocumento(), TipoCliente.toEnum(clienteNovoDTO.getTipoCliente()), bCryptPasswordEncoder.encode(clienteNovoDTO.getSenha()));
		Cidade cidade = new Cidade(clienteNovoDTO.getCidadeId(), null, null);
		Endereco endereco = new Endereco(null, clienteNovoDTO.getLogradouro(), clienteNovoDTO.getNumero(), clienteNovoDTO.getComplemento(), clienteNovoDTO.getBairro(), clienteNovoDTO.getCep(), cliente, cidade);
		cliente.getEnderecos().add(endereco);
		cliente.getTelefones().add(clienteNovoDTO.getTelefone1());
		if(clienteNovoDTO.getTelefone2() != null) cliente.getTelefones().add(clienteNovoDTO.getTelefone2());
		if(clienteNovoDTO.getTelefone3() != null) cliente.getTelefones().add(clienteNovoDTO.getTelefone3()); 
		return cliente;
	}
	
	private void updateAux(Cliente newData, Cliente oldData ) {
		newData.setNome(oldData.getNome());
		newData.setEmail(oldData.getEmail());
		
	}
	
}
