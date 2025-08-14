<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>게시판</title>
    <style>
      ul.pagination{ list-style:none; padding:0; margin:10px 0; display:flex; gap:6px; }
      ul.pagination li a{ text-decoration:none; padding:4px 8px; border:1px solid #ccc; border-radius:4px; }
      ul.pagination li.active a{ font-weight:700; background:#eee; }
    </style>
</head>
<body>
<table class="table table-dark table-striped" cellspacing=1 width=600 border=1>
<thead>
    <tr>
        <td width=100><p align=center>글번호</P></td>
        <td width=500><p align=center>제목</P></td>
        <td width=100><p align=center>작성자</P></td>
        <td width=200><p align=center>등록일</P></td>
    </tr>
</thead>

<tbody>
    <c:forEach var="post" items="${posts}">
        <tr>
            <td width=100>${post.postId}</td>
            <td width=500>
                <a href="${pageContext.request.contextPath}/post/${post.postId}">
                    ${post.title}

                </a>
            </td>
            <td width=100>
                id=${post.user.id}
            </td>
           <td width="100">
             <c:choose>
               <c:when test="${not empty post.createdAt}">
                 ${fn:substring(fn:replace(post.createdAt, 'T', ' '), 0, 16)}
               </c:when>
               <c:otherwise>-</c:otherwise>
             </c:choose>
           </td>
        </tr>
    </c:forEach>
    </tbody>
</table>

<table  width=650>
    <tr>
    <td width=550></td>
    <td><input type=button value="글작성"        onclick="location.href='${pageContext.request.contextPath}/new_post'">


    </tr>

</table>

<form method="get" action="${pageContext.request.contextPath}/board" class="search">
    <input type="text" class="form-control" name="search_value" value="${search_value}" />
    <button type="submit" class="btn btn-outline-secondary">검색</button>
</form>
<ul class="pagination">
  <c:if test="${pagination.firstPage != -1}">
    <li><a href="?page=${pagination.firstPage}&size=${size}&search_value=${fn:escapeXml(search_value)}">«</a></li>
  </c:if>
  <c:if test="${pagination.prevPage != -1}">
    <li><a href="?page=${pagination.prevPage}&size=${size}&search_value=${fn:escapeXml(search_value)}">‹</a></li>
  </c:if>

  <c:forEach var="p" begin="${pagination.startPage}"   end="${pagination.endPage == -1 ? pagination.startPage : pagination.endPage}">

    <li class="${p == pagination.currentPage ? 'active' : ''}">
      <a href="?page=${p}&size=${size}&search_value=${fn:escapeXml(search_value)}">${p}</a>
    </li>
  </c:forEach>

  <c:if test="${pagination.nextPage != -1}">
    <li><a href="?page=${pagination.nextPage}&size=${size}&search_value=${fn:escapeXml(search_value)}">›</a></li>
  </c:if>
  <c:if test="${pagination.lastPage != -1}">
    <li><a href="?page=${pagination.lastPage}&size=${size}&search_value=${fn:escapeXml(search_value)}">»</a></li>
  </c:if>
</ul>
</body>
</html>