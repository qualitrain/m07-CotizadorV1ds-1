package mx.com.qtx.cotizadorv1ds.core.componentes;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
	void testCrearPc() {
		//Dados
		
		// A. Datos grales Pc
		id = "PC-1";
		descripcion = "Laptop Dell 15 pulgadas Inspiron 8000";
		marca = "Dell";
		modelo = "Inspiron 8000";
		
	    // B. Componente 1: Disco Duro
        BigDecimal costoDisco = new BigDecimal("577.90");
        BigDecimal precioBaseDisco = new BigDecimal("1200.90");
        String capacidad = "2 Tb";
        Componente disco = Componente.crearDiscoDuro("D001", "Disco SSD", "Seagate", "Saturno", 
                                                   costoDisco, precioBaseDisco, capacidad);
        // C. Componente 2: Monitor
        BigDecimal costoMonitor = new BigDecimal("300.00");
        BigDecimal precioBaseMonitor = new BigDecimal("500.00");
        Componente monitor = Componente.crearMonitor("M001", "Monitor 24\"", "LG", "UltraGear", 
                                                   costoMonitor, precioBaseMonitor);
        
        List<Componente> subComponentes = List.of(disco, monitor);
		
		//Cuando
		
        Componente pc = Componente.crearPc(id, descripcion, marca, modelo, subComponentes);
        
		//Entonces
        assertNotNull(pc, "El pc generado es nulo");
        assertInstanceOf(Pc.class, pc, "No se generó el tipo esperado");
        Pc pcBis = (Pc) pc;

        // Verificar propiedades principales
        assertEquals(id, pcBis.getId(), "ID no coincide");
        assertEquals(descripcion, pcBis.getDescripcion(), "Descripción no coincide");
        assertEquals(marca, pcBis.getMarca(), "Marca no coincide");
        assertEquals(modelo, pcBis.getModelo(), "Modelo no coincide");

        // Verificar subcomponentes
       List<ComponenteSimple> subComponentesPc = pcBis.getSubComponentes();
       assertEquals(2, subComponentesPc.size(), "Número incorrecto de subcomponentes");        
       
       assertTrue(subComponentesPc.stream().anyMatch(c -> "D001".equals(c.getId())), 
                 "Falta el disco en subcomponentes");
       
       assertTrue(subComponentesPc.stream().anyMatch(c -> "M001".equals(c.getId())), 
               "Falta el monitor en subcomponentes");
      
	}
	
	@Test
	void testCrearPc_conListaVaciaDeSubcomponentes() {
		//Dados
		
		// A. Datos grales Pc
		id = "PC-1";
		descripcion = "Laptop Dell 15 pulgadas Inspiron 8000";
		marca = "Dell";
		modelo = "Inspiron 8000";
		
		// B. Lista vacía
	    List<Componente> subComponentes = new ArrayList<>();
		//Cuando
		
        Executable funcionCrearPc = ()-> Componente.crearPc(id, descripcion, marca, modelo, subComponentes);
        
		//Entonces
        assertThrows(EstructuraComponenteInvalidaException.class, funcionCrearPc, "No lanza la excepcion esperada");
	}
	
	@Test
	void testCrearPc_conListaNula() {
		//Dados
		
		// A. Datos grales Pc
		id = "PC-1";
		descripcion = "Laptop Dell 15 pulgadas Inspiron 8000";
		marca = "Dell";
		modelo = "Inspiron 8000";
		
		// B. Lista vacía
	    List<Componente> subComponentes = null;
		//Cuando
		
        Executable funcionCrearPc = ()-> Componente.crearPc(id, descripcion, marca, modelo, subComponentes);
        
		//Entonces
        assertThrows(EstructuraComponenteInvalidaException.class, funcionCrearPc, "No lanza la excepcion esperada");
	}

	@Test
	void testCrearPc_subComponenteInvalido() {
		//Dados
		
		// A. Datos grales Pc
		id = "PC-1";
		descripcion = "Laptop Dell 15 pulgadas Inspiron 8000";
		marca = "Dell";
		modelo = "Inspiron 8000";
		
	    // B. Componente 1: Disco Duro
        BigDecimal costoDisco = null;
        BigDecimal precioBaseDisco = null;
        String capacidad = "2 Tb";
        Componente disco = Componente.crearDiscoDuro("D001", "Disco SSD", "Seagate", "Saturno", 
                costoDisco, precioBaseDisco, capacidad);
        List<Componente> subComponentes = List.of(disco);
		//Cuando
		
        Executable funcionCrearPc = ()-> Componente.crearPc(id, descripcion, marca, modelo, subComponentes);
        
		//Entonces
        assertThrows(EstructuraComponenteInvalidaException.class, funcionCrearPc, "No lanza la excepcion esperada");
	}

}
