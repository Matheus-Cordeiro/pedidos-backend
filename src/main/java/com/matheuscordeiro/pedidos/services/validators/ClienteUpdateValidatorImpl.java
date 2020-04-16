package com.matheuscordeiro.pedidos.services.validators;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import com.matheuscordeiro.pedidos.domain.Cliente;
import com.matheuscordeiro.pedidos.dto.ClienteDTO;
import com.matheuscordeiro.pedidos.repositories.ClienteRepository;
import com.matheuscordeiro.pedidos.resources.exceptions.FieldMessage;

public class ClienteUpdateValidatorImpl implements ConstraintValidator<ClienteUpdateValidator, ClienteDTO> {

	@Autowired
	private HttpServletRequest httpServletRequest;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Override
	public void initialize(ClienteUpdateValidator ann) {}
	
	@Override
	public boolean isValid(ClienteDTO clienteDTO, ConstraintValidatorContext context) {
		
		List<FieldMessage> list = new ArrayList<>();
		
		
		@SuppressWarnings("unchecked")
		Map<String, String> requestAttributes = (Map<String, String>) httpServletRequest.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(requestAttributes.get("id"));
		
		Cliente clienteAux = clienteRepository.findByEmail(clienteDTO.getEmail());
		if(clienteAux != null && !clienteAux.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}
		
		for(FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
	
}
