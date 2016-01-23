package skylab.service;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import skylab.entity.Coordinate;

public interface CoordinateService {

	void create(Coordinate coordinate);

	void update(Coordinate coordinate);

	List<Coordinate> getAll();

	void saveBattleGround(int id, int[][] battleGroundUser,
			int[][] battleGroundCpu);
	
	void reCreateBattleGround(int id, int[][] battleGroundUser,
			int[][] battleGroundCpu);
	
	void restoreBattleGround(int id, int[][] battleGroundUser, int[][] battleGroundCpu);
	
	void userShoot(int id, int xy, int[][] battleGroundUser,
			int[][] battleGroundCpu);
	
	void createCookie(HttpServletResponse response, int id);
	
	boolean isCpuWin(int[][] battleGroundUser);
	
	boolean isUserWin(int[][] battleGroundCpu);
}
