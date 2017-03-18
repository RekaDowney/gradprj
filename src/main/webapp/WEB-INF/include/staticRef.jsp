<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>

<%-- 引入通用标签库 --%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib prefix="shiro" uri="http://shiro.apache.org/tags" %>
<%-- 引入通用标签库 --%>

<%-- 引入自定义标签库 --%>
<%@taglib prefix="global" uri="/WEB-INF/tag/custom/global.tld" %>
<%-- 引入自定义标签库 --%>

<%-- 订制通用变量 --%>
<c:set var="scheme" value="${pageContext.request.scheme}"/>
<c:set var="host" value="${pageContext.request.serverName}"/>
<c:set var="port" value="${pageContext.request.serverPort}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<c:set var="baseUrl" value="${scheme}://${host}:${port}${ctx}"/>
<%-- 订制通用变量 --%>

<script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>

<%-- 引入自定义右下角提示框 --%>
<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/tip/popupTip.css"/>
<script type="text/javascript" src="${baseUrl}/resources/tip/popupTip.js"></script>
<%-- 引入自定义右下角提示框 --%>
