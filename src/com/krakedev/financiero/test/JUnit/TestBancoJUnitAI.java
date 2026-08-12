package com.krakedev.financiero.test.JUnit;

import com.krakedev.financiero.servicio.Banco;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.krakedev.financiero.Cliente;
import com.krakedev.financiero.Cuenta;

/**
 * Pruebas unitarias para la clase Banco.
 * Cubre los métodos: crearCuenta, depositar y retirar.
 *
 * IMPORTANTE: Estas pruebas validan el comportamiento REAL de la clase Banco
 * tal como está implementada actualmente, no una descripción funcional externa.
 *
 * NOTA SOBRE UN BUG DETECTADO:
 * El método depositar() calcula correctamente "saldoAnterior + monto" en la
 * variable local "saldoNuevo", pero luego llama a cuenta.setSaldoActual(monto)
 * en lugar de cuenta.setSaldoActual(saldoNuevo). Esto provoca que el saldo NO
 * se acumule: cada depósito válido reemplaza el saldo actual por el monto
 * depositado, en vez de sumarlo. Las pruebas de depositar() están escritas
 * para reflejar este comportamiento real (incluyendo el bug).
 */
class TestBancoJUnitAI {

	// Tolerancia para comparación de valores double
	private static final double DELTA = 0.0001;

	private Banco banco;
	private Cliente cliente;

	@BeforeEach
	void setUp() {
		// Se usa el único constructor disponible de Banco: Banco()
		// (la clase Banco no cuenta con un segundo constructor sobrecargado)
		banco = new Banco();

		// Se usa el constructor real de Cliente(cedula, nombre, apellido)
		cliente = new Cliente("0102030405", "Juan", "Perez");
	}

	// ==================== crearCuenta ====================

	@Test
	@DisplayName("crearCuenta: genera el código inicial esperado (100) y asocia el propietario")
	void testCrearCuenta_codigoInicial() {
		// Banco() inicializa ultCodigo en 100, por lo que la primera cuenta
		// creada debe tener id "100" y el cliente debe quedar como propietario
		Cuenta cuenta = banco.crearCuenta(cliente);

		assertNotNull(cuenta, "La cuenta creada no debe ser null");
		assertEquals("100", cuenta.getId(), "El id de la primera cuenta debe ser '100'");
		assertEquals(cliente, cuenta.getPropietario(), "El propietario de la cuenta debe ser el cliente recibido");
	}

	@Test
	@DisplayName("crearCuenta: incrementa ultCodigo después de crear una cuenta")
	void testCrearCuenta_incrementaUltCodigo() {
		// Antes de crear la cuenta ultCodigo es 100; después debe ser 101
		assertEquals(100, banco.getUltCodigo(), "ultCodigo inicial debe ser 100");

		banco.crearCuenta(cliente);

		assertEquals(101, banco.getUltCodigo(), "ultCodigo debe incrementarse en 1 tras crear una cuenta");
	}

	@Test
	@DisplayName("crearCuenta: códigos consecutivos al crear varias cuentas")
	void testCrearCuenta_codigosConsecutivos() {
		// Cada llamada sucesiva debe generar un código distinto y consecutivo
		Cuenta cuenta1 = banco.crearCuenta(cliente);
		Cuenta cuenta2 = banco.crearCuenta(cliente);
		Cuenta cuenta3 = banco.crearCuenta(cliente);

		assertEquals("100", cuenta1.getId());
		assertEquals("101", cuenta2.getId());
		assertEquals("102", cuenta3.getId());
	}

	@Test
	@DisplayName("crearCuenta: respeta un ultCodigo modificado previamente con setUltCodigo")
	void testCrearCuenta_conUltCodigoPersonalizado() {
		// Si se modifica ultCodigo manualmente antes de crear la cuenta,
		// el nuevo id generado debe reflejar ese valor
		banco.setUltCodigo(500);

		Cuenta cuenta = banco.crearCuenta(cliente);

		assertEquals("500", cuenta.getId(), "El id debe usar el ultCodigo personalizado");
		assertEquals(501, banco.getUltCodigo(), "ultCodigo debe incrementarse a partir del valor personalizado");
	}

	@Test
	@DisplayName("crearCuenta: la cuenta nueva inicia con saldo en 0")
	void testCrearCuenta_saldoInicialEnCero() {
		// El constructor de Cuenta(id) inicializa saldoActual en 0
		Cuenta cuenta = banco.crearCuenta(cliente);

		assertEquals(0.0, cuenta.getSaldoActual(), DELTA, "El saldo inicial de una cuenta nueva debe ser 0");
	}

	// ==================== depositar ====================

	@Test
	@DisplayName("depositar: monto positivo retorna true y actualiza el saldo (comportamiento real, incluye bug)")
	void testDepositar_montoPositivo() {
		Cuenta cuenta = banco.crearCuenta(cliente);

		boolean resultado = banco.depositar(100.0, cuenta);

		assertTrue(resultado, "Un depósito con monto positivo debe retornar true");
		// Debido al bug real de la implementación (setSaldoActual(monto) en vez
		// de setSaldoActual(saldoNuevo)), el saldo queda igual al monto depositado
		assertEquals(100.0, cuenta.getSaldoActual(), DELTA,
				"El saldo queda igual al monto depositado debido al bug en depositar()");
	}

	@Test
	@DisplayName("depositar: depósitos sucesivos no acumulan el saldo (evidencia del bug)")
	void testDepositar_depositosSucesivosNoAcumulan() {
		Cuenta cuenta = banco.crearCuenta(cliente);

		banco.depositar(100.0, cuenta);
		boolean resultado = banco.depositar(50.0, cuenta);

		assertTrue(resultado, "El segundo depósito también debe retornar true");
		// Si el saldo se acumulara correctamente sería 150.0, pero por el bug
		// el saldo queda simplemente en el último monto depositado (50.0)
		assertEquals(50.0, cuenta.getSaldoActual(), DELTA,
				"El saldo final refleja solo el último monto depositado, no la suma");
	}

	@Test
	@DisplayName("depositar: monto negativo retorna false y no modifica el saldo")
	void testDepositar_montoNegativo() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(200.0);

		boolean resultado = banco.depositar(-50.0, cuenta);

		assertFalse(resultado, "Un depósito con monto negativo debe retornar false");
		assertEquals(200.0, cuenta.getSaldoActual(), DELTA, "El saldo no debe modificarse si el monto es negativo");
	}

	@Test
	@DisplayName("depositar: monto igual a 0 retorna true pero deja el saldo en 0 (evidencia del bug)")
	void testDepositar_montoCero() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(300.0);

		// monto = 0 no es < 0, por lo que el método continúa y ejecuta
		// setSaldoActual(monto), dejando el saldo en 0 pese a que había 300.0
		boolean resultado = banco.depositar(0.0, cuenta);

		assertTrue(resultado, "Un depósito de monto 0 retorna true según la condición actual");
		assertEquals(0.0, cuenta.getSaldoActual(), DELTA,
				"Por el bug, un depósito de 0 deja el saldo en 0 en lugar de conservar el saldo previo");
	}

	// ==================== retirar ====================

	@Test
	@DisplayName("retirar: monto válido menor al saldo retorna true y descuenta correctamente")
	void testRetirar_montoValido() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(500.0);

		boolean resultado = banco.retirar(200.0, cuenta);

		assertTrue(resultado, "Un retiro válido dentro del saldo disponible debe retornar true");
		assertEquals(300.0, cuenta.getSaldoActual(), DELTA, "El saldo debe descontarse correctamente tras el retiro");
	}

	@Test
	@DisplayName("retirar: monto igual al saldo disponible retorna true y deja el saldo en 0")
	void testRetirar_montoIgualAlSaldo() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(150.0);

		boolean resultado = banco.retirar(150.0, cuenta);

		assertTrue(resultado, "Retirar exactamente el saldo disponible debe retornar true");
		assertEquals(0.0, cuenta.getSaldoActual(), DELTA, "El saldo debe quedar en 0 tras retirar el total disponible");
	}

	@Test
	@DisplayName("retirar: monto mayor al saldo disponible retorna false y no modifica el saldo")
	void testRetirar_montoMayorAlSaldo() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(100.0);

		boolean resultado = banco.retirar(150.0, cuenta);

		assertFalse(resultado, "Un retiro mayor al saldo disponible debe retornar false");
		assertEquals(100.0, cuenta.getSaldoActual(), DELTA, "El saldo no debe modificarse si el retiro es rechazado");
	}

	@Test
	@DisplayName("retirar: monto negativo retorna false y no modifica el saldo")
	void testRetirar_montoNegativo() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(100.0);

		boolean resultado = banco.retirar(-10.0, cuenta);

		assertFalse(resultado, "Un retiro con monto negativo debe retornar false");
		assertEquals(100.0, cuenta.getSaldoActual(), DELTA, "El saldo no debe modificarse si el monto es negativo");
	}

	@Test
	@DisplayName("retirar: monto igual a 0 retorna false (la condición exige monto > 0)")
	void testRetirar_montoCero() {
		Cuenta cuenta = banco.crearCuenta(cliente);
		cuenta.setSaldoActual(100.0);

		boolean resultado = banco.retirar(0.0, cuenta);

		assertFalse(resultado, "La condición monto > 0 excluye un retiro de monto 0, por lo que debe retornar false");
		assertEquals(100.0, cuenta.getSaldoActual(), DELTA, "El saldo no debe modificarse si el retiro es de 0");
	}
}