package com.krakedev.financiero;

public class Cuenta {
	private String id;
	private String nombre;
	private double saldoActual;
	private String tipo = "A";
	private Cliente propietario;

	public Cuenta(String id) {
		this.id = id;
		this.saldoActual = 0;
		this.tipo = "A";
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public double getSaldoActual() {
		return saldoActual;
	}

	public void setSaldoActual(double saldoActual) {
		this.saldoActual = saldoActual;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	
	public Cliente getPropietario() {
		return propietario;
	}
	
	public void setPropietario(Cliente propietario) {
		this.propietario = propietario;
	}

	public void imprimir() {
			System.out.println("========================================");
			System.out.println("           DATOS DE LA CUENTA          ");
			System.out.println("========================================");
			System.out.printf("Cuenta N°      : %s%n", id);
			System.out.printf("Nombre         : %s%n", nombre);
			System.out.printf("Tipo de cuenta : %s%n", tipo);
			System.out.printf("Saldo actual   : $%,.2f%n", saldoActual);
			System.out.println("========================================");
			System.out.println("            DATOS DEL CLIENTE           ");
			System.out.println("========================================");
			System.out.printf("Cédula         : " + propietario.getCedula());
			System.out.printf("Nombre         : " + propietario.getNombre());
			System.out.printf("Apellido       : " + propietario.getApellido());
			System.out.println("========================================");
		
	}
	
}
