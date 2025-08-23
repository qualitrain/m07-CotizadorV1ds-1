package mx.com.qtx.cotizadorv1ds.servicios;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import mx.com.qtx.cotizadorv1ds.servicios.dtos.ComponenteDTO;

@SpringBootTest
class IGestorPersistenciaComponentesTest {
	
	@Autowired
	@Qualifier("beanGestorDatosTest")
	IGestorPersistenciaComponentes gestorPersistencia;
	
	private static Logger bitacora = LoggerFactory.getLogger(IGestorPersistenciaComponentesTest.class);	

	@Test
	void testGetComponenteDtoXID() throws SQLException {
		//Dados
		assumeTrue(this.gestorPersistencia != null);
		bitacora.info("Implementacion IGestorPersistenciaComponentes: " + this.gestorPersistencia.getClass().getName());
		ComponenteDTO componente = getComponenteDtoTest();
		assumeTrue(componente != null);
		this.gestorPersistencia.insertarComponenteDto(componente);
		
		//Cuando
		ComponenteDTO componenteLeido = this.gestorPersistencia.getComponenteDtoXID(componente.getIdComponente());
		
		assertThat(componenteLeido).isNotNull()
		                            .hasFieldOrPropertyWithValue("idComponente", componente.getIdComponente());
	}

	@Test
	void testGetSubcomponentes() {
		fail("Not yet implemented");
	}

	@Test
	void testGetPromocionXID() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDetallesPromocion() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDesctosPromocion() {
		fail("Not yet implemented");
	}
	
	private static ComponenteDTO getComponenteDtoTest() {
		ComponenteDTO dto = new ComponenteDTO();
		
		dto.setCapacidadAlm("2TB");
		dto.setCosto(new BigDecimal("500.00"));
		dto.setDescripcion("Disco estado solido 2Tb");
		dto.setMarca("Seagate");
		dto.setIdComponente("DD-2");
		dto.setMemoria(null);
		dto.setModelo("Universal 501");
		dto.setNumPromocion(null);
		dto.setPrecioBase(new BigDecimal("1000.00"));
		dto.setTipo("DiscoDuro");
		return dto;
	}
	
	@TestConfiguration
	public static class ConfiguracionPruebasLocal{
		@Bean(name = "beanGestorDatosTest")
		IGestorPersistenciaComponentes getGestorPersistenciaLocal() {
			return new GestorPersisLocal();
		}
	}
}

