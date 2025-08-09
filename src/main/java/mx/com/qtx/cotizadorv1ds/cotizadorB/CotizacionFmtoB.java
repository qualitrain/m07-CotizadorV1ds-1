package mx.com.qtx.cotizadorv1ds.cotizadorB;

import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.DetalleCotizacion;

public class CotizacionFmtoB extends Cotizacion {
	
	@Override
	public void emitirComoReporte() {
		System.out.println("===========================================================================================");
		System.out.println("Cotizacion número:" + this.num );
		System.out.println("Fecha:" + this.fecha );
		System.out.println("===========================================================================================\n");
		System.out.printf("%5s %-10s %-15s %-30s    %-12s %-12s\n\n","#", "Cantidad", "Id", "Descripcion", "Base", "Total"  );
		
		for(Integer k:this.detalles.keySet()) {
			this.desplegarLineaCotizacion(this.detalles.get(k));
		}
		
		System.out.printf("\n%88s","$" + String.format("%10.2f",this.getTotal()));
		
	}
	
	@Override
	protected void desplegarLineaCotizacion(DetalleCotizacion detI) {
		System.out.printf("%5d     %4d  %-15s %-30s $%10.2f   $%10.2f\n", detI.getNumDetalle(), detI.getCantidad(), detI.getIdComponente(),
				detI.getDescripcion(), detI.getPrecioBase(), detI.getImporteCotizado());
	}

}
