package com.krakedev.financiero.test;

import com.krakedev.financiero.Cliente;
import com.krakedev.financiero.Cuenta;

public class TestCuenta {

	public static void main(String[] args) {
		
		Cuenta c1 = new Cuenta("123");
		c1.setPropietario(new Cliente("1293129319", "Mario", "Tipanluisa"));
		c1.imprimir();	
		
		Cuenta c2 = new Cuenta("456");
		Cliente cl2 = new Cliente("1293129319", "Italo", "Tipanluisa");
		c2.setPropietario(cl2);
		c2.imprimir();
	}
		
}
