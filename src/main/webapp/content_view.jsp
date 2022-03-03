<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
	<!-- Required meta tags -->
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">

    <!-- Bootstrap CSS -->
    <link
            rel="stylesheet"
            href="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/css/bootstrap.min.css"
            integrity="sha384-MCw98/SFnGE8fJT3GXwEOngsV7Zt27NXFoaoApmYm81iuXoPkFOJwJ8ERdknLPMO"
            crossorigin="anonymous">
    <script src="https://kit.fontawesome.com/b7ee8a4337.js" crossorigin="anonymous"></script>

    <!--CSS-->
    <link rel="stylesheet" type="text/css" href = "CSS/style_content_view.css">
    
<title>content_view</title>
</head>
<body class="container">

    <nav class="navbar navbar-dark my-4 py-1">
        <a class="navbar-brand pl-5 py-0" href="list.do">게시판</a>
    </nav>
    
	<table class="table table-borderless mt-4">
		<form action="modify_view.do" method="post">
			<input type="hidden" name="bid" value="${content_view.bid}">
			<tr>
				<th>번호</th>
				<td>${content_view.bid}</td>
				<th>작성자</th>
				<td class="w-25">${content_view.bname}</td>				
				<th>조회수</th>
				<td>${content_view.bhit}</td>					
			</tr>
			<tr>
				<th>제목</th>
				<td colspan="4">${content_view.btitle}</td>			
			</tr>
			<tr class="content">
				<th class="align-middle">내용</th>
				<td colspan="5">${content_view.bcontent}</td>			
			</tr>
			<tr class="menu">
                <td colspan="6" class="pb-0">
                    <input type="submit" value="수정" class="btn">&nbsp;&nbsp;
                    <a href="list.do">목록보기</a>&nbsp;&nbsp;
                    <a href="delete.do?bid=${content_view.bid}">삭제</a>&nbsp;&nbsp;
                    <a href="reply_view.do?bid=${content_view.bid}">답글</a>
                </td>
            </tr>
			<!-- 다음글, 이전글 이동 버튼 -->
			<c:if test="${content_view.bstep eq 0}">
				<tr class="move_content">
					<td colspan="6" class="text-right py-0">					
						<c:if test="${not empty exsistPrevious}">
							<a href="content_previous.do?bid=${content_view.bid}">&ltrif;  이전글</a>&nbsp;&nbsp;
						</c:if>
						
						<c:if test="${not empty exsistNext}">
							<a href="content_next.do?bid=${content_view.bid}">다음글  &rtrif;</a>
						</c:if>						
					</td>
				</tr>
			</c:if>
		</form>
	</table>

	<br>
	<!-- 원본글에 달린 답글 목록 -->
	<c:if test="${not empty replyList && content_view.bstep eq 0}">
		<table class="table table-borderless" id="reply_list">
				<thead>
					<td colspan="4"><i class="fab fa-replyd fa-2x"></i>  답글 목록</td>
				</thead>		
				<tr>
					<th>번호</th>				
					<th>작성자</th>	
					<th>제목</th>	
					<th>날짜</th>
				</tr>
				<c:forEach var="dto" items="${replyList}">
					<tbody>	
						<tr>
							<td>${dto.bid}</td>				
							<td>${dto.bname}</td>	
							<td><a href="content_view.do?bid=${dto.bid}">${dto.btitle}</a></td>	
							<td>${dto.bdate}</td>
						</tr>
					</tbody> 		
				</c:forEach>			
		</table>			
	</c:if>

    <footer>
        <p class="text-right align-bottom p-0">&copy;2021. 박소은 all rights reserved.</p>
    </footer>	


	<!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" 
    integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" 
    crossorigin="anonymous"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.3/umd/popper.min.js" 
    integrity="sha384-ZMP7rVo3mIykV+2+9J3UJ46jBk0WLaUAdn689aCwoqbBJiSnjAK/l8WvCWPIPm49" 
    crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.1.3/js/bootstrap.min.js" 
    integrity="sha384-ChfqqxuZUCnJSK3+MXmPNIyE6ZbWh2IMqE241rYiqJxyMiZ6OW/JmZQ5stwEULTy" 
    crossorigin="anonymous"></script>
</body>
</html>