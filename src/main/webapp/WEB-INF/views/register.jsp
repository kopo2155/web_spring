<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
</head>
<body>
<h1>회원가입</h1>
<form action="${pageContext.request.contextPath}/register" method="post">
    <div>
        <label for="username">아이디:</label>
        <input type="text" id="username" name="username" required/>
    </div>
    <div>
        <label for="password">비밀번호:</label>
        <input type="password" id="password" name="password" required/>
    </div>
    <div>
        <button type="submit">가입하기</button>
        <button type="button" onclick="location.href='${pageContext.request.contextPath}/login'">취소</button>
    </div>
</form>
</body>
</html>