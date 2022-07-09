package kr.co.springbasic.service;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.co.springbasic.beans.ContentBean;
import kr.co.springbasic.dao.BoardDao;

@Service
public class MainService {

	// 각 게시판 리스트 가져오는 getContentList 메서드 호출
	@Autowired
	private BoardDao boardDao;
	
	public List<ContentBean> getMainList(int board_info_idx){
		RowBounds rowBounds = new RowBounds(0, 5);
		return boardDao.getContentList(board_info_idx, rowBounds);
	}
}











