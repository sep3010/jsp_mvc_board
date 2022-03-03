package edu.kosmo.ex.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;
import edu.kosmo.ex.dto.BDto;

public class BContentNextCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// content_next.do?bid=${content_view.bid}
		String bid = request.getParameter("bid");
		
		BDao dao = new BDao();
		BDto dto = dao.contentNext(bid);
		
		request.setAttribute("content_view", dto);
		// 포워딩 될때까지 유지...	
		
		// bid에 해당하는 글에 답글이 달려있으면(bgroup 값이 같은 글이 있으면) 값을 받아온다. 
		ArrayList<BDto> dtos = dao.replyList(dto.getBid());
		request.setAttribute("replyList", dtos);
		
		// 다음글 버튼을 보이게 할지 말지를 위한 값
		BDto dto2 = dao.exsistNext(dto.getBid());
		request.setAttribute("exsistNext", dto2);
		
		// 이전글 버튼을 보이게 할지 말지를 위한 값
		BDto dto3 = dao.exsistPrevious(dto.getBid());
		request.setAttribute("exsistPrevious", dto3);
	}
	
}
