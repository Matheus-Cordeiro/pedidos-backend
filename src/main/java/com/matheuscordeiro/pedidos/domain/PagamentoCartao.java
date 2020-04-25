package com.matheuscordeiro.pedidos.domain;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.matheuscordeiro.pedidos.domain.enums.EstadoPagamento;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@JsonTypeName("PagamentoCartao")
public class PagamentoCartao extends Pagamento {
	private static final long serialVersionUID = 1L;
	
	private Integer totalParcelas;

	public PagamentoCartao(Integer id, EstadoPagamento estadoPagamento, Pedido pedido, Integer totalParcelas) {
		super(id, estadoPagamento, pedido);
		this.totalParcelas = totalParcelas;
	}
}
