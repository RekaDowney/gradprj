<%--
  Created by IntelliJ IDEA.
  User: Chung Junbin
  Date: 2017/1/31 13:17
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>图片信息</title>
</head>
<body>
${requestScope.photo.id}<br/>
${requestScope.photo.name}<br/>
${requestScope.photo.path}<br/>
${requestScope.photo.created_time}<br/>
${requestScope.photo.valid}<br/>
<a href="${pageContext.request.contextPath}${requestScope.photo.path}">图片</a>

<script type="text/javascript">
</script>
</body>
</html>
