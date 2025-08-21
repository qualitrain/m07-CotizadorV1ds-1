package mx.com.qtx.cotizadorv1ds.servicios;

import java.sql.SQLException;
import java.util.List;

import mx.com.qtx.cotizadorv1ds.servicios.dtos.CotizacionDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.DetalleCotizacionDTO;

public interface IGestorPersistenciaCotizaciones {
	CotizacionDTO insertarCotizacion(CotizacionDTO cotizacion) throws SQLException; 
	CotizacionDTO getCotizacionXID(long numCotizacion) throws SQLException; 
	DetalleCotizacionDTO insertarDetalleCotizacion(DetalleCotizacionDTO detCot) throws SQLException; 
	List<DetalleCotizacionDTO> getDetallesCotizacion(long numCotizacion) throws SQLException;
	long insertarCotizacion(CotizacionDTO cotDTO, List<DetalleCotizacionDTO> lstDetsCotDTO) throws SQLException; 
}
