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
    <link rel="stylesheet" type="text/css" href = "CSS/style_searchResult.css">

<title>searchResult</title>
</head>
<body class="container">
 
    <nav class="navbar navbar-dark my-4 py-1">
        <a class="navbar-brand pl-5 py-0" href="list.do">게시판</a>
    </nav>

    <!-- 게시글 검색창 -->
    <form class="form-inline justify-content-end mt-5 mb-3" action="searchResult.do" method="get">
    <label class="my-1 mr-2" for="textSearch"><i class="fas fa-search fa-2x" style="color: #D2BBF2;"></i></label>        

        
        <!-- 날짜 검색 범위 -->
        <select class="custom-select mx-2" id="dateSearch" name="dateSearch">
            <option value="one">1일</option>
            <option value="seven">7일</option>
            <option value="month">30일</option>
            <option value="year">1년</option>
            <option value="all" selected="selected">전체 날짜</option>
        </select>

        <!-- 내용 검색 범위 -->
        <select class="custom-select mr-2" id="textSearch" name="textSearch">
            <option value="title">제목만</option>
            <option value="content">내용만</option>
            <option value="name">글작성자</option>
            <option value="total" selected="selected">전체 글</option>
        </select>           

        <input type="text" class="form-control mr-2"
            placeholder="검색어를 입력하세요." name="searchWord">

        <div class="input-group-append">
            <button type="submit" class="btn px-4">검색</button>
        </div>

    </form>
    
	<table class="table table-borderless">
		<c:if test="${not empty searchResult}">
			<thead>
	           <tr class="text-center">
	               <th>번호</th>
	               <th>작성자</th>
	               <th>제목</th>
	               <th>날짜</th>
	               <th>조회수</th>
	           </tr>
	        </thead>
        </c:if>
		<tbody>
			<c:forEach var="dto" items="${searchResult}">          
                <tr>
                    <td>${dto.bid}</td>
                    <td>${dto.bname}</td>
                    <td>
                        <c:forEach begin="1" end="${dto.bindent}">
                            &nbsp;
                        </c:forEach>
                        <c:if test="${dto.bindent >= 1}"><span class="reply">&rdca;[Re]</span></c:if>
                        <c:choose>
                            <c:when test="${dao.getBadgeNew(dto.bid)}">
                                <a href="content_view.do?bid=${dto.bid}">${dto.btitle}</a>
                                &nbsp;&nbsp;<span class="badge-custom">N</span>
                            </c:when>                            
                            <c:otherwise>
                                <a href="content_view.do?bid=${dto.bid}">${dto.btitle}</a>
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${dto.bdate}</td>
                    <td>${dto.bhit}</td>
                </tr>          	
			</c:forEach>
		</tbody>	
        <c:if test="${empty searchResult}">    
            <tr class="none">
                <td colspan="5">"${searchWord}"에 맞는 게시글을 찾지 못했습니다.</td>
            </tr>
        </c:if> 
			
        <tfoot>
            <tr>
                <td colspan="5"><a href="list.do">전체 목록으로 돌아가기</a></td>
            </tr>
        </tfoot>
	
	</table>
	
	<div class="text-center" style="color: #8E6BBF; font-size: 22px;">
		<c:if test="${searchPageMaker.pre}">
	       <a href="searchResult.do${searchPageMaker.makeSearchQuery(searchPageMaker.startPage - 1, dateSearch, textSearch, searchWord)}" style="color: #8E6BBF;">
	       	<img src="img/fast-backward.png">
	       </a>
	    </c:if>
	    
	    <c:if test="${currentPageNum > 1}">
	       <a href="searchResult.do${searchPageMaker.makeSearchQuery(currentPageNum - 1, dateSearch, textSearch, searchWord)}" class="mx-1">
	       	<img src="img/previous.png">
	       </a>
	    </c:if>
	
	    <!-- 링크를 걸어준다 1-10페이지까지 페이지를 만들어주는것  -->
	    <c:forEach var="idx" begin="${searchPageMaker.startPage}" end="${searchPageMaker.endPage}" >
	    	<c:choose>
	    		<c:when test="${idx eq currentPageNum}">
	    			<span style="color: #8E6BBF; font-weight: bold;" class="mx-1">${idx}</span>
	    		</c:when>
	    		
	    		<c:otherwise>
	    			<a href="searchResult.do${searchPageMaker.makeSearchQuery(idx, dateSearch, textSearch, searchWord)}" style="color: #8E6BBF;" class="mx-1">
	    				${idx}
	    			</a>
	    		</c:otherwise>	    	
	    	</c:choose>    	
	    </c:forEach>
	    
	    <c:if test="${currentPageNum < searchPageMaker.realEnd}">
	       <a href="searchResult.do${searchPageMaker.makeSearchQuery(currentPageNum + 1, dateSearch, textSearch, searchWord)}" style="color: #8E6BBF;">
	       	<img src="img/next.png">
	       </a>
	    </c:if>
	      
	    <c:if test="${searchPageMaker.next && searchPageMaker.endPage > 0}">
	       <a href="searchResult.do${searchPageMaker.makeSearchQuery(searchPageMaker.endPage +1, dateSearch, textSearch, searchWord)}" style="color: #8E6BBF;">
	       	<img src="img/fast-forward.png">
	       </a>
	    </c:if>
    </div>  

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