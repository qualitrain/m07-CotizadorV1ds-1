package mx.com.qtx.cotizadorv1ds.casosDeUso;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import mx.com.qtx.cotizadorv1ds.core.ICotizador;
import mx.com.qtx.cotizadorv1ds.core.IServicioComponentes;
import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.servicios.CotizadorPersistente;
import mx.com.qtx.cotizadorv1ds.servicios.GestorComponentes;

@Component
public class ProbadorCotizadorBD implements CommandLineRunner{
	
	private static Logger bitacora = LoggerFactory.getLogger(ProbadorCotizadorBD.class);
	@Autowired
	ICotizador cotizador;
	
	@Autowired
	IServicioComponentes gestorComponentes;

	public static void main(String[] args) {
//		testGenerarCotizacion();

	}

	private void testGenerarCotizacion() {
//		ICotizador cotizador = getCotizadorActual();
//		IServicioComponentes gestorComponentes = getServicioComponentes();
		
		bitacora.info("ICotizador:" +  this.cotizador.getClass().getName());
		
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
		bitacora.info("idCotizacion:" +idCotizacion);
		
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

	@Override
	public void run(String... args) throws Exception {
		testGenerarCotizacion();
		
	}

}
