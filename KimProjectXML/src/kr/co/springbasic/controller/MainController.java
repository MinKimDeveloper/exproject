package kr.co.springbasic.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.co.springbasic.beans.BoardInfoBean;
import kr.co.springbasic.beans.ContentBean;
import kr.co.springbasic.service.MainService;
import kr.co.springbasic.service.TopMenuService;

@Controller
public class MainController {
	
	@Autowired
	private MainService MainService;
	
	// Main.jsp 에 보여질 게시판 리스트 이름 셋팅 하기 위해 사용
	@Autowired
	private TopMenuService TopMenuService;
	
	@GetMapping("/main")
	public String main(Model model) {
		
		ArrayList<List<ContentBean>> list = new ArrayList<List<ContentBean>>();
		
		for(int i = 1; i <= 2; i++) {
		List<ContentBean> list1 = MainService.getMainList(i);
		list.add(list1);
		
		}
		
		model.addAttribute("list", list);
		
		// Main.jsp 에 보여질 게시판 리스트 이름 처리 메서드 가져와 사용
		List<BoardInfoBean> board_list = TopMenuService.getTopMenuList();
		model.addAttribute("board_list", board_list);
		
		return "main";
	}
}
