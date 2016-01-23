package skylab.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import skylab.service.CoordinateService;
import skylab.service.LogicService;
import skylab.service.UserService;

@Controller
public class BattleController {

	@Autowired
	private CoordinateService coordinateService;

	@Autowired
	private UserService userService;

	@Autowired
	private LogicService logicService;

	@RequestMapping(value = "/")
	public String showIndex(HttpServletRequest request,
			HttpServletResponse response, Model model) {
		Cookie cookie = userService.findBattleCookie(request.getCookies());
		if (cookie == null) {
			model.addAttribute("isOld", false);
			int id = userService.create();
			coordinateService.createCookie(response, id);
		} else {
			model.addAttribute("isOld", true);
		}
		return "index";
	}

	@RequestMapping(value = "/battle/{path}")
	public String showBattleGround(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "path") String path, Model model) {
		Cookie cookie = userService.findBattleCookie(request.getCookies());
		if (path.equals("new")) {
			if (cookie == null) {
				int id = userService.create();
				coordinateService.createCookie(response, id);
				int[][] battleGroundUser = logicService.placeAllShips();
				int[][] battleGroundCpu = logicService.placeAllShips();
				coordinateService.saveBattleGround(id, battleGroundUser, battleGroundCpu);
				model.addAttribute("battleGround", battleGroundUser);
				model.addAttribute("battleGroundCpu", battleGroundCpu);
			} else {
				int id = Integer.parseInt(cookie.getValue());
				int[][] battleGroundUser = logicService.placeAllShips();
				int[][] battleGroundCpu = logicService.placeAllShips();
				try {
					coordinateService.reCreateBattleGround(id, battleGroundUser, battleGroundCpu);
				} catch (NullPointerException e) {
					id = userService.create();
					coordinateService.createCookie(response, id);
					coordinateService.saveBattleGround(id, battleGroundUser, battleGroundCpu);
				}
				model.addAttribute("battleGround", battleGroundUser);
				model.addAttribute("battleGroundCpu", battleGroundCpu);
			}
		} else if (path.equals("previously")) {
			int id = Integer.parseInt(cookie.getValue());
			int[][] battleGroundUser = new int[10][10];
			int[][] battleGroundCpu = new int[10][10];
			coordinateService.restoreBattleGround(id, battleGroundUser, battleGroundCpu);
			model.addAttribute("battleGround", battleGroundUser);
			model.addAttribute("battleGroundCpu", battleGroundCpu);
		} else {
			int xy = 0;
			try {
				xy = Integer.parseInt(path);
			} catch (NumberFormatException e) {
				return "redirect:/battle/previously";
			}
			int id = Integer.parseInt(cookie.getValue());
			int[][] battleGroundUser = new int[10][10];
			int[][] battleGroundCpu = new int[10][10];
			coordinateService.restoreBattleGround(id, battleGroundUser, battleGroundCpu);
			coordinateService.userShoot(id, xy, battleGroundUser, battleGroundCpu);
			if(coordinateService.isUserWin(battleGroundCpu)){
				model.addAttribute("userWin", true);
				return "battleGround";
			}
			if(coordinateService.isCpuWin(battleGroundUser)){
				model.addAttribute("cpuWin", true);
				return "battleGround";
			}
			model.addAttribute("battleGround", battleGroundUser);
			model.addAttribute("battleGroundCpu", battleGroundCpu);
		}
		return "battleGround";
	}
}
