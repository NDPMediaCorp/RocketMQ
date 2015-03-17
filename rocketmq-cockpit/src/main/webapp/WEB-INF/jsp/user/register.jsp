<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <%@include file="../include/base-path.jsp"%>
    <base href="<%=basePath%>">
    <title>User Registration</title>
    <script src="http://libs.baidu.com/jquery/1.7.0/jquery.js" type="text/javascript"></script>
    <script src="/js/register.js" type="text/javascript"></script>
</head>
<body>

    <table>
        <tr>
           <td>Team</td>
           <td>
               <select id="team">
                   <c:forEach items="${teamList}" var="team">
                       <option value="${team.id}">${team.name}</option>
                   </c:forEach>
               </select>
           </td>
        </tr>

        <tr>
            <td>User Name</td>
            <td><input type="text" id="userName"></td>
        </tr>

        <tr>
            <td>Password</td>
            <td><input type="password" id="password"></td>
        </tr>

        <tr>
            <td>Confirm Password</td>
            <td>
                <input type="password" id="confirmPassword">
            </td>
        </tr>

        <tr>
            <td>Email</td>
            <td>
                <input type="text" id="email">
            </td>
        </tr>

        <tr>
            <td colspan="2" style="text-align: center;">
                <input type="button" value="Register" id="registerButton">
            </td>
        </tr>
    </table>

   Already Registered? <a href="/cockpit/login.jsp">Login</a>
</body>
</html>
