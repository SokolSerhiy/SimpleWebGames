package skylab.service.implementation;

import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import skylab.dao.CoordinateDao;
import skylab.dao.UserDao;
import skylab.entity.Coordinate;
import skylab.entity.User;
import skylab.service.CoordinateService;
import skylab.service.UserService;

@Service
public class CoordinateServiceImpl implements CoordinateService {

	public static final int NOTHING = 0;
	public static final int USER_SHIP = 1;
	public static final int CPU_SHIP = 2;
	public static final int USER_SHOOT_HIT = 3;
	public static final int CPU_SHOOT_HIT = 4;
	public static final int USER_SHOOT_MISS = 5;
	public static final int CPU_SHOOT_MISS = 6;

	@Autowired
	private CoordinateDao coordinateDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private UserService userServise;

	public void create(Coordinate coordinate) {
		coordinateDao.create(coordinate);
	}

	public void update(Coordinate coordinate) {
		coordinateDao.update(coordinate);
	}

	public List<Coordinate> getAll() {
		return coordinateDao.getAll();
	}

	public void createCookie(HttpServletResponse response, int id) {
		Cookie cookie = new Cookie("BattleShip", new Integer(id).toString());
		cookie.setMaxAge(Integer.MAX_VALUE);
		response.addCookie(cookie);
	}

	@Transactional
	public void saveBattleGround(int id, int[][] battleGroundUser,
			int[][] battleGroundCpu) {
		User user = userDao.findOneById(id);
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (battleGroundUser[i][j] == 1) {
					create(new Coordinate(USER_SHIP, i, j, user));
				}
				if (battleGroundCpu[i][j] == 1) {
					battleGroundCpu[i][j] = CPU_SHIP;
					create(new Coordinate(CPU_SHIP, i, j, user));
				}
			}
		}
	}

	@Transactional
	public void reCreateBattleGround(int id, int[][] battleGroundUser,
			int[][] battleGroundCpu) {
		User user = userDao.findOneById(id);
		Hibernate.initialize(user.getCoordinates());
		List<Coordinate> coordinates = user.getCoordinates();
		for (Coordinate coordinate : coordinates) {
			coordinateDao.delete(coordinate);
		}
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (battleGroundUser[i][j] == 1) {
					create(new Coordinate(USER_SHIP, i, j,
							user));
				}
				if (battleGroundCpu[i][j] == 1) {
					battleGroundCpu[i][j] = CPU_SHIP;
					create(new Coordinate(CPU_SHIP, i, j, user));
				}
			}
		}
	}

	@Transactional
	public void restoreBattleGround(int id, int[][] battleGroundUser,
			int[][] battleGroundCpu) {
		User user = userDao.findOneById(id);
		Hibernate.initialize(user.getCoordinates());
		List<Coordinate> coordinates = user.getCoordinates();
		for (Coordinate coordinate : coordinates) {
			if (coordinate.getStatus() == USER_SHIP) {
				battleGroundUser[coordinate.getX()][coordinate.getY()] = USER_SHIP;
			}
			if (coordinate.getStatus() == CPU_SHOOT_HIT) {
				battleGroundUser[coordinate.getX()][coordinate.getY()] = CPU_SHOOT_HIT;
			}
			if (coordinate.getStatus() == CPU_SHOOT_MISS) {
				battleGroundUser[coordinate.getX()][coordinate.getY()] = CPU_SHOOT_MISS;
			}
			if (coordinate.getStatus() == CPU_SHIP) {
				battleGroundCpu[coordinate.getX()][coordinate.getY()] = CPU_SHIP;
			}
			if (coordinate.getStatus() == USER_SHOOT_HIT) {
				battleGroundCpu[coordinate.getX()][coordinate.getY()] = USER_SHOOT_HIT;
			}
			if (coordinate.getStatus() == USER_SHOOT_MISS) {
				battleGroundCpu[coordinate.getX()][coordinate.getY()] = USER_SHOOT_MISS;
			}
		}
	}

	public int random(int min, int max) {
		return (int) (Math.round((Math.random() * (max - min) + min)));
	}

	@Transactional
	public void userShoot(int id, int xy, int[][] battleGroundUser,
			int[][] battleGroundCpu) {
		User user = userDao.findOneById(id);
		int y = xy % 10;
		int x = (xy - y) / 10;
		if (battleGroundCpu[x][y] == NOTHING) {
			battleGroundCpu[x][y] = USER_SHOOT_MISS;
			create(new Coordinate(USER_SHOOT_MISS, x, y, user));
		} else if (battleGroundCpu[x][y] == CPU_SHIP) {
			battleGroundCpu[x][y] = USER_SHOOT_HIT;
			create(new Coordinate(USER_SHOOT_HIT, x, y, user));
		}
		boolean isShootCorrect = false;
		while (!isShootCorrect) {
			y = random(0, 9);
			x = random(0, 9);
			if (battleGroundUser[x][y] == NOTHING) {
				battleGroundUser[x][y] = CPU_SHOOT_MISS;
				create(new Coordinate(CPU_SHOOT_MISS, x, y, user));
				isShootCorrect = true;
			}
			if (battleGroundUser[x][y] == USER_SHIP) {
				battleGroundUser[x][y] = CPU_SHOOT_HIT;
				create(new Coordinate(CPU_SHOOT_HIT, x, y, user));
				isShootCorrect = true;
			}
		}
	}

	public boolean isUserWin(int[][] battleGroundCpu) {
		int count = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (battleGroundCpu[i][j] == USER_SHOOT_HIT) {
					count++;
				}
			}
		}
		if (count == 20) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isCpuWin(int[][] battleGroundUser) {
		int count = 0;
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				if (battleGroundUser[i][j] == CPU_SHOOT_HIT) {
					count++;
				}
			}
		}
		if (count == 20) {
			return true;
		} else {
			return false;
		}
	}
}
