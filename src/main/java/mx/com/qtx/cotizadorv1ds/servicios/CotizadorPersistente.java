package mx.com.qtx.cotizadorv1ds.servicios;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import mx.com.qtx.cotizadorv1ds.core.ICotizador;
import mx.com.qtx.cotizadorv1ds.core.componentes.Componente;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.ComponenteInvalidoException;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.Cotizacion;
import mx.com.qtx.cotizadorv1ds.core.cotizaciones.DetalleCotizacion;
import mx.com.qtx.cotizadorv1ds.persistencia.CotizacionDao;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.CotizacionDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.DetalleCotizacionDTO;

//@Component
public class CotizadorPersistente implements ICotizador {
	private Map<Componente,Integer> mapCompsYcants;
	private IGestorPersistenciaCotizaciones gestorPersistencia;

	public CotizadorPersistente() {
		super();
		this.mapCompsYcants = new HashMap<>();
		this.gestorPersistencia = new CotizacionDao();
	}

	@Override
	public void agregarComponente(int cantidad, Componente componente) {
		this.mapCompsYcants.put(componente, cantidad);
	}

	@Override
	public void eliminarComponente(String idComponente) throws ComponenteInvalidoException {
		try {
	    	   Componente llave = this.mapCompsYcants.keySet()
	    			                    .stream()
	    			                    .filter(k->k.getId().equals(idComponente))
	    			                     .findFirst()
	    			                     .get();
	               
			
	    	   this.mapCompsYcants.remove(llave);
			}
			catch(Exception ex) {
				throw new ComponenteInvalidoException("Error subyacente", ex, null);
			}
	}

	@Override
	public Cotizacion generarCotizacion() {
        BigDecimal total = new BigDecimal(0);
        
        Cotizacion cotizacion = new CotizacionFmtoC();
        int i=0;
        for(Componente compI:this.mapCompsYcants.keySet()) {
        	int cantidadI = this.mapCompsYcants.get(compI);
        	BigDecimal importeCotizadoI = new BigDecimal(0);
        	i++;
        	
        	importeCotizadoI = compI.cotizar(cantidadI);
        	        
        	DetalleCotizacion detI = new DetalleCotizacion((i), compI.getId(), compI.getDescripcion(), cantidadI, 
        			                                        compI.getPrecioBase(), importeCotizadoI, compI.getCategoria());
        	cotizacion.agregarDetalle(detI);
            total = total.add(importeCotizadoI);
        }
        cotizacion.setTotal(total);
        
        long numCotizacion = this.persistirCotizacion(cotizacion);
        cotizacion.setNum(numCotizacion);
        
		return cotizacion;
	}

	private long persistirCotizacion(Cotizacion cotizacion) {
//		System.out.println("=== persistirCotizacion ===");
		CotizacionDTO cotDTO = new CotizacionDTO();
		List<DetalleCotizacionDTO> lstDetsCotDTO = new ArrayList<>();
		
		cotDTO.setFecCotizacion(cotizacion.getFecha().atTime(0, 0, 0));
		cotDTO.setTotal(cotizacion.getTotal());
		for(Integer numDet : cotizacion.getDetalles().keySet()) {
			DetalleCotizacion detI = cotizacion.getDetalles().get(numDet);
			DetalleCotizacionDTO detDTOi = new DetalleCotizacionDTO();
			this.copiarDetalleAdetDTO(detI,detDTOi);
			lstDetsCotDTO.add(detDTOi);
		}
		
		long numCotizacion = 0;
		try {
			numCotizacion = this.gestorPersistencia.insertarCotizacion(cotDTO,lstDetsCotDTO);
		} 
		catch (SQLException e) {
			e.printStackTrace();
			numCotizacion = -1;
		}
		
		return numCotizacion;
	}

	private void copiarDetalleAdetDTO(DetalleCotizacion detI, DetalleCotizacionDTO detDTOi) {
		detDTOi.setCantidad(detI.getCantidad());
		detDTOi.setCategoria(detI.getCategoria());
		detDTOi.setDescripcion(detI.getDescripcion());
		detDTOi.setIdComponente(detI.getIdComponente());
		detDTOi.setImporteCotizado(detI.getImporteCotizado());
		detDTOi.setNumDetalle(detI.getNumDetalle());
		detDTOi.setPrecioBase(detI.getPrecioBase());
	}

	@Override
	public void listarComponentes() {
        System.out.println("=== Componentes a cotizar en " + this.getClass().getSimpleName()
        		+ " ===");
        for(Componente compI:this.mapCompsYcants.keySet())  {
        	int cantidad = this.mapCompsYcants.get(compI);
            System.out.println(cantidad + " " + compI.getDescripcion() 
            		 + ": $" + compI.getPrecioBase() + " ID:" + compI.getId());        	
        }
	}

	@Override
	public Cotizacion getCotizacionXID(long id) {
		 try {
			CotizacionDTO cotDTO = this.gestorPersistencia.getCotizacionXID(id);
			List<DetalleCotizacionDTO> detsDTO = this.gestorPersistencia.getDetallesCotizacion(id);
			
			Cotizacion cot = new CotizacionFmtoC();
			cot.setNum(cotDTO.getNumCotizacion());
			cot.setFecha(cotDTO.getFecCotizacion().toLocalDate());
			BigDecimal total = new BigDecimal(0);
			for(DetalleCotizacionDTO detI :detsDTO ) {
				DetalleCotizacion detCotI = new DetalleCotizacion(detI.getNumDetalle(), detI.getIdComponente(), 
						                                          detI.getDescripcion(), detI.getCantidad(), 
						                                          detI.getPrecioBase(), detI.getImporteCotizado(), 
						                                          detI.getCategoria());
				cot.agregarDetalle(detCotI);
				total = total.add(detI.getImporteCotizado());
			}
			cot.setTotal(total);
			return cot;
		 } 
		 catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
