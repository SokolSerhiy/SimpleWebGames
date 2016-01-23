package skylab.dao;

import java.util.List;

public interface GeneralDao <C, N extends Number>{
	
	void create(C entity);
	void update(C entity);
	List<C> getAll();
	void delete(C entity);
	C findOneById(N id);
}
