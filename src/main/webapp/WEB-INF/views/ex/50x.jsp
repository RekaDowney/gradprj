<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/2/25 13:26
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@taglib prefix="global" uri="/WEB-INF/tag/custom/global.tld" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>50X</title>
    <link type="text/css" rel="stylesheet" href="${pageContext.request.contextPath}/resources/css/common.css"/>
</head>
<body>
系统发生异常，攻城狮正在努力修复！
<div style="display: none;">
    ${error_50x}
</div>
</body>
</html>
