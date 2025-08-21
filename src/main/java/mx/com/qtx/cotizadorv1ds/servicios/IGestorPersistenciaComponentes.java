package mx.com.qtx.cotizadorv1ds.servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import mx.com.qtx.cotizadorv1ds.servicios.dtos.ComponenteDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.DetallePromoDsctoXCantDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.DetallePromocionDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.PromocionDTO;

public interface IGestorPersistenciaComponentes {
	ComponenteDTO getComponenteDtoXID(String id) throws SQLException; 
	Map<ComponenteDTO, Integer> getSubcomponentes(String id) throws SQLException;
	PromocionDTO getPromocionXID(Long numPromocion) throws SQLException;
	List<DetallePromocionDTO> getDetallesPromocion(Long numPromocion) throws SQLException;
	List<DetallePromoDsctoXCantDTO> getDesctosPromocion(Long numPromocion, Integer numDetPromocion) throws SQLException;
	boolean insertarComponenteDto(ComponenteDTO dto) throws SQLException;
}
