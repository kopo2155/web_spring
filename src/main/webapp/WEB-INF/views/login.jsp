<%@ page language="java" contentType = "text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login</title>

</head>

<body>
    <h1>Login</h1>
    <form action="/doLogin" method="post">
    <div>
    <label for="username">Username:</label>
    <input type="text" id="username" name="username"/>
    </div>
    <div>
    <label for="password">Password:</label>
    <input type="password" id="password" name="password"/>
    </div>
    <div>
        <input type="submit" value="Log in"/>
<button type="button" onclick="location.href='/register'">회원가입</button>

          </div>

        </form>
        <a href="/">Go to Home</a>

        <c:if test="${not empty successmsg}">
          <div style="margin:12px 0; padding:10px; background:#e6ffed; border:1px solid #b7eb8f; border-radius:8px;">
            <strong><c:out value="${successmsg}"/></strong>
          </div>
          <script>
            alert('<c:out value="${successmsg}"/>');
          </script>
        </c:if>

</body>
</html>
