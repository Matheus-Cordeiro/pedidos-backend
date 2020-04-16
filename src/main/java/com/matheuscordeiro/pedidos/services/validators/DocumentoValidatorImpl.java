package com.matheuscordeiro.pedidos.services.validators;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.matheuscordeiro.pedidos.domain.enums.TipoCliente;
import com.matheuscordeiro.pedidos.dto.ClienteNovoDTO;
import com.matheuscordeiro.pedidos.resources.exceptions.FieldMessage;
import com.matheuscordeiro.pedidos.services.validators.util.BR;

public class DocumentoValidatorImpl implements ConstraintValidator<DocumentoValidator, ClienteNovoDTO> {

	@Override
	public void initialize(DocumentoValidator ann) {}
	
	@Override
	public boolean isValid(ClienteNovoDTO clienteNovoDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if(clienteNovoDTO.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(clienteNovoDTO.getDocumento())) {
			list.add(new FieldMessage("documento", "CPF inválido"));
		}
		
		if(clienteNovoDTO.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(clienteNovoDTO.getDocumento())) {
			list.add(new FieldMessage("documento", "CNPJ inválido"));
		}
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
	
}
