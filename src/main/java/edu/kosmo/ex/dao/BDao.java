package edu.kosmo.ex.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
/*
java.sql 패키지는 일반 데이터베이스 작업을 위해
javax.sql 패키지는 DataSource 객체를 사용하기 위해 
javax.naming 패키지는 JNDI 작업을 하기 위해 import
- JNDI는 서버에서 관리하고 있는 리소스에 대한 정보를 알고 있고 
  특정 리소스를 찾아서 사용할 수있도록 객체를 반환해주는 역활
  (커넥션 풀 접근을 위해 필요)
 */

import edu.kosmo.ex.dto.BDto;
import edu.kosmo.ex.page.Criteria;

/* 
이름       널?       유형            
-------- -------- ------------- 
BID      NOT NULL NUMBER(4)     
BNAME             VARCHAR2(20)  
BTITLE            VARCHAR2(100) 
BCONTENT          VARCHAR2(300) 
BDATE             DATE          
BHIT              NUMBER(4)     
BGROUP            NUMBER(4)     
BSTEP             NUMBER(4)     
BINDENT           NUMBER(4) 
*/

public class BDao {
	private DataSource dataSource;
	// 커넥션 풀의 connection을 관리하기 위한 객체
	// 해당 객체를 통해서 필요한 Connection을 획득, 반납 등의 작업을 한다.

	public BDao() {
		try { 
			Context context = new InitialContext();
			dataSource = (DataSource) context.lookup("java:comp/env/jdbc/oracle");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	// 전체 게시글 수를 알아내기 위한 함수
	public int getTotalCount() {
		System.out.println("getTotalCount()..");
		int totalPageCount = 0;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String query = "SELECT count(*) AS count FROM mvc_board";

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				totalPageCount = rs.getInt("count");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		return totalPageCount;
	};

	// 각 페이지 번호에 맞는 게시글들을 정해진 수(cri.getAmount()) 만큼 불러오는 함수.
	public ArrayList<BDto> getListWithPaging(Criteria cri) {
		System.out.println("list()..");

		ArrayList<BDto> dtos = new ArrayList<BDto>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String query = "SELECT * FROM ( " + "SELECT ROWNUM AS rnum, A.* FROM ( "
					+ "SELECT * FROM mvc_board ORDER BY bgroup DESC, bstep ASC" 
					+ " ) A WHERE ROWNUM <= ? * ?"
					+ " ) WHERE rnum > ( ? - 1 ) * ?";

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, cri.getPageNum());
			preparedStatement.setInt(2, cri.getAmount()); // cri.getAmount() = 10
			preparedStatement.setInt(3, cri.getPageNum());
			preparedStatement.setInt(4, cri.getAmount());

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int bid = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				BDto dto = new BDto(bid, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				dtos.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dtos;
	};

	// 조회 수 증가를 위한 함수
	private void upHit(String bid) { // 해당 BDao안에서만 사용할 것이기 때문에 private!
		System.out.println("upHit()..");

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			String query = "UPDATE mvc_board SET bhit = bhit + 1 WHERE bid = ?";
			// 클릭한 번호에 해당하는 게시글의 조회수를 1 증가 시키는 쿼리문
			// 여기서는 실행시킬 쿼리문이 Select가 아니기 때문에 ResultSet 객체는 필요가 없다!
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bid));

			int rn = preparedStatement.executeUpdate();
			// executeUpdate()가 반환하는 숫자값은 INSERT / DELETE / UPDATE문 실행 후 영향받은 레코드의 개수
			// (SELECT문을 제외한 SQL문 실행시 사용)
			// CREATE / DROP문에서는 -1 을 반환
			System.out.println("조회수: " + rn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}
	}
	
	// 게시글 조회 (클릭한 게시글 한 개의 내용을 불러와 보기 위한 함수)
	public BDto contentView(String bid) { // bid는 클릭한 게시글의 bid 값
		System.out.println("contentView()..");
		upHit(bid); // 조회수 추가해주는 함수!

		BDto dto = null;
		String query = "SELECT * FROM mvc_board WHERE bid = ?";
		// ?에 해당하는 번호의 게시글을 불러오기 위한 쿼리문
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bid));
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dto;
	}

	// 게시글 작성
	public void write(String bname, String btitle, String bcontent) {
		System.out.println("write()..");

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			String query = "INSERT INTO mvc_board (bid, bname, btitle, bcontent, bhit, bgroup, bstep, bindent)"
					+ "VALUES (mvc_board_seq.nextval, ?, ?, ?, 0, mvc_board_seq.currval, 0, 0 )";
			// .nextval은 값을 1씩 증가 시킨다. 
			// .currval은 현재 값을 조회. INSERT, SELECT등 쿼리문에 사용 시 NEXTVAL과 함께 사용해야한다.
			// mvc_board_seq.currval 값은 mvc_board_seq.nextval한 값과 동일하다!
			// 즉,bid와 bgroup 값은 동일하다.
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, bname);
			preparedStatement.setString(2, btitle);
			preparedStatement.setString(3, bcontent);

			int rn = preparedStatement.executeUpdate();
			System.out.println("업데이트 수: " + rn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// 답글을 달 원본 글에 저장된 값을 불러와 reply_view.jsp에 띄운다!
	public BDto reply_view(String bid) { // 원본글 bid값을 받아온다.
		System.out.println("reply_view()..");
		BDto dto = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String query = "SELECT * FROM mvc_board WHERE bid = ?";

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setInt(1, Integer.valueOf(bid));
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dto;
	}

	// 답글 작성
	// content_view에서 답글 버튼을 누름면 reply_view.do로 값이 전달되면서 reply_view()가 실행,
	// reply_view.jsp 페이지를 띄운다.
	// reply_view()에는 답글을 달려던 원본 글의 값들이 전달되서 저장되어 있다.
	// 여기서 답글을 작성하고 submit 버튼을 누르면 그 정보들이 reply()로 전달되면서 진짜 답글 작성이 완료!
	public void reply(String bid, String bname, String btitle, String bcontent, String bgroup, String bstep,
			String bindent) {
		// bid, bstep, bindent는 원본글의 값. bname, btitle, bcontent는 작성한 값.
		System.out.println("reply()..");

		replyShape(bgroup, bstep);
		// 새로 작성하는 답글이 답글들 중에 가장 위에 정렬될 수 있도록 기존 답글들을 한줄씩 아래로 내려준다.

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			String query = "INSERT INTO mvc_board (bid, bname, btitle, bcontent, bgroup, bstep, bindent)"
					+ "VALUES (mvc_board_seq.nextval, ?, ?, ?, ?, ?, ?)";

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setString(1, bname);
			preparedStatement.setString(2, btitle);
			preparedStatement.setString(3, bcontent);
			preparedStatement.setInt(4, Integer.parseInt(bgroup)); 
			preparedStatement.setInt(5, Integer.parseInt(bstep) + 1); 
			// 여기에 들어가는 bstep값은 답글을 다는 해당 굴의 bstep값이 전달된다.
			// 즉. 새로 작성되는 답글의 경우 항상 답굴을 단 글의 bstep값에서 +1을 한 값이 bstep값이 되기 때문에
			// list.do 페이지에서 기존 답글을 밀어내고 위쪽으로 정렬된다. (replyShape로 기존 답글을 아래로 밀고)
			preparedStatement.setInt(6, Integer.parseInt(bindent) + 1); 
			int rn = preparedStatement.executeUpdate();
			System.out.println("답글 업데이트 갯수:" + rn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// 새로운 답글이 작성되면 해당 함수로 기존 답글들을 아래로 한줄씩 내려준다. (bstep + 1)
	private void replyShape(String group, String step) {
		System.out.println("replyShape()..");

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			String query = "UPDATE mvc_board SET bstep = bstep + 1 WHERE bgroup = ? AND bstep > ?";
			// 새로 답글을 작성하면 해당 답글의 bstep값을 기준으로 해당 글보다 아래에 위치해야하는 모든 답글에 bstep에 +1을 해준다.

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);

			preparedStatement.setInt(1, Integer.parseInt(group));
			// 여기서 bgroup은 답글이기때문에 원본글과 동일하다.
			preparedStatement.setInt(2, Integer.parseInt(step));
			// ?에 들어가는 bstep값은 답글을 다는 글의 bstep과 동일하다.

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	// 글 삭제 함수
	public void delete(String bid) {
		System.out.println("delete()..");

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = dataSource.getConnection();
			// bid값에 해당하는 행의 값들을 삭제한다.
			String query = "DELETE FROM mvc_board WHERE bid = ?";
			// DELETE에는 * 쓰지 않으니 주의!
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bid));
			// 삭제 버튼을 누른 게시글의 bid 값이 전달된다.

			int rn = preparedStatement.executeUpdate();
			System.out.println("삭제 갯수: " + rn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

	}

	// 글 수정
	public void modify(String bid, String bname, String btitle, String bcontent) {
		System.out.println("modify()..");

		Connection connection = null;
		PreparedStatement preparedStatement = null;

		try {
			String query = "UPDATE mvc_board SET bname = ?, btitle = ?, bcontent = ? WHERE bid = ?";
			// bid값 ?에 해당하는 행의 데이터들을 수정한 내용으로 새로 저장하기 위한 쿼리문

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, bname);
			preparedStatement.setString(2, btitle);
			preparedStatement.setString(3, bcontent);
			preparedStatement.setInt(4, Integer.parseInt(bid));

			int rn = preparedStatement.executeUpdate();
			System.out.println("수정한 횟수: " + rn);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/* 여기서 부턴 추가로 만들어 본 기능들 */

	// 다음글 조회
	public BDto contentNext(String bid) {
		System.out.println("contentNext()..");
		upHit(bid); // 조회수 추가해주는 함수!

		BDto dto = null;
		String query = "SELECT * FROM mvc_board WHERE bstep = 0 AND bid > ? ORDER BY bgroup ASC";
		// ?에 해당하는 게시글의 다음글들을 조회 할 수 있다.
		// (다만, bstep = 0 이라는 조건을 걸었기 때문에 답글들은 제외되어 원본글로만 다음 글로 이동 가능하다.)

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bid));

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				break; // 이걸로 해당 글 다음 글 하나의 값만 저장하도록 해준다.
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dto;
	}

	// 다음글이 없으면 다음글 버튼이 생성되지 않도록하기 위해서 만든 함수(다음글이 있으면 값 저장, 다음글이 없으면 null)
	// 추가로 만든 이유는 위에 함수와 같지만 upHit()가 빠져있다.
	// 처음에는 위의 함수를 호출해서 사용하려고 했으나 upHit()가 중복 적용되어 조회수가 1씩오르지 않고 추가로 더 올라가 아래처럼 수정했다.
	public BDto exsistNext(int bid) {
		System.out.println("exsistNext()..");

		BDto dto = null;
		String query = "SELECT * FROM mvc_board WHERE bstep = 0 AND bid > ? ORDER BY bgroup ASC";

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, bid);

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				break; // 이걸로 해당 글 다음 글 하나의 값만 저장하도록 해준다.
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dto;
	}

	// 이전글 조회해서 값 저장.
	public BDto contentPrevious(String bid) {
		System.out.println("contentPrevious()..");
		upHit(bid); // 조회수 추가해주는 함수!

		BDto dto = null;
		String query = "SELECT * FROM mvc_board WHERE bstep = 0 AND bid < ? ORDER BY bgroup DESC";
		// ?에 해당하는 게시글의 이전글들을 정렬해서 뽑아내는 쿼리문 (답글 제외)

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, Integer.parseInt(bid));

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				break; // 이걸로 해당 글 다음 글 하나의 값만 저장하도록 해준다.
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dto;
	}

	// 이전글이 없으면 이전글 버튼이 생성되지 않도록하기 위해서 만든 함수...(이전글이 있으면 값 저장, 이전글이 없으면 null)
	// exsistNext와 같은 이유로 만든 함수
	public BDto exsistPrevious(int bid) {
		System.out.println("exsistPrevious()..");

		BDto dto = null;
		String query = "SELECT * FROM mvc_board WHERE bstep = 0 AND bid < ? ORDER BY bgroup DESC";

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, bid);

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				break; // 이걸로 해당 글 다음 글 하나의 값만 저장하도록 해준다.
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dto;
	}

	// content_view.jsp에서 해당 글에 답글이 있다면(동일한 bgroup) 답글을 목록으로 볼 수 있도록 하기 위해서 만든 함수
	public ArrayList<BDto> replyList(int bid) {
		System.out.println("replyList()");

		ArrayList<BDto> dtos = new ArrayList<BDto>();

		String query = "SELECT * FROM mvc_board WHERE bgroup = (SELECT bgroup FROM mvc_board WHERE bid = ?) "
				+ "AND bid != ? ORDER BY bgroup DESC, bstep ASC";
		// 원본글의 경우 답글목록으로 뽑아낼 필요가 없기 때문에 bid != ? 조건 추가.

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, bid);
			preparedStatement.setInt(2, bid);

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");

				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				BDto dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dtos;
	}

	// 시스템상 오늘 날짜를 기준으로 어제 ~ 오늘 작성된 게시글에 NEW 뱃지를 붙여주기 위한 함수
	public boolean getBadgeNew(int bid) {

		BDto dto = null;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			String query = "SELECT * FROM mvc_board WHERE bid = ? AND bdate > SYSDATE - 1";
			// bid값을 받아 어제(SYSDATE - 1) 이후에 작성된 글을 조회 했을 때 글이 있을 경우에는 dto에 값이 저장된다.
			// 조회 했을 때 글이 없을 경우에는 값이 저장 되지 않아 dto = null이 된다.

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, bid);

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		if (dto != null) {
			System.out.println("getBadgeNew()..");
			// bid 값을 하나하나 받아서 전체 게시글의 모두 순차적으로 확인하는 방식이라 
			// 콘솔창에서 확인용 코드가 너무 길어져서 new 뱃지가 달리는 게시글이 생길 경우에만
			// 콘솔창에 찍히도록 수정했다.
			return true;
		} else
			return false;

	}

	// 게시글 검색해서 해당 게시글들 페이징해서 출력하기 위한 함수
	public ArrayList<BDto> searchResultPaging(String dateSearch, String textSearch, String searchWord, Criteria cri) {
		System.out.println("searchResultPaging..()");

		String searchQuery = "SELECT * FROM mvc_board WHERE ";

		// 날짜 검색 범위 설정을 위한 쿼리문 정하기
		String part1 = "";
		if (dateSearch.equals("one"))
			part1 = "bdate >= SYSDATE - 1 AND ";
		else if (dateSearch.equals("seven"))
			part1 = "bdate >= SYSDATE - 7 AND ";
		else if (dateSearch.equals("month"))
			part1 = "bdate >= SYSDATE - 30 AND ";
		else if (dateSearch.equals("year"))
			part1 = "bdate >= SYSDATE - 365 AND ";
		else
			part1 = "";

		searchQuery += part1;

		// 단어 검색 범위 설정을 위한 쿼리문 정하기
		String part2 = "";
		int countQMark = 1; // 총 완성될 쿼리문의 ? 개수를 표시해 주는 변수
		if (textSearch.equals("title"))
			part2 = "btitle LIKE ?";
		else if (textSearch.equals("content"))
			part2 = "bcontent LIKE ?";
		else if (textSearch.equals("name"))
			part2 = "bname LIKE ?";
		else {
			part2 = "btitle LIKE ? OR bname LIKE ? OR bcontent LIKE ?";
			countQMark = 3; // 총 완성될 쿼리문의 ? 개수를 표시해 주는 변수
		}
		searchQuery += part2;

		// 정렬을 위한 조건문..
		String part3 = " ORDER BY bgroup DESC, bstep ASC";

		searchQuery += part3;
		// 각각 조건에 맞는 퀴리문을 if문으로 결정해 합쳐 하나의 쿼리문으로 완성
		
		// 완성한 쿼리문에 페이징을 위한 쿼리문 적용
		String query = "SELECT * FROM ( " + "SELECT ROWNUM AS rnum, A.* FROM ( "
				+ searchQuery
				+ " ) A WHERE ROWNUM <= ? * ?"
				+ " ) WHERE rnum > ( ? - 1 ) * ?";

		ArrayList<BDto> dtos = new ArrayList<BDto>();

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;

		try {
			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);

			if (countQMark == 3) {
				preparedStatement.setString(1, "%" + searchWord + "%");
				preparedStatement.setString(2, "%" + searchWord + "%");
				preparedStatement.setString(3, "%" + searchWord + "%");
			} else {
				preparedStatement.setString(1, "%" + searchWord + "%");
			}
			preparedStatement.setInt(4, cri.getPageNum());
			preparedStatement.setInt(5, cri.getAmount()); // cri.getAmount() = 10
			preparedStatement.setInt(6, cri.getPageNum());
			preparedStatement.setInt(7, cri.getAmount());

			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("bid");
				String bname = rs.getString("bname");
				String btitle = rs.getString("btitle");
				String bcontent = rs.getString("bcontent");
				Timestamp bdate = rs.getTimestamp("bdate");
				int bhit = rs.getInt("bhit");
				int bgroup = rs.getInt("bgroup");
				int bstep = rs.getInt("bstep");
				int bindent = rs.getInt("bindent");

				BDto dto = new BDto(id, bname, btitle, bcontent, bdate, bhit, bgroup, bstep, bindent);

				dtos.add(dto);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}

		return dtos;
	}
	
	// 검색한 결과의 전체 게시글 수를 알아내기 위한 함수
	public int getTotalSearchCount(String dateSearch, String textSearch, String searchWord) {
		System.out.println("getTotalSearchCount()..");
		int totalSearchCount = 0;

		Connection connection = null;
		PreparedStatement preparedStatement = null;
		ResultSet rs = null;
		
		String searchQuery = "";

		// 날짜 검색 범위 설정을 위한 쿼리문 정하기
		String part1 = "";
		if (dateSearch.equals("one"))
			part1 = "bdate >= SYSDATE - 1 AND ";
		else if (dateSearch.equals("seven"))
			part1 = "bdate >= SYSDATE - 7 AND ";
		else if (dateSearch.equals("month"))
			part1 = "bdate >= SYSDATE - 30 AND ";
		else if (dateSearch.equals("year"))
			part1 = "bdate >= SYSDATE - 365 AND ";
		else
			part1 = "";

		searchQuery += part1;

		// 단어 검색 범위 설정을 위한 쿼리문 정하기
		String part2 = "";
		int countQMark = 1; // 총 완성될 쿼리문의 ? 개수를 표시해 주는 변수
		if (textSearch.equals("title"))
			part2 = "btitle LIKE ?";
		else if (textSearch.equals("content"))
			part2 = "bcontent LIKE ?";
		else if (textSearch.equals("name"))
			part2 = "bname LIKE ?";
		else {
			part2 = "btitle LIKE ? OR bname LIKE ? OR bcontent LIKE ?";
			countQMark = 3; // 총 완성될 쿼리문의 ? 개수를 표시해 주는 변수
		}
		searchQuery += part2;

		// 정렬을 위한 조건문..
		String part3 = " ORDER BY bgroup DESC, bstep ASC";

		searchQuery += part3;
		// 최종적으로 각각 조건에 맞는 퀴리문을 if문으로 결정해 합쳐 하나의 쿼리문으로 완성

		try {
			String query = "SELECT count(*) AS count FROM mvc_board WHERE " + searchQuery;

			connection = dataSource.getConnection();
			preparedStatement = connection.prepareStatement(query);
			
			if (countQMark == 3) {
				preparedStatement.setString(1, "%" + searchWord + "%");
				preparedStatement.setString(2, "%" + searchWord + "%");
				preparedStatement.setString(3, "%" + searchWord + "%");
			} else {
				preparedStatement.setString(1, "%" + searchWord + "%");
			}
			
			rs = preparedStatement.executeQuery();

			while (rs.next()) {
				totalSearchCount = rs.getInt("count");

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
				if (preparedStatement != null)
					preparedStatement.close();
				if (connection != null)
					connection.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}

		}

		return totalSearchCount;
	};
	
	

}
