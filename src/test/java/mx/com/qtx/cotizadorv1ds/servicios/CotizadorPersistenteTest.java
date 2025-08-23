package mx.com.qtx.cotizadorv1ds.servicios;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import mx.com.qtx.cotizadorv1ds.core.ICotizador;
import mx.com.qtx.cotizadorv1ds.core.IServicioComponentes;
import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.DetalleCotizacion;

@SpringBootTest
class CotizadorPersistenteTest {
	//
	@Autowired
	ICotizador cotizadorPersistente;
	
	@Autowired
	IServicioComponentes gestorComponentes;
	
	@Test
	void testGenerarCotizacion() {
		//Dados
		assumeTrue(cotizadorPersistente != null);
		assumeTrue(gestorComponentes != null);
		Componente disco = getDiscoDuro();
		assumeTrue(disco != null);
		int cantDiscos = 5;
		Componente monitor = getMonitor();
		assumeTrue(monitor != null);
		int cantMonitores = 2;
		
		//Cuando 
		cotizadorPersistente.agregarComponente(cantMonitores, monitor);
		cotizadorPersistente.agregarComponente(cantDiscos, disco);
		Cotizacion cotizacion = cotizadorPersistente.generarCotizacion();
		
		//Entonces
		assertNotNull(cotizacion);
		assertThat(cotizacion.getDetalles().size()).isEqualTo(2);
		Collection<DetalleCotizacion> dets = cotizacion.getDetalles().values();
		List<String> lstIds = dets.stream()
				                   .map(detI->detI.getIdComponente())
				                   .filter(idI->monitor.getId().equals(idI)).toList();
		assertTrue(lstIds.size() == 1);
		
		cotizacion.emitirComoReporte();
	}

	private Componente getMonitor() {
		return this.gestorComponentes.getComponenteXID("MON01");
//		return Componente.crearMonitor("M-1", "Monitor super plus de 100 pulgadas", 
//				"LG", "Lunar 3000", new BigDecimal("35000.00"), new BigDecimal("70000.00"));
	}

	private Componente getDiscoDuro() {
		return this.gestorComponentes.getComponenteXID("DD-1");
//		return Componente.crearDiscoDuro("DD-1", "Disco ultra rapido 3Tb", "Seagate", "Hurricane", 
//				new BigDecimal("800.00"), new BigDecimal("1600.00"), "3Tb");
	}

}
