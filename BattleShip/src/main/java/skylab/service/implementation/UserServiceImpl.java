package skylab.service.implementation;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import skylab.dao.UserDao;
import skylab.entity.User;
import skylab.service.UserService;
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;

	public int create() {
		User user = new User();
		userDao.create(user);
		return user.getId();
	}

	public void update(User user) {
		userDao.update(user);
	}
	
	public Cookie findBattleCookie(Cookie[] cookies){
		if(cookies == null) return null;
		Cookie cookie = null;
		for (int i = 0; i < cookies.length; i++) {
//			System.out.println(cookies[i].getName());
			if(cookies[i].getName().equals("BattleShip")){
				cookie = cookies[i];
			}
		}
		return cookie;
	}

}
