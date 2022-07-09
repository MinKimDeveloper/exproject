package kr.co.springbasic.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.co.springbasic.beans.ContentBean;
import kr.co.springbasic.beans.PageBean;
import kr.co.springbasic.beans.UserBean;
import kr.co.springbasic.service.BoardService;

@Controller
@RequestMapping("/board")
public class BoardController {

	@Autowired
	private BoardService boardService;

	// 로그인한 사람 idx 를 가져오기 위해서...
	@Resource(name = "loginUserBean")
	@Lazy
	private UserBean loginUserBean;

	// TopMenuMapper 에서 board_info_idx 넘김
	// 아래 페이지 값 처음 게시판 클릭시는 page = 1 로 셋팅
	@GetMapping("/main")
	public String main(@RequestParam("board_info_idx") int board_info_idx,
			@RequestParam(value = "page", defaultValue = "1") int page, Model model) {

//board main jsp 에서 글쓰기 버튼 클릭시 사용
		model.addAttribute("board_info_idx", board_info_idx);

//각 게시판 좌측 상단에  표시할 게시판 이름 지정
		String boardInfoName = boardService.getBoardInfoName(board_info_idx);
		model.addAttribute("boardInfoName", boardInfoName);

//각 게시판 리스트 모두 가져오기
//해당 게시판의 페이지 네이게이션 번호 클릭시  페이지 번호에 해당하는 리스트 가져옴.
		List<ContentBean> contentList = boardService.getContentList(board_info_idx, page);
		model.addAttribute("contentList", contentList);

//페이징 처리를 위해 board_info_idx 와 page 번호 전달
		PageBean pageBean = boardService.getContentCnt(board_info_idx, page);
		model.addAttribute("pageBean", pageBean);

//board/main jsp 에 제목 부분 링크 뒤에 page 를 넘김(6페이지 에 있는 글중 하나를 클릭해서 글 내용을 보고 목록보기 클릭하면  6페이지로 유지하기위해서)
		model.addAttribute("page", page);

		return "board/main";
	}

//Board main 에서 글 제목 클릭할때 board_info_idx 와 content_idx 을 넘김.
//page board main jsp 에서 넘어옴
	@GetMapping("/read")
	public String read(@RequestParam("board_info_idx") int board_info_idx, @RequestParam("content_idx") int content_idx,
			@RequestParam("page") int page,

			Model model) {

//글 읽기 페이지 목록보기 버튼에 사용합니다.
		model.addAttribute("board_info_idx", board_info_idx);
//글 읽기 페이지에 있는  수정 삭제 버튼 처리에 사용합니다.
		model.addAttribute("content_idx", content_idx);

//해당 게시글 읽기 화면에 내용 가져오기 ( read 페이지에서  로그인 사용자 idx 비교위해서 content_writer_idx를 추가적으로 담아온다.))
		ContentBean readContentBean = boardService.getContentInfo(content_idx);
		model.addAttribute("readContentBean", readContentBean);

//read 페이지에서 로그인 사용자 idx 와 글쓴이 idx 비교해서 일치 하지 않으면 글 read페이지에 수정, 삭제 버튼 노출시키지 않는다.
		model.addAttribute("loginUserBean", loginUserBean);

//board main 에서 처음 클릭했던 page 번호를 유지하기 위해서  처리
		model.addAttribute("page", page);

		return "board/read";
	}

	@GetMapping("/write")
	public String write(@ModelAttribute("writeContentBean") ContentBean writeContentBean,
			@RequestParam("board_info_idx") int board_info_idx) {

		writeContentBean.setContent_board_idx(board_info_idx);

		return "board/write";
	}

	@PostMapping("/write_pro")
	public String write_pro(@Valid @ModelAttribute("writeContentBean") ContentBean writeContentBean,
			BindingResult result) {
		if (result.hasErrors()) {
			return "board/write";
		}

		boardService.addContentInfo(writeContentBean);

		return "board/write_success";
	}

	// board read.jsp 넘어온 page
	@GetMapping("/modify")
	public String modify(@RequestParam("board_info_idx") int board_info_idx,
			@RequestParam("content_idx") int content_idx,
			@ModelAttribute("modifyContentBean") ContentBean modifyContentBean, @RequestParam("page") int page,
			Model model) {

		// 수정 페이지에 취소 버튼 클릭시 되돌아 가는데 필요.
		model.addAttribute("board_info_idx", board_info_idx);
		model.addAttribute("content_idx", content_idx);

		// 처음 클릭한 page 유지 하기 위한 처리과정
		model.addAttribute("page", page);

		// 수정 페이지 내용 DB서 가져오기
		ContentBean tempContentBean = boardService.getContentInfo(content_idx);

		modifyContentBean.setContent_writer_name(tempContentBean.getContent_writer_name());
		modifyContentBean.setContent_date(tempContentBean.getContent_date());
		modifyContentBean.setContent_subject(tempContentBean.getContent_subject());
		modifyContentBean.setContent_text(tempContentBean.getContent_text());
		modifyContentBean.setContent_file(tempContentBean.getContent_file());
		modifyContentBean.setContent_writer_idx(tempContentBean.getContent_writer_idx());
		modifyContentBean.setContent_board_idx(board_info_idx);
		modifyContentBean.setContent_idx(content_idx);

		return "board/modify";
	}

	// 수정페이지에 수정완료 버튼 클릭시...
	// modify.jsp 에서 hidden 으로 page 넘김
	@PostMapping("/modify_pro")
	public String modify_pro(@Valid @ModelAttribute("modifyContentBean") ContentBean modifyContentBean,
			BindingResult result, @RequestParam("page") int page, Model model) {

		model.addAttribute("page", page);

		if (result.hasErrors()) {
			return "board/modify";
		}

		// 수정 페이지에서 DB에 수정처리.
		boardService.modifyContentInfo(modifyContentBean);

		return "board/modify_success";
	}

	@GetMapping("/delete")
	public String delete(@RequestParam("board_info_idx") int board_info_idx,
			@RequestParam("content_idx") int content_idx, Model model) {

		// 글 읽기 페이지에 삭제버튼처리
		boardService.deleteContentInfo(content_idx);

		// 글 삭제 메시지 보여준 후 글 목록 페이지 가기 위해 필요
		model.addAttribute("board_info_idx", board_info_idx);

		return "board/delete";
	}

	// CheckWriterInterceptor 적용시 해당 jsp로 이동됨.
	@GetMapping("/not_writer")
	public String not_writer() {
		return "board/not_writer";
	}
}
