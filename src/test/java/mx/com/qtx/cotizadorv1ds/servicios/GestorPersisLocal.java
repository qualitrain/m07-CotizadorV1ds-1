package mx.com.qtx.cotizadorv1ds.servicios;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import mx.com.qtx.cotizadorv1ds.servicios.dtos.ComponenteDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.DetallePromoDsctoXCantDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.DetallePromocionDTO;
import mx.com.qtx.cotizadorv1ds.servicios.dtos.PromocionDTO;

public class GestorPersisLocal implements IGestorPersistenciaComponentes {

	@Override
	public ComponenteDTO getComponenteDtoXID(String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Map<ComponenteDTO, Integer> getSubcomponentes(String id) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PromocionDTO getPromocionXID(Long numPromocion) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetallePromocionDTO> getDetallesPromocion(Long numPromocion) throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<DetallePromoDsctoXCantDTO> getDesctosPromocion(Long numPromocion, Integer numDetPromocion)
			throws SQLException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean insertarComponenteDto(ComponenteDTO dto) throws SQLException {
		// TODO Auto-generated method stub
		return false;
	}

}
