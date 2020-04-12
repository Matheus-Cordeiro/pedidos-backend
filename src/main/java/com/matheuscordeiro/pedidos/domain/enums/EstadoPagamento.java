package com.matheuscordeiro.pedidos.domain.enums;

public enum EstadoPagamento {

	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");
	
	private int codigo;
	private String msg;

	private EstadoPagamento(int codigo, String msg) {
		this.codigo = codigo;
		this.msg = msg;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getMsg() {
		return msg;
	}

	public static EstadoPagamento toEnum(Integer codigo) {

		if (codigo == null)
			return null;

		for (EstadoPagamento estadoPagamento : EstadoPagamento.values()) {
			if (codigo.equals(estadoPagamento.getCodigo())) {
				return estadoPagamento;
			}
		}

		throw new IllegalArgumentException("Id inválido: " + codigo);
	}
	
}
