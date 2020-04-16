package com.matheuscordeiro.pedidos.services.validators;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.domain.enums.TipoCliente;
import com.matheuscordeiro.pedidos.dto.ClienteNovoDTO;
import com.matheuscordeiro.pedidos.repositories.ClienteRepository;
import com.matheuscordeiro.pedidos.resources.exceptions.FieldMessage;
import com.matheuscordeiro.pedidos.services.validators.util.BR;

public class ClienteValidatorImpl implements ConstraintValidator<ClienteValidator, ClienteNovoDTO> {

	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteValidator ann) {}
	
	@Override
	public boolean isValid(ClienteNovoDTO clienteNovoDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if(clienteNovoDTO.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(clienteNovoDTO.getDocumento())) {
			list.add(new FieldMessage("documento", "CPF inválido"));
		}
		
		if(clienteNovoDTO.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(clienteNovoDTO.getDocumento())) {
			list.add(new FieldMessage("documento", "CNPJ inválido"));
		}
		
		Cliente clienteAux = clienteRepository.findByEmail(clienteNovoDTO.getEmail());
		if(clienteAux != null) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
	
}
