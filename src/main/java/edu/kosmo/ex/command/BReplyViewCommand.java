package edu.kosmo.ex.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;
import edu.kosmo.ex.dto.BDto;

public class BReplyViewCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		String bid = request.getParameter("bid");
		BDao dao = new BDao();
		BDto dto = dao.reply_view(bid);
		
		request.setAttribute("reply_view", dto); 
		// 답글을 달 원본글의 값을 key - "reply_view"로 저장.
	}
	
}
