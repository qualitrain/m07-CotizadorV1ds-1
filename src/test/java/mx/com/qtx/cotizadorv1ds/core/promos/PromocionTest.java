package mx.com.qtx.cotizadorv1ds.core.promos;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

class PromocionTest {
	static int nPrueba = 0;
	
	@BeforeEach
	@Timeout(value=1,unit = TimeUnit.MICROSECONDS)
	void preProcesarPruebaI() {
		nPrueba++;
		System.out.println("Ejecutando prueba " + nPrueba++);
	}

	@Test
	void testCalcularImportePromocion_dsctoPlano() {
		//Dados
		float descto = 10f;
		int cantidad = 10;
		float fPrecioBase = 2000f;
		float delta = 0.000001f;
		
		BigDecimal precioBase = new BigDecimal(fPrecioBase);
		assumeTrue(precioBase != null);
		Promocion prom = Promocion.getBuilder()
				         .agregarDsctoPlano(descto)
				         .build();
		assumeTrue(prom != null);
		
		float importePromoEsperado = cantidad * fPrecioBase * (1 - (descto / 100));
		//Cuando
		BigDecimal importePromo = prom.calcularImportePromocion(cantidad, precioBase);
		//Entonces
		assertNotNull(importePromo);
		assertEquals(importePromoEsperado, importePromo.floatValue(), delta);
	}
	
	@RepeatedTest(5)
	void testCalcularImportePromocion_dsctoPlano(RepetitionInfo repInfo) {
		//Dados
		float dsctos[]= {5f,10f,15f,20f,25f};
		int cantidad = 10;
		float fPrecioBase = 2000f;
		float delta = 0.000001f;
		float descto = dsctos[repInfo.getCurrentRepetition() - 1];
		
		BigDecimal precioBase = new BigDecimal(fPrecioBase);
		assumeTrue(precioBase != null);
		Promocion prom = Promocion.getBuilder()
				         .agregarDsctoPlano(descto)
				         .build();
		assumeTrue(prom != null);
		
		float importePromoEsperado = cantidad * fPrecioBase * (1 - (descto / 100));
		//Cuando
		BigDecimal importePromo = prom.calcularImportePromocion(cantidad, precioBase);
		//Entonces
		assertNotNull(importePromo);
		assertEquals(importePromoEsperado, importePromo.floatValue(), delta);
	}
	
	@ParameterizedTest
	@ValueSource(ints = {1,2,3,4,5,6})
	void testCalcularImportePromocion_3X2(int cantidad) {
		//Dados
		int lleveN = 3;
		int pagueM = 2;
		float fPrecioBase = 2000f;
		float delta = 0.000001f;
		BigDecimal precioBase = new BigDecimal(fPrecioBase);
		assumeTrue(precioBase != null);
		Promocion prom = Promocion.getBuilder()
						         .conPromocionBaseNXM(lleveN, pagueM)
				                 .build();
		assumeTrue(prom != null);
		
//		float importePromoEsperado = cantidad * fPrecioBase ;
		float importePromoEsperado = switch(cantidad) {
			case 1-> 1 * fPrecioBase;
			case 2-> 2 * fPrecioBase;
			case 3-> 2 * fPrecioBase;
			case 4-> 3 * fPrecioBase;
			case 5-> 4 * fPrecioBase;
			case 6-> 4 * fPrecioBase;
			default->{
				throw new IllegalArgumentException("Unexpected value: " + cantidad);
			}
		};
		
		//Cuando
		BigDecimal importePromo = prom.calcularImportePromocion(cantidad, precioBase);
		
		//Entonces
		assertNotNull(importePromo);
		assertEquals(importePromoEsperado, importePromo.floatValue(), delta);
	}
	
	@Test
	@Disabled
	void testCalcularImportePromocion_DsctoXcantidad() {
		//Dados
		//Tabla de cantidades y Descuentos
		int cant1 = 10;
		double descto1 = 10.0;
		int cant2 = 20;
		double descto2 = 20.0;
		int cant3 = 30;
		double descto3 = 30.0;
		
		int cantidad = 15;
		float fPrecioBase = 2000f;
		float delta = 0.000001f;
		BigDecimal precioBase = new BigDecimal(fPrecioBase);
		assumeTrue(precioBase != null);
		
		double importePromoEsperado = cantidad * (1 - (descto1/100)) * fPrecioBase;
		Promocion prom = Promocion.getBuilder()
		         .conPromocionBaseSinDscto().agregarDsctoXcantidad(
		        		 Map.of(
		        				 cant1,descto1,
		        				 cant2,descto2,
		        				 cant3,descto3
		        				 )
		        		 )
                .build();
         assumeTrue(prom != null);
		
		//Cuando
 		BigDecimal importePromo = prom.calcularImportePromocion(cantidad, precioBase);
		
		//Entonces
		assertNotNull(importePromo);
		assertEquals(importePromoEsperado, importePromo.doubleValue(), delta);
	}
	
	@ParameterizedTest(name= "Prueba {index} con descuentos: {0}-{1}, {2}-{3}, {4}-{5} y cantidad = {6}")
	@MethodSource("getDescuentosYcantidades")
	void testCalcularImportePromocion_DsctoXcantidad(int cant1, double descto1,
													int cant2, double descto2,
													int cant3, double descto3,
													int cantidad){
		//Dados
		float fPrecioBase = 2000f;
		float delta = 0.000001f;
		
		BigDecimal precioBase = new BigDecimal(fPrecioBase);
		assumeTrue(precioBase != null);
		
		double importePromoEsperado = 0;
		
		if(cantidad >= cant3) {
			importePromoEsperado = cantidad * (1 - (descto3/100)) * fPrecioBase;
		}
		else
		if(cantidad >= cant2) {
			importePromoEsperado = cantidad * (1 - (descto2/100)) * fPrecioBase;
		}
		else
		if(cantidad >= cant1) {
			importePromoEsperado = cantidad * (1 - (descto1/100)) * fPrecioBase;
		}
		else {
			importePromoEsperado = cantidad * fPrecioBase;
		}
		Promocion prom = Promocion.getBuilder()
		         .conPromocionBaseSinDscto().agregarDsctoXcantidad(
		        		 Map.of( 0,    0.0,
		        				 cant1,descto1,
		        				 cant2,descto2,
		        				 cant3,descto3
		        				 )
		        		 )
                .build();
         assumeTrue(prom != null);
		
		//Cuando
        System.out.println("cantidad=" + cantidad + ", " + "precioBase=" + precioBase);
 		BigDecimal importePromo = prom.calcularImportePromocion(cantidad, precioBase);
		
		//Entonces
		assertNotNull(importePromo);
		assertEquals(importePromoEsperado, importePromo.doubleValue(), delta);
	}
	
	public static Stream<Arguments> getDescuentosYcantidades(){
		return Stream.of(
				Arguments.arguments(10,10.0, 20,20.0, 30,30.0,1),
				Arguments.arguments(10,10.0, 20,20.0, 30,30.0,9),
				Arguments.arguments(10,10.0, 20,20.0, 30,30.0,10),
				Arguments.arguments(10,10.0,20,20.0,30,30.0,15),
				Arguments.arguments(10,10.0,20,20.0,30,30.0,19),
				Arguments.arguments(10,10.0,20,20.0,30,30.0,20),
				Arguments.arguments(10,10.0,20,20.0,30,30.0,21),
				Arguments.arguments(10,10.0,20,20.0,30,30.0,30),
				Arguments.arguments(10,10.0,20,20.0,30,30.0,35),
				Arguments.arguments(10,5.0, 40,10.0,100,20.0,150)
				);
	}
}
