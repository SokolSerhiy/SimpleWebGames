package skylab.dao.implementation;

import org.springframework.stereotype.Repository;

import skylab.dao.UserDao;
import skylab.entity.User;
@Repository
public class UserDaoImpl extends GeneralDaoImpl<User, Integer> implements UserDao{

	public UserDaoImpl() {
		super(User.class);
	}

}
