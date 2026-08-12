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
	
	public boolean depositar(double monto, Cuenta cuenta) {
		if(monto < 0) {
			return false;
		}
			double saldoAnterior = cuenta.getSaldoActual();
			double saldoNuevo = saldoAnterior + monto;
			cuenta.setSaldoActual(monto);
			return true;
		}
	
	public boolean retirar(double monto, Cuenta cuenta) {
			if(monto > 0 && monto <= cuenta.getSaldoActual()) {
				double saldoAnterior = cuenta.getSaldoActual();
				double saldoNuevo = saldoAnterior - monto;
				cuenta.setSaldoActual(saldoNuevo);
				return true;
			}else {
				return false;
			}
		}
	public boolean transferir(Cuenta origen, Cuenta destino, double monto) {
		boolean resultadoRetiro = retirar(monto, origen);
		if(resultadoRetiro){
			return depositar(monto,destino);
		}else {
			return false;
		}
	}
}

	
		
	

