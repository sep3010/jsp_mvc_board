package edu.kosmo.ex.command;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;
import edu.kosmo.ex.dto.BDto;

public class BContentCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		// content_view.do?bid=${dto.bid}
		String bid = request.getParameter("bid");
		
		BDao dao = new BDao();
		BDto dto = dao.contentView(bid);
		
		request.setAttribute("content_view", dto);
		
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
