<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/18 22:16
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>403</title>
    <link type="text/css" rel="stylesheet"
          href="${pageContext.request.contextPath}/resources/bootstrap/css/bootstrap.min.css"/>
    <%--<script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.js"></script>--%>
</head>
<body>
<span style="color: red;">当前地址尚未授权或者当前身份状态下无法访问。</span>
<a class="btn btn-primary" href="${pageContext.request.contextPath}/index">回到首页</a>
<script type="text/javascript">
</script>
</body>
</html>
