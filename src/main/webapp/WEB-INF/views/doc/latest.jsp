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

    <script type="text/javascript" src="${baseUrl}/resources/moment/moment.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/moment/DateTime.js"></script>

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

        li.page a {
            width: 40px;
        }

        a.list-group-item:hover {
            z-index: 2;
            color: #fff;
            background-color: #337ab7;
            border-color: #337ab7;
        }
    </style>

</head>
<body>
<div class="header" style="background-color: #c56f96">
    头部导航栏
</div>
<div class="main">
</div>
<div class="tail" style="background-color: #b2dba1">尾部说明</div>

<script type="text/javascript">
</script>
</body>
</html>
