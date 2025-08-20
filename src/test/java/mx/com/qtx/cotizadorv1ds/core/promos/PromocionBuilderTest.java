package mx.com.qtx.cotizadorv1ds.core.promos;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

class PromocionBuilderTest {

	@Test
	void testBuild_sinPromBase_conDsctoPlano() {
		
		//Dados
		PromocionBuilder builderPromo = Promocion.getBuilder();
		assumeTrue(builderPromo != null);
		
		float dscto = 10f;
		
		//Cuando
		builderPromo.agregarDsctoPlano(dscto);
		Promocion promo = builderPromo.build();
		
		//entonces
		assertInstanceOf(PromAcumulable.class, promo, "Promocion no es instancia de PromAcumulable");
		PromAcumulable promoAcum = (PromAcumulable) promo;
		assertInstanceOf(PromSinDescto.class,promoAcum.getPromoBase(),
				         "Promocion base no es instancia de PromSinDescto");
		Promocion.mostrarEstructuraPromocion(promo);
	}
	
	@RepeatedTest(5)
	void testBuild_conDsctoPlano_invalido(RepetitionInfo repInfo) {
		//Dados
		float dsctos[]= {-10f,0f,95f,100f,200f};
		float dsctoI = dsctos[repInfo.getCurrentRepetition() - 1];
		//Cuando
		Executable crarPromoDsctoInvalido = ()->{
											 Promocion.getBuilder().agregarDsctoPlano(dsctoI).build();
										    };
		//Entonces								    
		assertThrows(IllegalArgumentException.class, crarPromoDsctoInvalido,"Descuento fuera de rango válido");						   
	}

}
