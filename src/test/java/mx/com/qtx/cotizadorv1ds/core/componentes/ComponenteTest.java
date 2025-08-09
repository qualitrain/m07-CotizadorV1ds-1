package mx.com.qtx.cotizadorv1ds.core.componentes;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class ComponenteTest {
	String id;
	String descripcion;
	String marca;
	String modelo;

	
	@BeforeEach
	void mostrarMensajeAntesDeTest() {
		id = "T-1";
		descripcion = "Disco Estado Sólido 2 Terabytes";
		marca = "Seagate";
		modelo = "Saturno";
	}

	@Test
	void testCrearDiscoDuro() {
		
		//Dados
		BigDecimal costo = new BigDecimal("577.90");
		BigDecimal precioBase = new BigDecimal("1200.90");
		String capacidad = "2 Tb";
		
		//Cuando
		Componente disco = Componente.crearDiscoDuro(id, descripcion, marca, modelo, costo, precioBase, capacidad);

		//Entonces
		assertNotNull(disco,"El objeto generado es Nulo");
		assertInstanceOf(DiscoDuro.class,disco,"No se generó el tipo esperado");
		DiscoDuro discoBis = (DiscoDuro) disco;
		assertEquals(discoBis.getMarca(), marca);
	}
	
	@Test
	void testCrearDiscoDuro_costoNulo() {
		
		//Dados
		BigDecimal costo = null;
		BigDecimal precioBase = new BigDecimal("1200.90");
		String capacidad = "2 Tb";
		
		//Cuando
		Executable crearDiscoCostoNulo = () -> Componente.crearDiscoDuro(id, descripcion, marca, modelo, costo, precioBase, capacidad);

		//Entonces
		assertThrows(IllegalArgumentException.class, crearDiscoCostoNulo,"No se lanza la excepcion adecuada");

	}

	@Test
	void testCrearDiscoDuro_precioBaseNulo() {
		
		//Dados
		BigDecimal costo = new BigDecimal("1200.90");;
		BigDecimal precioBase = null;
		String capacidad = "2 Tb";
		
		//Cuando
		Executable crearDiscoCostoNulo = 
				() -> Componente.crearDiscoDuro(id, descripcion, marca, modelo, costo, precioBase, capacidad);

		//Entonces
		assertThrows(IllegalArgumentException.class, crearDiscoCostoNulo,"No se lanza la excepcion adecuada");

	}
	
	@Test
	@Disabled
	void testCrearMonitor() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testCrearTarjetaVideo() {
		fail("Not yet implemented");
	}

	@Test
	@Disabled
	void testCrearPc() {
		fail("Not yet implemented");
	}

}
