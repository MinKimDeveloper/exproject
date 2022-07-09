package kr.co.springbasic.beans;

public class PageBean {

	//최소 페이지 번호
	private int min;
	//최대 페이지 번호
	private int max;
	//이전 버튼의 페이지 번호
	private int prevPage;
	//다음 버튼의 페이지 번호
	private int nextPage;
	//전체 페이지 개수
	private int pageCnt;
	//현재 페이지 번호
	private int currentPage;
	
	// contentCnt : 전체글 개수, currentPage : 현재 페이지 번호, contentPageCnt : 페이지당 글의 개수, paginationCnt: 페이지 버튼의 개수
	public PageBean(int contentCnt, int currentPage, int contentPageCnt, int paginationCnt) {
		
		//현재 페이지 번호
		this.currentPage = currentPage;
		
		//전체 페이지 개수	 ex) 533 / 10 =  53  3개의 글이 남기때문에 페이지 하나 더 추가해야합니다.
		pageCnt = contentCnt / contentPageCnt;
		if(contentCnt % contentPageCnt > 0) {
			pageCnt++;
		}
		
		
		min  = ((currentPage - 1) / contentPageCnt) * contentPageCnt + 1;
		
		max = min + paginationCnt - 1;
		
		// ex) 페이지 버튼이 51~60 인데 pageCnt가  55 이면... 전체페이지 개수를 max로.
		if(max > pageCnt) {
			max = pageCnt;
		}
		
		prevPage = min - 1;
		nextPage = max + 1;
		// 전체 페이지 개수를 nextPage 로 설정하면 마지막 페이지에서 다음 버튼은 작동 되지 않는다.
		if(nextPage > pageCnt) {
			nextPage = pageCnt;
		}
	}
	
	public int getMin() {
		return min;
	}
	public int getMax() {
		return max;
	}
	public int getPrevPage() {
		return prevPage;
	}
	public int getNextPage() {
		return nextPage;
	}
	public int getPageCnt() {
		return pageCnt;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	
	
}
