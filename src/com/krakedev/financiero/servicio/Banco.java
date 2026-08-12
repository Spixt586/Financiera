package com.krakedev.financiero.servicio;

import com.krakedev.financiero.Cliente;
import com.krakedev.financiero.Cuenta;

public class Banco {
	
	private int ultCodigo;
	
	public Banco() {
		ultCodigo = 100;
	}

	public int getUltCodigo() {
		return ultCodigo;
	}

	public void setUltCodigo(int ultCodigo) {
		this.ultCodigo = ultCodigo;
	}
	
	public Cuenta crearCuenta(Cliente cliente) {
		String codigoStr = ultCodigo + "";
		ultCodigo++;
		Cuenta c = new Cuenta(codigoStr);
		c.setPropietario(cliente);
		return c;
	}
		
	
}
