package skylab.service;

import javax.servlet.http.Cookie;

import skylab.entity.User;

public interface UserService {

	int create ();
	void update (User user);
	Cookie findBattleCookie(Cookie[] cookies);
}
