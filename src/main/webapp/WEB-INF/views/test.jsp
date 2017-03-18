<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/2/23 20:38
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html>
<head>
    <title>个人主页</title>
</head>
<body>
<%--${global:visitor()}--%>
<c:set var="visitor" value="${global:visitor()}"/>
${visitor.password}
<br/>
<br/>
<hr/>
${global:error50xKey()}
<c:set var="errorKey" value="${global:error50xKey()}"/>
<%-- 如果 ${} 返回的是一个字符串，那么该字符串无法作为 key 而被 $ 再次识别 --%>
<%--${errorKey.message}--%>
${errorKey}
<br/>
<br/>
<hr/>
<%--${error_50x.message}--%>
<c:set var="error50xKey" value="${global:error50xKey()}"/>
${error50xKey}---
<br/>
<hr/>
<c:set var="err" value="${error50xKey}"/>
${err}--
<br/>
<hr/>
<c:set var="err" value="${requestScope.get(error50xKey)}"/>
${err}--
<br/>
<hr/>

<script type="text/javascript">
</script>
</body>
</html>
