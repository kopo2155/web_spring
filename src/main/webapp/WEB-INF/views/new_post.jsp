<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url var="createUrl" value="/post/create"/>

<form method="post" action="${pageContext.request.contextPath}/post/create">
  제목: <input type="text" name="title"><br>
  내용: <textarea name="content"></textarea><br>

  <!-- ✅ 컨트롤러에서 넣어준 loginUserId 사용 -->
  <input type="hidden" name="userId" value="${loginUserId}"/>

  <select name="boardId">
    <c:forEach var="b" items="${boards}">
      <!-- Board 엔티티 필드명과 일치해야 함: boardId / boardName 가정 -->
      <option value="${b.id}">${b.boardTitle}</option>
    </c:forEach>
  </select>

  <c:if test="${not empty _csrf}">
    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
  </c:if>

  <button type="submit">작성완료</button>
</form>