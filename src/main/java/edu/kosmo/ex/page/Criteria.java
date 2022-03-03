package edu.kosmo.ex.page;

public class Criteria {
	
	// 페이징 처리를 위해 페이지번호와 한페이지당 몇개의 데이터를 보여줄것인지 결정.	
	private int pageNum; // 페이지 번호
	private int amount; // 한페이지당 몇개의 데이터를 보여줄것인가?
	
	public Criteria() {
		this(1,10); // 기본값 페이지 10개로 지정(디폴트로)
		// 한 페이지 당 10개의 게시물을 보여 주겠다.
	}
	
	public Criteria(int pageNum,int amount) {
		this.pageNum = pageNum;
		this.amount = amount;
	}

	public int getPageNum() {
		return pageNum;
	}

	public void setPageNum(int pageNum) {
		this.pageNum = pageNum;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
