package com.matheuscordeiro.pedidos.domain.enums;

public enum TipoCliente {

	PESSOAFISICA(1, "Pessoa Física"), 
	PESSOAJURIDICA(2, "Pessoa Jurídica");

	private int codigo;
	private String msg;

	private TipoCliente(int codigo, String msg) {
		this.codigo = codigo;
		this.msg = msg;
	}

	public int getCodigo() {
		return codigo;
	}

	public String getMsg() {
		return msg;
	}

	public static TipoCliente toEnum(Integer codigo) {

		if (codigo == null)
			return null;

		for (TipoCliente tipoCliente : TipoCliente.values()) {
			if (codigo.equals(tipoCliente.getCodigo())) {
				return tipoCliente;
			}
		}

		throw new IllegalArgumentException("Id inválido: " + codigo);
	}
}
