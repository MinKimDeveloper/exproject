package kr.co.springbasic.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.springbasic.beans.ContentBean;
import kr.co.springbasic.beans.UserBean;
import kr.co.springbasic.service.BoardService;

//주소창에 직접 치고 들어갈때 로그인 사용자 idx 와 글쓴이 idx 가 다른경우 수정/삭제 로 접근 할수 없도록 인터셉터 적용
public class CheckWriterInterceptor implements HandlerInterceptor{
	
	//자바는 인터셉터에서 주입이 불가능
	
	//로그인 사용자 정보
	private UserBean loginUserBean;
	
	//글쓴이 content_writer_idx 가져오기 위해
	private BoardService boardService;
	
	public CheckWriterInterceptor(UserBean loginUserBean, BoardService boardService) {
		// TODO Auto-generated constructor stub
		this.loginUserBean = loginUserBean;
		this.boardService = boardService;
	}
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		//read.jsp 에서 content_idx값 넘김.
		String str1 = request.getParameter("content_idx");
		int content_idx = Integer.parseInt(str1);
		
		ContentBean currentContentBean = boardService.getContentInfo(content_idx);
		
		if(currentContentBean.getContent_writer_idx() != loginUserBean.getUser_idx()) {
			String contextPath = request.getContextPath();
			response.sendRedirect(contextPath + "/board/not_writer");
			return false;
		}
		
		return true;
	}
}












