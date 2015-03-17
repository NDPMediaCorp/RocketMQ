<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>User Registration</title>
    <script src="http://libs.baidu.com/jquery/1.7.0/jquery.js" type="text/javascript"></script>
    <script src="js/register.js" type="text/javascript"></script>
</head>
<body>

    <select id="team">
        <c:forEach items="${teamList}" var="team">
            <option value="${team.id}">${team.name}</option>
        </c:forEach>
    </select>
    <br />

    <input type="text" id="userName">
    <br />
    <input type="password" id="password">
    <br />
    <select id="role">
        <c:forEach items="${roleList}" var="role">
            <option value="${role.id}">${role.name}</option>
        </c:forEach>
    </select>
    <br />

    <button value="Register" id="registerButton"></button>
</body>
</html>
