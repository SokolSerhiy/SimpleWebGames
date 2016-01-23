package skylab.dao.implementation;

import org.springframework.stereotype.Repository;

import skylab.dao.CoordinateDao;
import skylab.entity.Coordinate;

@Repository
public class CoordinateDaoImpl extends GeneralDaoImpl<Coordinate, Integer> implements CoordinateDao{

	public CoordinateDaoImpl() {
		super(Coordinate.class);
	}
}
