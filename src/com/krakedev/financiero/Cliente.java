package com.krakedev.financiero;

public class Cliente {
	private String cedula;
	private String nombre;
	private String apellido;
	private Cliente propietario;
	
	public Cliente(String cedula, String nombre, String apellido) {
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
	}
	
	public String getCedula() {
		return cedula;
	}

	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}
	
	public Cliente getPropietario() {
		return propietario;
	}

	public void setPropietario(Cliente propietario) {
		this.propietario = propietario;
	}

	public void imprimir() {
		System.out.println("========================================");
		System.out.println("            DATOS DEL CLIENTE           ");
		System.out.println("========================================");
		System.out.printf("Cédula         : " + cedula);
		System.out.printf("Nombre         : " + nombre);
		System.out.printf("Apellido       : " + apellido);
		System.out.println("========================================");
	}

}
