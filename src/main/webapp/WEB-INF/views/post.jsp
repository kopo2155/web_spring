<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title><c:out value="${post.title}" /> - 게시글</title>
    <style>
        body { font-family: system-ui, -apple-system, "Segoe UI", Roboto, sans-serif; margin: 24px; }
        .wrap { max-width: 800px; margin: 0 auto; }
        .meta { color:#666; font-size:14px; margin: 8px 0 16px; }
        .meta span { margin-right: 14px; }
        .content { border:1px solid #ddd; border-radius:8px; padding:16px; background:#fafafa; }
        .content pre { margin:0; white-space:pre-wrap; word-break:break-word; }
        .actions { margin-top:18px; display:flex; gap:8px; flex-wrap: wrap; }
        .btn { display:inline-block; padding:6px 10px; border:1px solid #bbb; border-radius:6px; text-decoration:none; color:#222; background:#fff; cursor:pointer; }
        .btn.primary { border-color:#222; font-weight:600; }
        form { display:inline; margin:0; }
        .alert { background:#f6ffed; border:1px solid #b7eb8f; color:#135200; padding:8px 12px; border-radius:6px; margin-bottom:12px; }
        .reply { border:1px solid #eee; padding:10px; border-radius:8px; margin:8px 0; }
        .reply-meta { color:#666; font-size:13px; }
        textarea { width:100%; }
        hr { margin:24px 0; }
    </style>
</head>
<body>
<div class="wrap">

    <c:if test="${not empty msg}">
        <div class="alert"><c:out value="${msg}"/></div>
    </c:if>

    <c:choose>
        <c:when test="${not empty post}">
            <h1><c:out value="${post.title}" /></h1>

            <div class="meta">
                <span>글번호: <c:out value="${post.postId}" /></span>
                <span>
                    작성자:
                    <c:choose>
                        <c:when test="${post.user.username != null && not empty post.user.username}">
                            <c:out value="${post.user.username}"/>
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </span>
                <span>
                    게시판:
                    <c:choose>
                        <c:when test="${post.board != null && not empty post.board.boardTitle}">
                            <c:out value="${post.board.boardTitle}" />
                        </c:when>
                        <c:when test="${post.board != null}">
                            #<c:out value="${post.board.id}" />
                        </c:when>
                        <c:otherwise>-</c:otherwise>
                    </c:choose>
                </span>
            </div>

            <div class="content">
                <pre><c:out value="${post.content}" /></pre>
            </div>

            <div class="actions">
                <a class="btn" href="${pageContext.request.contextPath}/board">목록</a>
                <c:if test="${canEdit}">

                <a class="btn" href="${pageContext.request.contextPath}/post/${post.postId}/edit">수정</a>

                <form action="${pageContext.request.contextPath}/post/${post.postId}/delete" method="post">
                    <c:if test="${not empty _csrf}">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
                    </c:if>
                    <button type="submit" class="btn">삭제</button>
                </form>
                </c:if>

            </div>
        </c:when>
        <c:otherwise>
            <p>게시글을 찾을 수 없습니다.</p>
            <a class="btn primary" href="${pageContext.request.contextPath}/board">목록으로</a>
        </c:otherwise>
    </c:choose>

    <hr>

    <h2 style="margin:8px 0;">댓글</h2>

    <!-- 댓글 목록 -->
    <c:forEach var="r" items="${replies}">
        <div class="reply" >
            <div class="reply-meta">작성자 ID: <c:out value="${r.userId}"/></div>
            <pre style="margin:6px 0 0; white-space:pre-wrap;"><c:out value="${r.replyContent}"/></pre>

<c:if test="${pageContext.request.isUserInRole('ROLE_ADMIN') or r.userId == loginUserId}">
  <button type="button" class="btn" onclick="toggleEdit(${r.replyId})">수정</button>

  <form method="post"
        action="${pageContext.request.contextPath}/reply/${r.replyId}/delete"
        style="display:inline" onsubmit="return confirm('삭제하시겠습니까?');">
    <c:if test="${not empty _csrf}">
      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
    </c:if>
    <button type="submit" class="btn">삭제</button>
  </form>

  <!-- 인라인 수정 폼 -->
  <div id="edit-${r.replyId}" style="display:none; margin-top:8px;">
    <form method="post" action="${pageContext.request.contextPath}/reply/${r.replyId}/edit">
      <textarea name="replyContent" rows="3" maxlength="255" ><c:out value="${r.replyContent}"/></textarea>
          <div class="muted"><span class="count">0 / 255</span></div>

      <c:if test="${not empty _csrf}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      </c:if>
      <button type="submit" class="btn">저장</button>
      <button type="button" class="btn" onclick="toggleEdit(${r.replyId})">취소</button>
    </form>
  </div>
</c:if>
        </div>
    </c:forEach>

    <!-- 댓글 작성 -->
    <c:choose>
        <c:when test="${pageContext.request.userPrincipal ne null}">
            <form method="post" action="${pageContext.request.contextPath}/post/${post.postId}/reply" style="margin-top:12px;">
                <textarea name="replyContent" rows="3" maxlength="255" placeholder="댓글을 입력하세요"></textarea>
                  <div class="muted"><span class="count">0 / 255</span></div>

                <c:if test="${not empty _csrf}">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                </c:if>
                <button type="submit" class="btn">댓글 등록</button>
            </form>
        </c:when>
        <c:otherwise>
            <p style="color:#666;">댓글을 쓰려면 로그인하세요.</p>
        </c:otherwise>
    </c:choose>

</div>

<!-- 수정 폼 토글 스크립트 -->
<script>
  document.querySelectorAll('textarea[name="replyContent"]').forEach(function(ta){
    const limit = parseInt(ta.getAttribute('maxlength') || '255', 10);
    const counter = ta.parentElement.querySelector('.count');
    function enforce(){
      if (ta.value.length > limit) ta.value = ta.value.slice(0, limit);
      if (counter) counter.textContent = ta.value.length + ' / ' + limit;
    }
    ta.addEventListener('input', enforce);
    enforce();
  });

  function toggleEdit(id){
    const el = document.getElementById('edit-' + id);
    if (!el) return;
    el.style.display = (el.style.display === 'none' || el.style.display === '') ? 'block' : 'none';
  }
</script>
</body>
</html>
