package com.matheuscordeiro.pedidos.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ClienteNovoDTO implements Serializable {
	static final long serialVersionUID = 1L;

	private String nome;
	private String email;
	private String documento;
	private Integer tipoCliente;
	private String logradouro;
	private String numero;
	private String complemento;
	private String bairro;
	private String cep;
	private String telefone1;
	private String telefone2;
	private String telefone3;
	private Integer cidadeId;
}
