<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <jsp:include page="include/html-title.jsp">
        <jsp:param name="pageTitle" value="Cockpit Home" />
    </jsp:include>
</head>
<body>
    <jsp:include page="include/header.jsp"></jsp:include>
<%
String msg = "";
Object errSMSG =  session.getAttribute("ACCESS_DENIED_MSG");
if (null != errSMSG){
    msg = errSMSG.toString();
    }
%>
    <ul>
        <li><a href="cockpit/name-server/">Manage Name Server List</a></li>
        <li><a href="cockpit/name-server/kv">Manage Name Server KV List</a> </li>
        <li><a href="cockpit/ip/">Manage IP Mapping</a></li>
        <li><a href="cockpit/topic/">Topic Management</a></li>
        <li><a href="cockpit/message/">Query Message</a></li>
        <li><a href="cockpit/consumer-group/">Consumer Group</a></li>
        <li><a href="cockpit/consume-progress/">Consume Progress</a></li>
        <li><a href="cockpit/topic-progress/">Topic Progress</a></li>
        <li><a href="cockpit/admin/user">Manage User</a></li>
    </ul>
    <ul>
       <span style="color:red"> <%=msg%> </span>
    </ul>
</body>
</html>
