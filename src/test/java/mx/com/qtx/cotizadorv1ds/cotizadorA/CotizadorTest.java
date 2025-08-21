package mx.com.qtx.cotizadorv1ds.cotizadorA;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;

@ExtendWith(MockitoExtension.class)
class CotizadorTest {
	
	@Mock
	Componente monitor;
	
	@Mock
	Componente discoDuro;

	@Test
	void testAgregarComponente() {
		//Dados
		Cotizador cotizador = new Cotizador();
		
		when(monitor.getId()).thenReturn("M-1");
		when(discoDuro.getId()).thenReturn("D-1");
		
		//Cuando
		cotizador.agregarComponente(2, discoDuro);
		cotizador.agregarComponente(3, monitor);
		
		//Entonces
		assertEquals(discoDuro.getId(), cotizador.getComponenteCotizado(discoDuro.getId()).getId());
		assertEquals(monitor.getId(), cotizador.getComponenteCotizado(monitor.getId()).getId());
		
		verify(discoDuro,atLeast(2)).getId();
		verify(monitor,atLeast(2)).getId();
	}

	@Test
	void testEliminarComponente() {
		fail("Not yet implemented");
	}

	@Test
	void testGenerarCotizacion() {
	    // Dados
	    Cotizador cotizador = new Cotizador();

	    when(discoDuro.getId()).thenReturn("D-1");
	    when(monitor.getId()).thenReturn("M-1");
	    when(discoDuro.getDescripcion()).thenReturn("Disco SSD");
	    when(monitor.getDescripcion()).thenReturn("Monitor 24\"");
	    when(discoDuro.getPrecioBase()).thenReturn(new BigDecimal("100.00"));
	    when(monitor.getPrecioBase()).thenReturn(new BigDecimal("200.00"));
	    when(discoDuro.getCategoria()).thenReturn("Almacenamiento");
	    when(monitor.getCategoria()).thenReturn("Visualización");

	    // ¡IMPORTANTE! Mockear el método cotizar()
	    when(discoDuro.cotizar(5)).thenReturn(new BigDecimal("500.00")); // 5 unidades
	    when(monitor.cotizar(2)).thenReturn(new BigDecimal("400.00")); // 2 unidades

	    // Cuando
	    cotizador.agregarComponente(5, discoDuro);
	    cotizador.agregarComponente(2, monitor);
	    Cotizacion cotizacion = cotizador.generarCotizacion();

	    // Entonces
	    assertEquals(2, cotizacion.getDetalles().size());
	    assertEquals(new BigDecimal("900.00"), cotizacion.getTotal());
	}
	
	@Test
	void testGetCotizacionXID() {
		fail("Not yet implemented");
	}

}
