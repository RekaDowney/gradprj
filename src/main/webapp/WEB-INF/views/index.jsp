<%--
  Created by IntelliJ IDEA.
  User: Chung Junbin
  Date: 2017/1/31 0:02
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>Welcome</title>
    <%-- bootstrap --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>
    <%-- bootstrap --%>
</head>
<body>
<div class="header"></div>
<div class="main">
    <c:forEach items="${sessionScope.loginAccount.relationPermList}" var="menu">
        <button type="button" class="btn btn-info">
            <a href="${baseUrl}${menu.permUrl}">${menu.permName}</a>
        </button>
    </c:forEach>
</div>
<div class="tail"></div>

<script type="text/javascript">
</script>
</body>
</html>
