package mx.com.qtx.cotizadorv1ds.casosDeUso;

import mx.com.qtx.cotizadorv1ds.core.ICotizador;
import mx.com.qtx.cotizadorv1ds.core.IServicioComponentes;
import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.servicios.CotizadorPersistente;
import mx.com.qtx.cotizadorv1ds.servicios.GestorComponentes;

public class CotizadorBDTest {

	public static void main(String[] args) {
		testGenerarCotizacion();

	}

	private static void testGenerarCotizacion() {
		ICotizador cotizador = getCotizadorActual();
		IServicioComponentes gestorComponentes = getServicioComponentes();
		
		Componente monitor = gestorComponentes.getComponenteXID("MON01");
		cotizador.agregarComponente(10, monitor);

		Componente monitor2 = gestorComponentes.getComponenteXID("MON02");
		cotizador.agregarComponente(7, monitor2);
		
		Componente disco = gestorComponentes.getComponenteXID("HD01");
		cotizador.agregarComponente(10, disco);
		
		Componente tarjeta = gestorComponentes.getComponenteXID("GPU01");
		cotizador.agregarComponente(10, tarjeta);
//		Promocion promo = (Promocion) tarjeta.getPromo();
//		Promocion.mostrarEstructuraPromocion(promo);
		
		Componente pc = gestorComponentes.getComponenteXID("PC01");
		pc.mostrarCaracteristicas();
		cotizador.agregarComponente(1, pc);
		
		Cotizacion cotizacion = cotizador.generarCotizacion();
		cotizacion.emitirComoReporte();
		
		System.out.println("\n COTIZACION EN BD: \n");
		
		long idCotizacion = cotizacion.getNum();
		Cotizacion cotBd = cotizador.getCotizacionXID(idCotizacion);
		cotBd.emitirComoReporte();
	}

	private static IServicioComponentes getServicioComponentes() {
		IServicioComponentes gestorComponentes = new GestorComponentes();
		return gestorComponentes;
	}

	private static ICotizador getCotizadorActual() {
		ICotizador cotizador = new CotizadorPersistente();
		return cotizador;
	}

}
