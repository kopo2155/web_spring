<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${post.title}" /> - 글 수정</title>
</head>
<body>
<div class="wrap">

  <c:choose>
    <c:when test="${not empty post}">
      <h1>글 수정</h1>

      <form method="post"
            action="${pageContext.request.contextPath}/post/update">
        <!-- 글 ID (서버에서 path로도 받지만, 필요시 hidden으로 함께 보냄) -->
        <input type="hidden" name="id" value="${post.postId}"/>

        <!-- 작성자 유지가 필요하면 서버에서 loginUserId를 넣어두고 hidden으로 전달 -->
        <c:if test="${not empty loginUserId}">
          <input type="hidden" name="userId" value="${loginUserId}"/>
        </c:if>

        <div class="mb-3">
          <label>제목</label><br>
          <input type="text" name="title"
                 value="<c:out value='${post.title}'/>" style="width: 420px;">
        </div>

        <div class="mb-3">
          <label>내용</label><br>
          <textarea name="content" rows="10" style="width: 420px;"><c:out value="${post.content}"/></textarea>
        </div>

        <div class="mb-3">
          <label>게시판</label><br>
          <select name="boardId">
            <c:forEach var="b" items="${boards}">
              <option value="${b.id}"
                <c:if test="${post.board ne null and b.id == post.board.id}">selected</c:if>>
                <c:out value="${b.boardTitle}"/>
              </option>
            </c:forEach>
          </select>
        </div>

        <!-- CSRF -->
        <c:if test="${not empty _csrf}">
          <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        </c:if>

        <button type="submit">수정 완료</button>
        <a href="${pageContext.request.contextPath}/post/${post.postId}">취소</a>
      </form>
    </c:when>

    <c:otherwise>
      <p>게시글을 찾을 수 없습니다.</p>
      <a href="${pageContext.request.contextPath}/board">목록으로</a>
    </c:otherwise>
  </c:choose>

</div>
</body>
</html>
