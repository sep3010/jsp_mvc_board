package edu.kosmo.ex.page;

public class PageVO {
	
		private int startPage; // 화면에 보여지는 페이지 시작번호
		private int endPage; // 화면에 보여지는 페이지 끝번호
		private int realEnd; // 실제 총 페이지 수 
		
		private boolean pre, next; 
		// 이전과 다음으로 이동가능한 '<<' 과 '>>' 출력 여부를 결정하기 위한 변수
		// 페이지 하단의 페이징을 10개 단위로 이동 시켜주는 링크(버튼)
		
		private int total; // 전체 게시판 글수. BDao의 getTotalCount() 함수를 이용해 가져올 값.
		private Criteria cri;

		// 생성자
		public PageVO(Criteria cri, int total) {
			this.cri = cri;
			this.total = total;

			// endPage는 현재의 페이지 번호를 기준으로 계산한다.		
			this.endPage = (int) (Math.ceil(cri.getPageNum() / 10.0)) * 10;
			// 현재페이지 정보로 부터 끝페이지에 대한 연산 Math.ceil()은 올림함수.
			// 현재 페이지값은 cri.getPageNum()로 가져온다.
			// 끝페이지는 현재페이지 번호를 10.0으로 나눈 값을 올림한 후, 
			// 다시 10(하단 페이징 바에 보여줄 페이지 개수)을 곱해준다.
			// 위 계산식을 통해 페이지 1~10번까지는 하단 페이징 바에 1~10까지 보이고,
			// 페이지 11~20번까지는 하단 페이징 바에서 11~20까지 보인다.
			// 나머지 페이지들도 동일한 방식으로 보인다.
			
			this.startPage = this.endPage - 9;
			// 하단 페이징 바에 보여지는 시작 페이지 번호.
					
			// 페이징 바에 보이는 10개씩 끊어지는 방식의 페이지 번호가 아닌
			// 실제 페이지 개수를 구하는 연산
			realEnd = (int) (Math.ceil((total * 1.0) / cri.getAmount()));
			// cri.getAmount() = 10
			// 전체 총 페이지수를 한 페이지당 띄울 페이지 개수로 나눈뒤 올림을 해주면 실제 페이지 개수를 구할 수 있다.

			if (realEnd <= this.endPage) {
				this.endPage = realEnd;
			} // 만약 실제 페이지 수가 endPage 연산 값보다 작다면 endPage값에 realEnd값을 넣어줘야 
				// 하단의 페이징 바에서 실제 페이지 수까지만 나타낼 수 있다.
						
			// 페이징 바가 1~10페이지를 출력할 때는 좌측의 '<<'버튼 생략
			this.pre = this.startPage > 1;
									
			// realEnd가 끝번호(endPage)보다 큰 경우에만 존재
			// ====> 총 100페이지, 출력하는 페이지가 90~100일때 우측의 '>>'버튼 생략
			this.next = this.endPage < realEnd;
		}
		
		// ?pageNum=1&amount=10를 만들어 주기 위한 함수! (게시글 목록 페이징을 위한 주소창 처리)
		public String makeQuery(int page) { 			
			return "?pageNum=" + page + "&amount=" + cri.getAmount();
		}
		
		// 검색 결과 페이징을 위한 주소창 처리
		// "searchResult.do${searchPageMaker.makeQuery(currentPageNum - 1)}&dateSearch=${dateSearch}&textSearch=${textSearch}&searchWord=${searchWord}"
		public String makeSearchQuery(int page, String dateSearch, String textSearch, String searchWord) { 			
			return "?pageNum=" + page + "&amount=" + cri.getAmount() + "&dateSearch=" + dateSearch + "&textSearch=" + textSearch + "&searchWord=" + searchWord;
		}
		
		public int getStartPage() {
			return startPage;
		}

		public void setStartPage(int startPage) {
			this.startPage = startPage;
		}

		public int getEndPage() {
			return endPage;
		}

		public void setEndPage(int endPage) {
			this.endPage = endPage;
		}

		public boolean isPre() {
			return pre;
		}

		public void setPre(boolean pre) {
			this.pre = pre;
		}

		public boolean isNext() {
			return next;
		}

		public void setNext(boolean next) {
			this.next = next;
		}

		public int getRealEnd() {
			return realEnd;
		}

		public void setRealEnd(int realEnd) {
			this.realEnd = realEnd;
		}
				
}
