package edu.kosmo.ex.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;
import edu.kosmo.ex.dto.BDto;
import edu.kosmo.ex.page.Criteria;
import edu.kosmo.ex.page.PageVO;

public class BSearchCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String dateSearch = request.getParameter("dateSearch");
		String textSearch = request.getParameter("textSearch");
		String searchWord = request.getParameter("searchWord");
		
		request.setAttribute("dateSearch", dateSearch);
		request.setAttribute("textSearch", textSearch);
		
		String searchPageNum = request.getParameter("pageNum"); 
		String searchAmount = request.getParameter("amount");
		
		Criteria cri;
		
		if(searchPageNum == null) { // list.do로 접속할 경우
			// 현재 페이지에 해당하는 번호는 링크가 눌리지 않도록 하기 위해 현재 페이지 번호 값 저장.
			request.setAttribute("currentPageNum", 1);
			cri = new Criteria();
		}	
		else {
			request.setAttribute("currentPageNum", Integer.valueOf(searchPageNum));
			cri = new Criteria(Integer.valueOf(searchPageNum), Integer.valueOf(searchAmount));
		}		
		
		BDao dao = new BDao();
			
		ArrayList<BDto> dtos = dao.searchResultPaging(dateSearch, textSearch, searchWord, cri);
		
		request.setAttribute("searchResult", dtos);		
		request.setAttribute("searchWord", searchWord); // 검색어를 화면에 출력하기 위한 설정
		
		int searchTotal = dao.getTotalSearchCount(dateSearch, textSearch, searchWord);
		
		PageVO pageVO = new PageVO(cri, searchTotal);
		
		request.setAttribute("searchPageMaker", pageVO);		
		request.setAttribute("dao", dao);	
		// searchResult.jsp에서 dao.getBadgeNew(int bid) 함수를 사용하기 위해서 dao 객체를 저장
	}
	
}
