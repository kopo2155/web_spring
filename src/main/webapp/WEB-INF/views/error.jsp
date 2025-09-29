<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title><c:out value="${title != null ? title : '오류 안내'}"/></title>
  <style>
    body{font-family:system-ui; padding:24px;}
    .card{max-width:680px; margin:0 auto; padding:20px; border:1px solid #eee; border-radius:12px;}
    .btn{display:inline-block; margin-top:12px; padding:8px 12px; border:1px solid #ddd; border-radius:8px; text-decoration:none;}
    .muted{color:#666; font-size:13px}
    ul{margin:8px 0 0 16px}
  </style>
</head>
<body>
  <div class="card">
    <h2><c:out value="${title != null ? title : '요청을 처리하지 못했습니다'}"/></h2>

    <!-- 메인 메시지 -->
    <p><c:out value="${error}"/></p>

    <!-- 필드별 오류 리스트(검증 실패 등) -->
    <c:if test="${not empty fieldErrors}">
      <ul>
        <c:forEach var="e" items="${fieldErrors}">
          <li><c:out value="${e}"/></li>
        </c:forEach>
      </ul>
    </c:if>

    <!-- 상세(개발/디버그용) -->
    <c:if test="${not empty detail}">
      <p class="muted"><c:out value="${detail}"/></p>
    </c:if>

    <!-- Spring Boot 기본 에러 속성도 있으면 표시 가능 -->
    <c:if test="${not empty status}">
      <p class="muted">HTTP <c:out value="${status}"/> • <c:out value="${error}"/></p>
    </c:if>

    <a class="btn" href="javascript:history.back()">뒤로가기</a>
    <a class="btn" href="${pageContext.request.contextPath}/board">목록으로</a>
  </div>
</body>
</html>

