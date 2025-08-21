package mx.com.qtx.cotizadorv1ds.core;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;

@SpringBootTest
class ICotizadorTest {
	
	@Autowired
	ICotizador cotizador;

	@Test
	void testAgregarComponente() {
		//Dados
		assumeTrue(this.cotizador != null);
		System.out.println("Implementacion ICotizador:" + this.cotizador.getClass().getName());
		
		//Cuando
		Componente monitor = Componente.crearMonitor("M-1", "Monitor 21 pulg", "Lg", "modelo", 
				new BigDecimal("870.00"), new BigDecimal("1870.00"));
		this.cotizador.agregarComponente(5, monitor);
		//Entonces
		fail("Pendiente de concluir");
		
	}

	@Test
	void testEliminarComponente() {
		fail("Not yet implemented");
	}

	@Test
	void testGenerarCotizacion() {
		fail("Not yet implemented");
	}

	@Test
	void testListarComponentes() {
		fail("Not yet implemented");
	}

	@Test
	void testGetCotizacionXID() {
		fail("Not yet implemented");
	}

}
