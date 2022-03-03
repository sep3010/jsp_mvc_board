<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <link rel="stylesheet" type="text/css" href = "CSS/style_reply.css">
    
<title>reply_view</title>
</head>
<body class="container">

    <nav class="navbar navbar-dark my-4 py-1">
        <a class="navbar-brand pl-5 py-0" href="list.do">게시판</a>
    </nav>
    
	<table class="table table-borderless mt-4">
		<form action="reply.do" method="post">
			<input type="hidden" name="bid" value="${reply_view.bid}">
			<input type="hidden" name="bgroup" value="${reply_view.bgroup}">
			<input type="hidden" name="bstep" value="${reply_view.bstep}">
			<input type="hidden" name="bindent" value="${reply_view.bindent}">
			<!-- hidden하면 유저에게 보이지 않음 -->
            <tbody>
                <tr>
                    <th>번호</th>
                    <td>${reply_view.bid}</td>
                </tr>
                <tr>
                    <th>작성자</th>
                    <td colspan="4"><input type="text" name="bname" placeholder="${reply_view.bname}" class="w-100 px-3"></td>
                </tr>
                <tr>
                    <th>제목</th>
                    <td colspan="4"><input type="text" name="btitle" placeholder="${reply_view.btitle}" class="w-100 px-3"></td>
                </tr>
                <tr class="content">
                    <th class="align-middle">내용</th>
                    <td colspan="4"><textarea rows="10" name="bcontent" placeholder="${reply_view.bcontent}" class="w-100 px-3"></textarea></td>
                </tr>
            </tbody>
            <tfoot>
                <tr>
                    <td colspan="4" class="text-right">
                        <a href="list.do">목록보기</a>
                        <input type="submit" value="답글 등록하기" class="btn">                  
                    </td>
                </tr>
            </tfoot>
		</form>
	</table>
	
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