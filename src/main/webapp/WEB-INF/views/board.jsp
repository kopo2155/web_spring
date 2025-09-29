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
.logout-form {
  display: flex;
  justify-content: flex-end; /* 오른쪽 정렬 */
  margin: 10px 0;
}
    :root{
      --bg:#f7f8fa; --card:#fff; --text:#222; --muted:#6b7280;
      --line:#e5e7eb; --brand:#2563eb; --brand-2:#1d4ed8;
      --hover:#f1f5f9; --accent:#111827;
    }
    *{box-sizing:border-box}
    body{
      margin:0; padding:24px;
      background:var(--bg); color:var(--text);
      font:14px/1.5 system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;
    }
    .container{
      max-width:920px; margin:0 auto;
    }
    .card{
      background:var(--card);
      border-radius:14px; box-shadow:0 4px 12px rgba(0,0,0,.04);
      overflow:hidden;
    }
    .table-wrap{overflow-x:auto}
    table{
      width:100%; border-collapse:separate; border-spacing:0;
      min-width:720px;
    }
    thead th, thead td{
      position:sticky; top:0; z-index:1;
      background:linear-gradient(180deg,#fafafa,#f3f4f6);
      color:var(--accent); font-weight:700; letter-spacing:.2px;
      border-bottom:1px solid var(--line);
    }
    th, td{
      padding:14px 16px; border-bottom:1px solid var(--line);
      text-align:center;
    }
    tbody tr:hover{background:var(--hover)}
    tbody td:nth-child(2){ text-align:left } /* 제목 왼쪽 정렬 */

    /* 링크(제목) */
    a.title{
      color:var(--brand); text-decoration:none; font-weight:600;
    }
    a.title:hover{ color:var(--brand-2); text-decoration:underline }

    .actions{
      display:flex; gap:12px; align-items:center; justify-content:space-between;
      padding:16px; background:#fafafa;
    }
    .search{
      display:flex; gap:8px; width:100%; max-width:380px;
    }
    .search input{
      flex:1; height:36px; padding:0 12px; border:1px solid var(--line);
      border-radius:10px; background:#fff; outline:none;
    }
    .search input:focus{ border-color:var(--brand); box-shadow:0 0 0 3px rgba(37,99,235,.15) }

    .btn{
      height:36px; padding:0 14px; border-radius:10px;
      border:1px solid var(--line); background:#fff; cursor:pointer;
      font-weight:600;
    }
    .btn:hover{ background:#f8fafc }
    .btn-primary{
      border-color:transparent; background:var(--brand); color:#fff;
    }
    .btn-primary:hover{ background:var(--brand-2) }

    /* 페이지네이션 */
    .pagination{
      list-style:none; display:flex; gap:8px; padding:16px; margin:0;
      justify-content:center; align-items:center; background:#fafafa;
      border-top:1px solid var(--line);
    }
    .pagination li a{
      display:inline-block; min-width:34px; text-align:center;
      padding:6px 10px; border:1px solid var(--line); border-radius:10px;
      text-decoration:none; color:var(--text); background:#fff;
    }
    .pagination li a:hover{ border-color:var(--brand); color:var(--brand) }
    .pagination li.active a{
      background:var(--brand); color:#fff; border-color:var(--brand);
      font-weight:700;
    }

    /* 작은 화면 */
    @media (max-width:640px){
      body{ padding:12px }
      .actions{ flex-direction:column; align-items:stretch; gap:10px }
      .search{ max-width:none }
    }


    /* 테이블과의 간격을 살짝 */
    .table-wrap{ margin-top:0 }
    .actions-bottom + .pagination{ margin-top:0 }
  </style>
</head>
<body>
        <c:if test="${not empty msg}">
          <div style="margin:12px 0; padding:10px; background:#e6ffed; border:1px solid #b7eb8f; border-radius:8px;">
            <strong><c:out value="${msg}"/></strong>
          </div>
          <script>
            alert('<c:out value="${msg}"/>');
          </script>
        </c:if>

<div class="logout-form">
    <form action="${pageContext.request.contextPath}/logout" method="post">
    <input type = "hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />


    <button type = "submit">로그아웃 </button>
    </form>
    </div>
<div class="container">


    <div class="table-wrap">
<p>
  <c:choose>
    <c:when test="${selectedBoardId == 0}">전체 게시글</c:when>
    <c:otherwise>${board.boardTitle}</c:otherwise>
  </c:choose>
</p>


<form id="listForm" method="get" action="${pageContext.request.contextPath}/board" class="search" style="display:flex;gap:8px;">
  <select name="boardId" onchange="document.getElementById('pageField').value='1'; this.form.submit();">
    <option value="0" ${selectedBoardId == 0 ? 'selected' : ''}>전체</option>
    <c:forEach var="b" items="${boards}">
      <option value="${b.id}" ${b.id == selectedBoardId ? 'selected' : ''}>${b.boardTitle}</option>
    </c:forEach>
  </select>
  <input type="hidden" name="page" id="pageField" value="${pagination.currentPage}">
  <input type="hidden" name="size" value="${size}">
</form>

      <table>
        <thead>
        <tr>
          <td width="100">글번호</td>
          <td width="100">게시판</td>
          <td>제목</td>
          <td width="140">작성자</td>
          <td width="180">등록일</td>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="post" items="${posts}">
          <tr>
            <td>${post.postId}</td>
            <td>${post.board.boardTitle}</td>
            <td>
              <a class="title" href="${pageContext.request.contextPath}/post/${post.postId}">
              <c:out value="${post.title}"/>
              </a>
            </td>
            <td>${post.user.username}</td>
            <td>
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
    </div>

    <ul class="pagination">
      <c:if test="${pagination.firstPage != -1}">
        <li><a href="?page=${pagination.firstPage}&size=${size}&search_value=${fn:escapeXml(search_value)}&boardId=${selectedBoardId}">«</a></li>
      </c:if>
      <c:if test="${pagination.prevPage != -1}">
        <li><a href="?page=${pagination.prevPage}&size=${size}&search_value=${fn:escapeXml(search_value)}&boardId=${selectedBoardId}">‹</a></li>
      </c:if>

      <c:forEach var="p" begin="${pagination.startPage}" end="${pagination.endPage == -1 ? pagination.startPage : pagination.endPage}">
        <li class="${p == pagination.currentPage ? 'active' : ''}">
          <a href="?page=${p}&size=${size}&search_value=${fn:escapeXml(search_value)}&boardId=${selectedBoardId}">${p}</a>
        </li>
      </c:forEach>

      <c:if test="${pagination.nextPage != -1}">
        <li><a href="?page=${pagination.nextPage}&size=${size}&search_value=${fn:escapeXml(search_value)}&boardId=${selectedBoardId}">›</a></li>
      </c:if>
      <c:if test="${pagination.lastPage != -1}">
        <li><a href="?page=${pagination.lastPage}&size=${size}&search_value=${fn:escapeXml(search_value)}&boardId=${selectedBoardId}">»</a></li>
      </c:if>
    </ul>
    <div class="card">
        <div class="actions">
          <form method="get" action="${pageContext.request.contextPath}/board" class="search">
            <input type="hidden" name="boardId" value="${selectedBoardId}"/>

            <input type="text" name="search_value" value="${search_value}" placeholder="제목/내용 검색" />

            <button type="submit" class="btn">검색</button>
          </form>
          <button class="btn btn-primary"
        onclick="location.href='${pageContext.request.contextPath}/new_post?boardId=${selectedBoardId}'">글작성</button>
        </div>
  </div>
</div>
</body>
</html>
