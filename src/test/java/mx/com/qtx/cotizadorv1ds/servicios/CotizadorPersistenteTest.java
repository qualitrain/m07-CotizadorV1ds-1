package mx.com.qtx.cotizadorv1ds.servicios;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import mx.com.qtx.cotizadorv1ds.core.ICotizador;
import mx.com.qtx.cotizadorv1ds.core.IServicioComponentes;
import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.DetalleCotizacion;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.ComponenteDTO;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CotizadorPersistenteTest {
	private static Logger bitacora = LoggerFactory.getLogger(CotizadorPersistenteTest.class);

	@MockitoBean
	IGestorPersistenciaComponentes gestorPersistencia;
	
	@Autowired
	IServicioComponentes gestorComponentes;
	
	@Autowired
	ICotizador cotizadorPersistente;

	@Test
	void testGenerarCotizacion() throws SQLException {
		//Dados
		assumeTrue(cotizadorPersistente != null);
		assumeTrue(gestorComponentes != null);
		ComponenteDTO discoDto = getDiscoDuroDTO();
		assumeTrue(discoDto != null);
		int cantDiscos = 5;
		ComponenteDTO monitorDto = getMonitorDTO();
		assumeTrue(monitorDto != null);
		int cantMonitores = 2;
		when(gestorPersistencia.getComponenteDtoXID(monitorDto.getIdComponente())).thenReturn(monitorDto);
		when(gestorPersistencia.getComponenteDtoXID(discoDto.getIdComponente())).thenReturn(discoDto);
		
		//Cuando 
		cotizadorPersistente.agregarComponente(cantMonitores, getMonitor(monitorDto));
		cotizadorPersistente.agregarComponente(cantDiscos, getDisco(discoDto));
		Cotizacion cotizacion = cotizadorPersistente.generarCotizacion();
		
		//Entonces
		assertNotNull(cotizacion);
		assertThat(cotizacion.getDetalles().size()).isEqualTo(2);
		Collection<DetalleCotizacion> dets = cotizacion.getDetalles().values();
		List<String> lstIds = dets.stream()
				                   .map(detI->detI.getIdComponente())
				                   .filter(idI->monitorDto.getIdComponente().equals(idI))
				                   .toList();
		assertTrue(lstIds.size() == 1);
		
		cotizacion.emitirComoReporte();
		
	}

	private Componente getDisco(ComponenteDTO disco) {
		return Componente.crearDiscoDuro(disco.getIdComponente(), disco.getDescripcion(), 
				disco.getMarca(), disco.getModelo(), 
		        disco.getCosto(), disco.getPrecioBase(), disco.getCapacidadAlm());
	}

	private Componente getMonitor(ComponenteDTO monitor) {
		return Componente.crearMonitor(monitor.getIdComponente(), monitor.getDescripcion(), 
		                              monitor.getMarca(), monitor.getModelo(), monitor.getCosto(), 
		                              monitor.getPrecioBase());
	}

	private ComponenteDTO getMonitorDTO() {
		ComponenteDTO monitor = new ComponenteDTO();
		monitor.setIdComponente("Mon-X");
		monitor.setCapacidadAlm(null);
		monitor.setCosto(new BigDecimal("1200.00"));
		monitor.setDescripcion("Monitor 20 pulgadas");
		monitor.setMarca("LG");
		monitor.setMemoria(null);
		monitor.setModelo("Receptron 500");
		monitor.setNumPromocion(null);
		monitor.setPrecioBase(new BigDecimal("2200.00"));
		monitor.setTipo("monitor");
		return monitor;
	}

	private ComponenteDTO getDiscoDuroDTO() {
		ComponenteDTO disco = new ComponenteDTO();
		disco.setIdComponente("Disco-Y");
		disco.setCapacidadAlm("2Tb");
		disco.setCosto(new BigDecimal("700.00"));
		disco.setDescripcion("Disco ultra rapido 2Tb");
		disco.setMarca("Seagate");
		disco.setMemoria(null);
		disco.setModelo("Navidad 3000");
		disco.setNumPromocion(null);
		disco.setPrecioBase(new BigDecimal("1400.00"));
		disco.setTipo("disco-duro");
		return disco;
	}

}
