package mx.com.qtx.cotizadorv1ds.core.promos;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class PromocionTest {

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
	

}
