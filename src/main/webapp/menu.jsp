<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/2/25 20:17
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>层级菜单</title>
    <link type='text/css' rel='stylesheet' href='http://fonts.googleapis.com/css?family=Ubuntu:300,400,700,400italic'/>
    <link type='text/css' rel='stylesheet' href='http://fonts.googleapis.com/css?family=Oswald:400,300,700'/>
    <link type='text/css' rel='stylesheet' href='${pageContext.request.contextPath}/resources/menu/css/base.css'/>
    <link type='text/css' rel='stylesheet' href='${pageContext.request.contextPath}/resources/menu/css/style.css'/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/resources/js/jquery.js"></script>
</head>
<body>
<div id="wrapper">
    <nav>
        <ul class="content clearfix">
            <li><a href="#">最新入库</a></li>
            <li class="dropdown">
                <a href="#">分类文档</a>
                <ul class="sub-menu">
                    <li class="dropdown">
                        <a href="#">信息学院</a>
                        <ul class="sub-menu">
                            <li class="dropdown">
                                <a href="#">软件工程</a>
                                <ul class="sub-menu">
                                    <li><a href="#">软件开发基础（.java）</a></li>
                                    <li><a href="#">Java语言程序设计</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#">网络工程</a>
                                <ul class="sub-menu">
                                    <li><a href="#">数字电路与逻辑设计</a></li>
                                    <li><a href="#">计算机网络技术</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <li class="dropdown">
                        <a href="#">兽医学院</a>
                        <ul class="sub-menu">
                            <li class="dropdown">
                                <a href="#">动物药学</a>
                                <ul class="sub-menu">
                                    <li><a href="#">动物毒理学</a></li>
                                    <li><a href="#">动物免疫学</a></li>
                                </ul>
                            </li>
                            <li class="dropdown">
                                <a href="#">动物医学</a>
                                <ul class="sub-menu">
                                    <li><a href="#">动物解剖学</a></li>
                                    <li><a href="#">动物生物化学</a></li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
            <li><a href="#">优质文档</a></li>
            <li><a href="#">关于我们</a></li>
        </ul>
    </nav>
</div>

<div id="main">
    <div class="content">
        内容
    </div>
</div>

<script type="text/javascript">
    $(function () {
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
