<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:url var="createUrl" value="/post/create"/>

<style>
  :root{
    --bg:#f7f8fa; --card:#fff; --line:#e5e7eb; --text:#111;
    --muted:#6b7280; --brand:#2563eb; --brand-2:#1d4ed8;
  }
  body{ background:var(--bg); font:14px/1.5 system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif; color:var(--text); margin:0; padding:24px;}
  .wrap{ max-width:800px; margin:0 auto; }
  .card{ background:var(--card); border:1px solid var(--line); border-radius:14px; padding:20px 22px; box-shadow:0 6px 18px rgba(0,0,0,.04); }
  h2{ margin:4px 0 16px; font-size:20px; }
  .form .row{ display:flex; flex-direction:column; gap:6px; margin-bottom:14px; }
  .form label{ font-weight:600; color:#111827; }
  .form input[type="text"], .form textarea, .form select{
    width:100%; border:1px solid var(--line); border-radius:10px;
    padding:10px 12px; background:#fff; outline:none; transition:border-color .2s, box-shadow .2s;
  }
  .form input::placeholder, .form textarea::placeholder{ color:#9ca3af; }
  .form input:focus, .form textarea:focus, .form select:focus{
    border-color:var(--brand); box-shadow:0 0 0 3px rgba(37,99,235,.15);
  }
  .form textarea{ min-height:160px; resize:vertical; }
  .form .hint{ color:var(--muted); font-size:12px; text-align:right; }
  .actions{ display:flex; gap:8px; margin-top:8px; flex-wrap:wrap; }
  .btn{ display:inline-block; padding:10px 14px; border:1px solid var(--line); border-radius:10px; background:#fff; text-decoration:none; color:var(--text); cursor:pointer; font-weight:600; }
  .btn:hover{ background:#f8fafc; }
  .btn.primary{ background:var(--brand); color:#fff; border-color:var(--brand); }
  .btn.primary:hover{ background:var(--brand-2); }
  @media (max-width:640px){ body{ padding:12px } .card{ padding:16px } }
</style>

<div class="wrap">
  <div class="card">
    <h2>새 글 작성</h2>

    <form method="post" action="${pageContext.request.contextPath}/post/create" class="form">
      <div class="row">
        <label for="title">제목</label>
        <input id="title" type="text" name="title" placeholder="제목을 입력하세요" required>
      </div>

      <div class="row">
        <label for="content">내용</label>
        <textarea id="content" name="content" maxlength="255" placeholder="내용을 입력하세요 (최대 255자)" required></textarea>
        <div class="hint"><span id="counter">0</span>/255</div>
      </div>

      <div class="row">
        <label for="boardId">게시판</label>
        <select id="boardId" name="boardId" required>
          <c:forEach var="b" items="${boards}">
            <option value="${b.id}">${b.boardTitle}</option>
          </c:forEach>
        </select>
      </div>

      <!-- 컨트롤러에서 넣어준 loginUserId -->
      <input type="hidden" name="userId" value="${loginUserId}"/>

      <c:if test="${not empty _csrf}">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
      </c:if>

      <div class="actions">
        <button type="submit" class="btn primary">작성완료</button>
        <a href="${pageContext.request.contextPath}/board" class="btn">목록</a>
      </div>
    </form>
  </div>
</div>

<script>
  (function(){
    const ta = document.getElementById('content');
    const counter = document.getElementById('counter');
    if(ta && counter){
      const update = () => { counter.textContent = ta.value.length; };
      ta.addEventListener('input', update);
      update();
    }
  })();
</script>
