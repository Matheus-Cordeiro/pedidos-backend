package com.matheuscordeiro.pedidos.domain.enums;

public enum Perfil {

	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	
	private int codigo;
	private String msg;

	private Perfil(int codigo, String msg) {
		this.codigo = codigo;
		this.msg = msg;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getMsg() {
		return msg;
	}

	public static Perfil toEnum(Integer codigo) {

		if (codigo == null)
			return null;

		for (Perfil estadoPagamento : Perfil.values()) {
			if (codigo.equals(estadoPagamento.getCodigo())) {
				return estadoPagamento;
			}
		}

		throw new IllegalArgumentException("Id inválido: " + codigo);
	}
	
}
