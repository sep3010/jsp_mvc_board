package edu.kosmo.ex.command;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.kosmo.ex.dao.BDao;

public class BWriteCommand implements BCommand {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) {
		
		String bname = request.getParameter("bname");
		String btitle = request.getParameter("btitle");
		String bcontent = request.getParameter("bcontent");
		// 게시글을 작서앟고 제출을 눌렀을 때 input으로 전달된 작성자명, 제목, 내용을 받아와
		// DB에 저장한다! DB에 값을 저장하는 것이기 때문에 BDto는 사용하지 않는다.(DB에서 값을 가져올 때 사용)
		BDao dao = new BDao();
		dao.write(bname, btitle, bcontent);
	}
	
}
