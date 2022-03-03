package edu.kosmo.ex.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;
import edu.kosmo.ex.dto.BDto;

public class BContentPreviousCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// content_Previous.do?bid=${content_view.bid}
		String bid = request.getParameter("bid");
		
		BDao dao = new BDao();
		BDto dto = dao.contentPrevious(bid);
		
		request.setAttribute("content_view", dto);
		// 포워딩 될때까지 유지...	
		
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
