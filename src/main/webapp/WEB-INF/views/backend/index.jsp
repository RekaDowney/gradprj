<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/24 20:28
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>后台管理</title>

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link rel="shortcut icon" href="${baseUrl}/resources/backend/img/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/style.css"/>
</head>

<body class="fixed-sidebar full-height-layout gray-bg" style="overflow:hidden">

<div id="wrapper">
    <!--左侧导航开始-->
    <nav class="navbar-default navbar-static-side" role="navigation">
        <div class="nav-close"><i class="fa fa-times-circle"></i>
        </div>
        <div class="sidebar-collapse">
            <ul class="nav" id="side-menu">
                <li class="nav-header">
                    <div class="dropdown profile-element">
                        <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                                <span class="clear">
                                    <span class="block m-t-xs" style="font-size:20px;">
                                        <i class="fa fa-area-chart"></i>
                                        <strong class="font-bold">后台管理</strong>
                                    </span>
                                </span>
                        </a>
                    </div>
                    <div class="logo-element">后台管理
                    </div>
                </li>
                <li class="hidden-folded padder m-t m-b-sm text-muted text-xs">
                    <span class="ng-scope">分类</span>
                </li>
                <li>
                    <a class="J_menuItem" href="${baseUrl}/backend/main">
                        <i class="fa fa-home"></i>
                        <span class="nav-label">主页</span>
                    </a>
                </li>
                <li>
                    <a href="#">
                        <i class="fa fa-gears"></i>
                        <span class="nav-label">系统管理</span>
                        <span class="fa arrow"></span>
                    </a>
                    <ul class="nav nav-second-level">
                        <shiro:hasPermission name="manage:*:user">
                            <li>
                                <a class="J_menuItem" href="${baseUrl}/backend/user/manage">用户管理</a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="manage:*:role">
                            <li>
                                <a class="J_menuItem" href="${baseUrl}/backend/role/manage">角色管理</a>
                            </li>
                        </shiro:hasPermission>
                        <shiro:hasPermission name="manage:*:perm">
                            <li>
                                <a class="J_menuItem" href="${baseUrl}/backend/category/manage">栏目管理</a>
                            </li>
                        </shiro:hasPermission>
                    </ul>
                </li>
            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->
    <!--右侧部分开始-->
    <div id="page-wrapper" class="gray-bg dashbard-1">

        <div class="row border-bottom">
            <nav class="navbar navbar-static-top" role="navigation" style="margin-bottom: 0">
                <ul class="nav navbar-top-links navbar-right">

                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown">
                            <button id="returnHome" type="button" class="btn btn-sm btn-info">
                                <span class="glyphicon glyphicon-home"></span>
                                返回前台
                            </button>
                        </a>
                    </li>

                    <li class="dropdown">
                        <a class="dropdown-toggle count-info" data-toggle="dropdown">
                            <button id="logout" type="button" class="btn btn-sm btn-primary">
                                <span class="glyphicon glyphicon-log-out"></span>
                                登出
                            </button>
                        </a>
                    </li>
                </ul>
            </nav>
        </div>

        <%-- 进入管理页面的时候就立刻打开首页的 iframe --%>
        <div class="row J_mainContent" id="content-main">
            <iframe id="J_iframe" width="100%" height="100%" src="${baseUrl}/backend/main" frameborder="0"
                    seamless></iframe>
        </div>

    </div>
    <!--右侧部分结束-->
</div>

<script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>

<script src="${baseUrl}/resources/backend/js/plugins/metisMenu/jquery.metisMenu.js"></script>
<script src="${baseUrl}/resources/backend/js/plugins/slimscroll/jquery.slimscroll.min.js"></script>


<!-- 自定义js -->
<script type="text/javascript" src="${baseUrl}/resources/backend/js/hAdmin.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/backend/js/index.js"></script>

<script type="text/javascript">
    <%--
        $("#J_iframe").attr('src', "${baseUrl}/backend/main");
    --%>

    $(function () {
        $('#returnHome').on('click', function (e) {
            window.location.href = "${baseUrl}";
        });

        $('#logout').on('click', function (e) {
            window.location.href = "${baseUrl}/logout";
        });
    });
</script>

</body>
</html>
