<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/6 20:07
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>${category.permName}</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <%-- ztree --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.js"></script>
    <%-- ztree --%>

    <%-- bootstrap --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>
    <%-- bootstrap --%>

    <script type="text/javascript" src="${baseUrl}/resources/js/jqPaginator.min.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>

    <style type="text/css">
        .time-badge {
            float: right;
            padding-right: 7px;
        }

        .badge {
            padding: 4px 7px;
            background-color: #5bc0de;
            border-color: #46b8da;
        }

        .pageContainer {
            min-width: 400px;
            width: 70%;
            margin-left: 30%;
            padding-top: 20px;
            min-height: 500px;
            height: 100%;
        }
    </style>

</head>
<body>
<hr/>
<div class="list-group pageContainer">
    <c:forEach items="${docList}" var="doc">
        <a href="${baseUrl}${doc.docUrl}" target="_blank" class="list-group-item">
            <span class="docId" hidden>${doc.id}</span>
            <span class="docName">${doc.docName}</span>
            <span class="time-badge">${global:format(doc.createdTime, "yyyy-MM-dd HH")}</span>
            <c:if test="${global:oneDayAgo(doc.createdTime)}">
                <span class="badge">新</span>
            </c:if>
        </a>
    </c:forEach>
</div>
<ul id="paginator" class="pagination"></ul>

<script type="text/javascript">
    $(function () {
        $("a.list-group-item").hover(
                function (e) {
                    $(this).addClass("active");
                }, function (e) {
                    $(this).removeClass("active");
                });
    });
</script>
</body>
</html>
