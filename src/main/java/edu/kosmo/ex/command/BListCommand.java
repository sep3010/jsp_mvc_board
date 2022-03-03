package edu.kosmo.ex.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;
import edu.kosmo.ex.dto.BDto;
import edu.kosmo.ex.page.Criteria;
import edu.kosmo.ex.page.PageVO;

public class BListCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		BDao dao = new BDao();
		Criteria cri;
		
		String pageNum = request.getParameter("pageNum"); 
		String amount = request.getParameter("amount");
		
		
		if(pageNum == null) { // list.do로 접속할 경우
			request.setAttribute("currentPageNum", 1);
			// 현재 페이지에 해당하는 번호는 링크가 눌리지 않도록 하기 위해 현재 페이지 번호 값 저장.
			cri = new Criteria();
		}	
		else {
			request.setAttribute("currentPageNum", Integer.valueOf(pageNum));
			// 현재 페이지에 해당하는 번호는 링크가 눌리지 않도록 하기 위해 현재 페이지 번호 값 저장.
			cri = new Criteria(Integer.valueOf(pageNum), Integer.valueOf(amount));
		}	
		
		ArrayList<BDto> dtos = dao.getListWithPaging(cri);
		
		request.setAttribute("list", dtos);	
		
		int total = dao.getTotalCount();
		
		PageVO pageVO = new PageVO(cri, total);
			
		request.setAttribute("pageMaker", pageVO);	
		request.setAttribute("dao", dao);	
		// list.jsp에서 dao.getBadgeNew(int bid) 함수를 사용하기 위해서 dao 객체를 저장
	}
	
}
