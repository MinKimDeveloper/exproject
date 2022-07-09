package kr.co.springbasic.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.session.RowBounds;

import kr.co.springbasic.beans.ContentBean;



public interface BoardMapper {
	
    //게시판 새로운 글을 등록하기 위해 +1 시퀀스 증가 시켜준 후 쿼리를 작동시켜야 한다.
	@SelectKey(statement = "select content_seq.nextval from dual", keyProperty = "content_idx", before = true, resultType = int.class)
	
	@Insert("insert into content_table(content_idx, content_subject, content_text, " +
			"content_file, content_writer_idx, content_board_idx, content_date) " +
			"values (#{content_idx}, #{content_subject}, #{content_text}, #{content_file, jdbcType=VARCHAR}, " +
			"#{content_writer_idx}, #{content_board_idx}, sysdate)")
	void addContentInfo(ContentBean writeContentBean);
	
	//각 게시판 이름 가져오기 
	@Select("select board_info_name " + 
			"from board_info_table " + 
			"where board_info_idx = #{board_info_idx}")
	String getBoardInfoName(int board_info_idx);
	
	//각 게시판 목록 리스트 전부 가져오기
	@Select("select a1.content_idx, a1.content_subject, a2.user_name as content_writer_name, " + 
			"       to_char(a1.content_date, 'YYYY-MM-DD') as content_date " + 
			"from content_table a1, user_table a2 " + 
			"where a1.content_writer_idx = a2.user_idx " + 
			"      and a1.content_board_idx = #{board_info_idx} " + 
			"order by a1.content_idx desc")
	List<ContentBean> getContentList(int board_info_idx, RowBounds rowBounds);
	
	//해당 게시판 글 읽기 페이지 내용 가져오기 
	@Select("select a2.user_name as content_writer_name, " + 
			"       to_char(a1.content_date, 'YYYY-MM-DD') as content_date, " + 
			"       a1.content_subject, a1.content_text, a1.content_file, a1.content_writer_idx " + 
			"from content_table a1, user_table a2 " + 
			"where a1.content_writer_idx = a2.user_idx " + 
			"      and content_idx = #{content_idx}")
	ContentBean getContentInfo(int content_idx);
	
	// 수정페이지 수정완료(업데이트) 
	@Update("update content_table " +
			"set content_subject = #{content_subject}, content_text = #{content_text}, " +
			"content_file = #{content_file, jdbcType=VARCHAR} " +
			"where content_idx = #{content_idx}")
	void modifyContentInfo(ContentBean modifyContentBean);
	
	// 글 읽기 페이지에 있는 삭제 버튼
	@Delete("delete from content_table where content_idx = #{content_idx}")
	void deleteContentInfo(int content_idx);
	
	//각 게시판의 전체 글 개수(PageBean  에서 contentCnt)구하기
	@Select("select count(*) from content_table where content_board_idx = #{content_board_idx}")
	int getContentCnt(int content_board_idx);
} 


















