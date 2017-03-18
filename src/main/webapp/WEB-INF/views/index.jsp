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
    <link type='text/css' rel='stylesheet' href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,700,400italic'/>
    <link type='text/css' rel='stylesheet' href='http://fonts.googleapis.com/css?family=Oswald:400,300,700'/>
    <link type='text/css' rel='stylesheet' href='${baseUrl}/resources/menu/css/base.css'/>
    <link type='text/css' rel='stylesheet' href='${baseUrl}/resources/menu/css/style.css'/>
    <script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
    <style type="text/css">
        .show-menu-list {
            display: block;
        }

        .hidden-menu-list {
            display: none;
        }

        #menu-list li {
            list-style-type: none;
        }

    </style>
</head>
<body>
<header>
    <div class="menuBar" style="float: right;">
        <button id="menu" type="button">
            <img src="${baseUrl}/resources/images/menu-icon.jpg" width="20px" height="20px"/>
        </button>
        <ul id="menu-list" style="display: none;">
            <li>
                <c:choose>
                    <c:when test="${loginAccount.principal eq 'visitor'}">
                        <a href="${baseUrl}/auth/login">登陆</a>
                    </c:when>
                    <c:otherwise>
                        <a href="${baseUrl}/logout">登出</a>
                    </c:otherwise>
                </c:choose>
            </li>
            <li>
                <shiro:hasPermission name="manage:*:backend">
                    <a href="#">进入后台管理</a>
                </shiro:hasPermission>
            </li>
        </ul>
    </div>
</header>
<div id="wrapper">
    <nav>
        <ul class="content clearfix">
            <li><a href="${baseUrl}/doc/newest">最新入库</a></li>
            <li class="dropdown">
                <a menuId="1d4b1455eef54443a01dd77468225f52" href="####">分类文档</a>
                <ul class="sub-menu">
                    <li class="dropdown">
                        <a menuId="dc34a2cbf05840e0b457d9d17b648243" href="####">数信学院</a>
                        <ul class="sub-menu">
                            <li class="dropdown">
                                <a menuId="7c8cf9e601344bb98cd8b5b610d08549"
                                   href="####">软件工程</a>
                                <ul class="sub-menu">
                                    <li><a menuId="21b22dfac3b84f179428375345a96eab"
                                           href="${baseUrl}/doc/21b22dfac3b84f179428375345a96eab/list">软件开发基础（.java）</a>
                                    </li>
                                    <li><a menuId="37826d7fb34b471f8643f6945ab582fe"
                                           href="${baseUrl}/doc/37826d7fb34b471f8643f6945ab582fe/list">Java语言程序设计</a>
                                    </li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="####" menuId="b9a5c1880e4e4abf96483842544af502">网络工程</a>
                                <ul class="sub-menu">
                                    <li><a menuId="2d270deadc544187a7234cc2d21215e6"
                                           href="${baseUrl}/doc/2d270deadc544187a7234cc2d21215e6/list">数字电路与逻辑设计</a>
                                    </li>
                                    <li><a menuId="f654d8ecc5fc43be83b660d50dce499c"
                                           href="${baseUrl}/doc/f654d8ecc5fc43be83b660d50dce499c/list">计算机网络技术</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="####" menuId="9d5ad5ceb3d54a1d89f0cade4d4806f9">兽医学院</a>
                        <ul class="sub-menu">
                            <li class="dropdown">
                                <a href="####" menuId="e61fe8e97b294e9287416fefb04b7cec"
                                   onclick="return false;">动物药学</a>
                                <ul class="sub-menu">
                                    <li><a menuId="9d996cd154f74d459d0c2a44b8b68bef"
                                           href="${baseUrl}/doc/9d996cd154f74d459d0c2a44b8b68bef/list">动物毒理学</a></li>
                                    <li><a menuId="e56689b586ff495fa4bf66f73aa4b9f6"
                                           href="${baseUrl}/doc/e56689b586ff495fa4bf66f73aa4b9f6/list">动物免疫学</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="####" menuId="eaedb90bf2854530b137533e1aee25e7">动物医学</a>
                                <ul class="sub-menu">
                                    <li><a menuId="1461e4cd3ec84ec68bdcc4c544af0597"
                                           href="${baseUrl}/doc/1461e4cd3ec84ec68bdcc4c544af0597/list">动物解剖学</a></li>
                                    <li><a menuId="4621a7fb843f4398b6e5af0f349bccd3"
                                           href="${baseUrl}/doc/4621a7fb843f4398b6e5af0f349bccd3/list">动物生物化学</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="####" menuId="ba72dc0eeffe4d53b37f39bb5f7dd62e">林学院</a>
                    </li>
                </ul>
            </li>
            <li><a href="#">优质文档</a></li>
            <li><a href="#">关于我们</a></li>
        </ul>
    </nav>
</div>
<%--
    <nav>
        <ul class="content clearfix">
            <c:set var="menuList" value="${loginAccount.relationPermList}"/>
            <c:forEach items="${menuList}" var="menu1">
                <c:set var="sub1" value="${menu1.sub}"/>
                <c:choose>
                    <c:when test="${not empty sub1}">
                        <li class="dropdown">
                            <a href="#">${menu1.permName}</a> 一级
                            <c:forEach items="${sub1}" var="menu2">
                                <ul class="sub-menu">
                                    <c:set var="sub2" value="${menu2.sub}"/>
                                    <c:choose>
                                        <c:when test="${not empty sub2}">
                                            <li class="dropdown">
                                                <a href="#">${menu2.permName}</a>二级三级四级
                                            </li>
                                        </c:when>
                                        <c:otherwise>
                                            <li><a href="#">${menu2.permName}</a></li>
                                        </c:otherwise>
                                    </c:choose>
                                </ul>
                            </c:forEach>
                        </li>
                    </c:when>
                    <c:otherwise>
                        <li><a href="#">${menu1.permName}</a></li>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </ul>
    </nav>
--%>

<script type="text/javascript">
    $(function () {

        $("#menu").unbind('click').on('click', function () {
            $("#menu-list").toggleClass('show-menu-list')
        });

        $(".dropdown").hover(
                function () {
                    $(this).children(".sub-menu").slideDown(200);
                },
                function () {
                    $(this).children(".sub-menu").slideUp(200);
                }
        )
    });
</script>
</body>
</html>
