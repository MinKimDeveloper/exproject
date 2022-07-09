package kr.co.springbasic.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import kr.co.springbasic.beans.ContentBean;
import kr.co.springbasic.beans.PageBean;
import kr.co.springbasic.beans.UserBean;
import kr.co.springbasic.dao.BoardDao;

@Service
@PropertySource("/WEB-INF/properties/option.properties")
public class BoardService {
	
	@Value("${path.upload}")
	private String path_upload;
	
	// 게시판 1 페이지당 10개 글내용 셋팅
	@Value("${page.listcnt}")
	private int page_listcnt;
	
	// 10 으로 셋팅되어있음
	@Value("${page.paginationcnt}")
	private int page_paginationcnt;
	
	@Autowired
	private BoardDao boardDao;
	
	@Resource(name = "loginUserBean")
	private UserBean loginUserBean;
	
	private String saveUploadFile(MultipartFile upload_file) {
		
		String file_name = System.currentTimeMillis() + "_" + upload_file.getOriginalFilename();
		
		try {
			upload_file.transferTo(new File(path_upload + "/" + file_name));
		}catch(Exception e) {
			e.printStackTrace();
		}
		
		return file_name;
	}
	
	public void addContentInfo(ContentBean writeContentBean) {
		
		MultipartFile upload_file = writeContentBean.getUpload_file();
		
		
		if(upload_file.getSize() > 0) {
			String file_name = saveUploadFile(upload_file);
			writeContentBean.setContent_file(file_name);
		}
		
		writeContentBean.setContent_writer_idx(loginUserBean.getUser_idx());
		
		boardDao.addContentInfo(writeContentBean);
	}
	
	////각 게시판 이름 가져오기
	public String getBoardInfoName(int board_info_idx) {
		return boardDao.getBoardInfoName(board_info_idx);
	}
	
	// 각 게시판 리스트 전부 가져오기
	// 해당 페이지의 게시판 리스트 내용 가져옴.
	public List<ContentBean> getContentList(int board_info_idx, int page){
		
		// page 가 1 page 이면 start = 0(index개념), page_listcnt(페이지당 글 개수)= 10
		// 1페이지(1번 글부터 10개 가져옴)
		// page 가 2 면 start가 인덱스10 -> 2페이지(11번 글 부터 10개 셋팅)
		
		int start = (page - 1) * page_listcnt;
		RowBounds rowBounds = new RowBounds(start, page_listcnt);
		
		return boardDao.getContentList(board_info_idx, rowBounds);
	}
	
	// 해당 게시글 읽기페이지 내용 가져오기
	public ContentBean getContentInfo(int content_idx) {
		return boardDao.getContentInfo(content_idx);
	}
	
	// 수정페이지 수정한 내용 DB에 저장하는 과정(첨부된 파일 있으면 가져와서 처리후 Dao로 넘김)
	public void modifyContentInfo(ContentBean modifyContentBean) {
		
		MultipartFile upload_file = modifyContentBean.getUpload_file();
		
		if(upload_file.getSize() > 0) {
			String file_name = saveUploadFile(upload_file);
			modifyContentBean.setContent_file(file_name);
		}
		
		boardDao.modifyContentInfo(modifyContentBean);
	}
	
	// 글 읽기 페이지에 삭제버튼처리
	public void deleteContentInfo(int content_idx) {
		boardDao.deleteContentInfo(content_idx);
	}
	
	// 해당 게시판 전체 글개수 가져 오기 위해 content_board_idx 받았고 currentPage 받은걸 PageBean 로 넘김.
	public PageBean getContentCnt(int content_board_idx, int currentPage) {
		
		// 게시판 전체 글 개수
		int content_cnt = boardDao.getContentCnt(content_board_idx);
		
		// PageBean 생성자 만들어서 해당 값들 넘겨준다.
		PageBean pageBean = new PageBean(content_cnt, currentPage, page_listcnt, page_paginationcnt);
		
		return pageBean;
	}
}








