package kr.co.springbasic.dao;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import kr.co.springbasic.beans.ContentBean;
import kr.co.springbasic.mapper.BoardMapper;

@Repository
public class BoardDao {
	
	@Autowired
	private BoardMapper boardMapper;
	
	public void addContentInfo(ContentBean writeContentBean) {
		boardMapper.addContentInfo(writeContentBean);
	}
	
	//각 게시판 이름 가져오기
	public String getBoardInfoName(int board_info_idx) {
		return boardMapper.getBoardInfoName(board_info_idx);
	}
	

	//각 게시판 리스트 전부 가져오기
	//RowBounds는 1페이지는 1~10번까지글 ... 2페이지는 11~ 20번 글  가져온다 (페이지당 10개 글씩 셋팅 기능)
	public List<ContentBean> getContentList(int board_info_idx, RowBounds rowBounds){
		return boardMapper.getContentList(board_info_idx, rowBounds);
	}
	
	//해당 게시글 읽기 화면에 내용 가져오기
	public ContentBean getContentInfo(int content_idx) {
		return boardMapper.getContentInfo(content_idx);
	}
	
	//수정페이지 수정한 내용 DB에 저장.
	public void modifyContentInfo(ContentBean modifyContentBean) {
		boardMapper.modifyContentInfo(modifyContentBean);
	}
	
	// 글 읽기 페이지에 있는 삭제버튼 처리
	public void deleteContentInfo(int content_idx) {
		boardMapper.deleteContentInfo(content_idx);
	}
	
	//해당 게시판 전체 글 개수 가져옴.
	public int getContentCnt(int content_board_idx) {
		return boardMapper.getContentCnt(content_board_idx);
	}
}










