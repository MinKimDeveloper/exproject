package kr.co.springbasic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Select;

import kr.co.springbasic.beans.BoardInfoBean;



public interface TopMenuMapper {
	
	@Select("select board_info_idx, board_info_name " +
			"from board_info_table " + 
			"order by board_info_idx")
	List<BoardInfoBean> getTopMenuList();
}
