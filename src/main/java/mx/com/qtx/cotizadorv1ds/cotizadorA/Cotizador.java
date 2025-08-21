package mx.com.qtx.cotizadorv1ds.cotizadorA;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import mx.com.qtx.cotizadorv1ds.core.ICotizador;
import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.ComponenteInvalidoException;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.DetalleCotizacion;

@Component
public class Cotizador implements ICotizador{
    private List<Componente> componentes = new ArrayList<>();
    private List<Integer> cantidades = new ArrayList<>();

    // Métodos de gestión de componentes
    public void agregarComponente(int cantidad, Componente componente) {
    	this.cantidades.add(cantidad);
    	this.componentes.add(componente);
    }

    public void eliminarComponente(String idComponente) throws ComponenteInvalidoException {
    	if(idComponente == null) {
    		throw new ComponenteInvalidoException("Id del componente es nulo ", null);
    	}
    	int i = this.componentes.stream().map(compI->compI.getId())
    			                         .toList().indexOf(idComponente);
    	if (i==-1) {// NO existe
    		throw new ComponenteInvalidoException("No existe componente con Id "+ idComponente, null);
    	}
    	this.cantidades.remove(i);
    	this.componentes.remove(i);
    }

    public Cotizacion generarCotizacion() {
        BigDecimal total = new BigDecimal(0);
        
        Cotizacion cotizacion = new Cotizacion();
        
        for(int i=0; i<this.cantidades.size();i++) {
        	Componente compI = this.componentes.get(i);
        	int cantidadI = this.cantidades.get(i);
        	BigDecimal importeCotizadoI = new BigDecimal(0);
        	
        	importeCotizadoI = compI.cotizar(cantidadI);
        	        
        	DetalleCotizacion detI = new DetalleCotizacion((i + 1), compI.getId(), compI.getDescripcion(), cantidadI, 
        			                                        compI.getPrecioBase(), importeCotizadoI, compI.getCategoria());
        	cotizacion.agregarDetalle(detI);
            total = total.add(importeCotizadoI);
        }
        cotizacion.setTotal(total);
   	
		return cotizacion;
    }

	public void listarComponentes() {
        System.out.println("=== Componentes a cotizar ===");
        for(int i=0; i<this.cantidades.size();i++) {
        	Componente c = this.componentes.get(i);
            System.out.println(this.cantidades.get(i) + " " + c.getDescripcion() 
            		 + ": $" + c.getPrecioBase() + " ID:" + c.getId());        	
        }
    }
	
	Componente getComponenteCotizado(String idComponente) {
		return this.componentes.stream()
				               .filter(compI->compI.getId().equals(idComponente))
				               .findFirst()
				               .get();
	}

	@Override
	public Cotizacion getCotizacionXID(long id) {
		// TODO Auto-generated method stub
		return null;
	}

}