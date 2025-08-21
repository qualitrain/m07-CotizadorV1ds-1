package mx.com.qtx.cotizadorv1ds.persistencia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.api.function.Executable;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import mx.com.qtx.cotizadorv1ds.servicios.dtos.ComponenteDTO;

class ComponenteDaoTest {
	private static boolean bdOperable = false;
	private static ComponenteDao dao = new ComponenteDao();
	private static ComponenteDTO dto;
	private ComponenteDao daoLocal;

	
    @BeforeAll
    public static void prepararBdPruebas(){
    	System.out.println("prepararBdPruebas()");
    	try {
    		dao.getConnection();
    		bdOperable = true;
    		dto = getComponenteDtoTest();
    		dao.insertarComponenteDto(dto);
    	}
    	catch(SQLIntegrityConstraintViolationException sicvex) {
    		System.out.println(sicvex.getClass().getName() + ":[" + sicvex.getMessage() + "]");
    		bdOperable = true;
    		return;
    	}
    	catch(Exception ex) {
    		System.out.println(ex.getClass().getName() + ":[" + ex.getMessage() + "]");
    		bdOperable = false;
    		
    		if(ex instanceof SQLException) {
    			SQLException sqlEx = (SQLException) ex;
    			System.out.println("Codigo Error del fabricante:" + sqlEx.getErrorCode());
    		}
    	}
    }
    
    @AfterAll
    public static void limpiarBdPruebas(){
    	if(bdOperable) {
    		try {
    			dao.eliminarComponenteDto(dto.getIdComponente());
    		}
        	catch(Exception ex) {
        		System.out.println(ex.getClass().getName() + ":[" + ex.getMessage() + "]");
        	}
   	}
    }


	private static ComponenteDTO getComponenteDtoTest() {
		ComponenteDTO dto = new ComponenteDTO();
		
		dto.setCapacidadAlm("2TB");
		dto.setCosto(new BigDecimal("500.00"));
		dto.setDescripcion("Disco estado solido 2Tb");
		dto.setMarca("Seagate");
		dto.setIdComponente("DD-1");
		dto.setMemoria(null);
		dto.setModelo("Universal 501");
		dto.setNumPromocion(null);
		dto.setPrecioBase(new BigDecimal("1000.00"));
		dto.setTipo("DiscoDuro");
		return dto;
	}


	@BeforeEach
	void setUp() throws Exception {
		this.daoLocal = new ComponenteDao();
		
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testComponenteDao_StringStringString_ArgUrlNulo() {
		//Dados
		String url = null;
		String user = "root";
		String password = "root";
		
		//Cuando
		Executable constructorStrStrStr = ()->{ new ComponenteDao(url,user,password); };
		//Entonces
		assertThrows(IllegalArgumentException.class, constructorStrStrStr, "No se lanza Excepcion con url nula");
	}
	
	@Test
	void testComponenteDao_StringStringString_ArgUrlVacio() {
		fail("Not yet implemented");
	}
	
	@Test
	void testComponenteDao_StringStringString_ArgUrlInvalido() {
		fail("Not yet implemented");
	}

	@Test
	void testComponenteDao() {
		//Dados
		
		//Cuando
		ComponenteDao dao = new ComponenteDao();
		//Entonces
		assertThat(dao.getUrl()).isNotEmpty()
		                        .isNotBlank()
		                        .isNotEqualTo(null)
		                        .doesNotStartWithWhitespaces()
		                        .hasSizeGreaterThan(20);
		
		assertThat(dao.getUser()).isNotEmpty()
                                 .isNotBlank()
                                 .isNotEqualTo(null)
                                 .doesNotStartWithWhitespaces()
                                 .hasSizeGreaterThan(3);
		
		assertThat(dao.getPassword()).isNotEmpty()
                                     .isNotBlank()
                                     .isNotEqualTo(null)
                                     .doesNotStartWithWhitespaces()
                                     .hasSizeGreaterThan(3);
		
	}

	@Test
	@Tag("CRUD_Componente")
	void testGetComponenteDtoXID() throws SQLException {
		assumeTrue(bdOperable);
		
		//Dados
		String idComponente = ComponenteDaoTest.dto.getIdComponente();
		
		//Cuando
		ComponenteDTO dtoLocal = daoLocal.getComponenteDtoXID(idComponente);
		
		//Entonces

		assertNotNull(dto,"dto nulo");
		assertEquals(dtoLocal.getIdComponente(), dto.getIdComponente());
		//Otros asserts
	}
	
	@ParameterizedTest(name="Prueba num {index} - valor probado:[{0}]")
	@NullAndEmptySource
	@ValueSource(strings= {"\n\n","\t\t","\t \n","      "})
	void testGetComponenteDtoXID_IdInvalido(String idComponente) throws SQLException {
		//Dados
		assumeTrue(bdOperable);
		//Cuando
		Executable lectura = ()->daoLocal.getComponenteDtoXID(idComponente);
		
		//Entonces
		assertThrows(IllegalArgumentException.class, lectura, "No lanza IllegalArgumentException");
	}

	@Test
	@Tag("CRUD_Componente")
	@Timeout(value = 100, unit = TimeUnit.MILLISECONDS)
	void testInsertarComponenteDto() throws SQLException {
		assumeTrue(bdOperable);
		
		//Dados
		ComponenteDTO dtoLocal = getComponenteDtoInsert();
		
		//Cuando
		boolean insercionExitosa = this.daoLocal.insertarComponenteDto(dtoLocal);
		
		//Entonces
		ComponenteDTO dtoLeido = this.daoLocal.getComponenteDtoXID(dtoLocal.getIdComponente());
		assertTrue(insercionExitosa,"No se devuelve true en lectura de objeto insertado en BD");
		assertEquals(dtoLeido.getIdComponente(), dtoLocal.getIdComponente());
		assertEquals(dtoLeido.getCosto().floatValue(), dtoLocal.getCosto().floatValue());
		
		this.daoLocal.eliminarComponenteDto(dtoLocal.getIdComponente());
	}

	private ComponenteDTO getComponenteDtoInsert() {
		ComponenteDTO dto = new ComponenteDTO();
		
		dto.setCapacidadAlm(null);
		dto.setCosto(new BigDecimal("700.00"));
		dto.setDescripcion("TarjetaVideo");
		dto.setMarca("Sony");
		dto.setIdComponente("TVD-1");
		dto.setMemoria("700Gb");
		dto.setModelo("Tiger-23");
		dto.setNumPromocion(null);
		dto.setPrecioBase(new BigDecimal("1400.00"));
		dto.setTipo("TarjetaVideo");
		return dto;
	}

	@Test
	@Tag("CRUD_Componente")
	@Disabled
	void testActualizarComponenteDto() {
		fail("Not yet implemented");
	}

	@Test
	@Tag("CRUD_Componente")
	void testEliminarComponenteDto() {
		fail("Not yet implemented");
	}

	@Test
	void testGetSubcomponentes() {
		fail("Not yet implemented");
	}

	@ParameterizedTest(name = "Prueba {index} con id=[{0}]")
	@MethodSource("getIdsPromoInvalidos")
	@NullSource
	void testGetPromocionXID_IdInvalido(Long idPromo) {
		//Dados
		assumeTrue(bdOperable);
		//Cuando
		Executable lectura = ()->daoLocal.getPromocionXID(idPromo);
		
		//Entonces
		assertThrows(IllegalArgumentException.class, lectura, "No lanza IllegalArgumentException");
	}
	
 	public static Stream<Long> getIdsPromoInvalidos(){
 		return List.of(0L, -1L, -5L).stream();
 	}
    

	@Test
	void testGetDetallesPromocion() {
		fail("Not yet implemented");
	}

	@Test
	void testGetDesctosPromocion() {
		fail("Not yet implemented");
	}

}
