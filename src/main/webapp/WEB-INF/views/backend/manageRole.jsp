<%--suppress ALL --%>
<%--suppress JSUnusedLocalSymbols --%>
<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/24 21:01
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>角色管理</title>

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link rel="shortcut icon" href="${baseUrl}/resources/backend/img/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>
    <!-- 该样式文件会对 zTree 造成影响 -->
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/style.css"/>--%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/demo.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/require.css"/>

    <style type="text/css">
        td.text-danger {
            text-align: center;
            font-size: 20px;
        }
    </style>
</head>

<body>
<div class="wrapper wrapper-content animated fadeInRight">
    <div class="row">
        <div class="alert alert-info" style="width: 98%;margin-left: 1%;">
            <a href="#" class="close" data-dismiss="alert">
                &times;
            </a>
            <strong style="font-weight: bold">注意！</strong>visitor 和 user 角色是基础角色，不允许删除和修改
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1">
            <button id="createRole" type="button" class="btn btn-primary">
                <span class="glyphicon glyphicon-user"></span>
                创建角色
            </button>
        </div>
        <div class="col-sm-1 pull-right">
            <button id="reset" type="button" class="btn btn-primary">
                <span class="glyphicon glyphicon-refresh"></span>
                重置
            </button>
        </div>
        <div class="col-sm-4 pull-right">
            <div class="input-group search-box">
                <label for="roleName"></label>
                <input id="roleName" type="text" name="roleName" class="form-control"
                       placeholder="输入角色名称"/>
                <span class="input-group-btn">
                    <button type="button" class="btn btn-info search">
                        <span class="glyphicon glyphicon-search"></span>
                        搜索
                    </button>
                </span>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12 col-center">
            <table class="table table-bordered table-condensed table-responsive" style="margin: 10px auto;">
                <thead>
                <tr>
                    <th class="hide">ID</th>
                    <th style="min-width: 20px">序号</th>
                    <th style="min-width: 120px">角色英文名称</th>
                    <th style="min-width: 120px">角色中文名称</th>
                    <th style="min-width: 120px">备注</th>
                    <th style="min-width: 100px">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty page.content}">
                        <tr>
                            <td colspan="4" class="text-danger">
                                还没有任何角色，点击左上角“创建角色”按钮来生成角色吧
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${page.content}" var="role" varStatus="status">
                            <tr>
                                <td class="hide">${role.id}</td>
                                <td>${status.count}</td>
                                <td>${role.roleName}</td>
                                <td>${role.roleNameCn}</td>
                                <td>${role.remarks}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${role.roleName eq 'visitor' or role.id eq '85d4eb03bc3b407e93a447ee5e27d4c4'}">
                                            <%--<c:when test="${role.roleName eq 'visitor'}">--%>
                                            <button href="${baseUrl}/role/${role.id}/perm/grant"
                                                    class="btn btn-sm btn-primary role-perm">权限分配
                                            </button>
                                            <button href="${baseUrl}/role/${role.id}/category/grant"
                                                    class="btn btn-sm btn-primary role-perm">栏目分配
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button href="${baseUrl}/role/${role.id}/modify"
                                                    class="btn btn-sm btn-info role-modify">修改
                                            </button>
                                            <button href="${baseUrl}/role/${role.id}/delete"
                                                    class="btn btn-sm btn-danger role-delete">删除
                                            </button>
                                            <button href="${baseUrl}/role/${role.id}/perm/grant"
                                                    class="btn btn-sm btn-primary role-perm">权限分配
                                            </button>
                                            <button href="${baseUrl}/role/${role.id}/category/grant"
                                                    class="btn btn-sm btn-primary role-category">栏目分配
                                            </button>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                <%--
                                <tr>
                                    <td class="hide">55597494fsd5af456sa1f3e1qfl2k3a1</td>
                                    <td>1</td>
                                    <td>visitor</td>
                                    <td>游客</td>
                                    <td>
                                        <button href="555" class="btn btn-sm btn-info role-modify">修改</button>
                                        <button href="555" class="btn btn-sm btn-danger role-delete">删除</button>
                                        <button href="555" class="btn btn-sm btn-primary role-perm">权限分配</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="hide">1l156489af1w23kl12j3kl12we22k3a1</td>
                                    <td>2</td>
                                    <td>user</td>
                                    <td>注册用户</td>
                                    <td>
                                        <button href="111" class="btn btn-sm btn-info role-modify">修改</button>
                                        <button href="111" class="btn btn-sm btn-danger role-delete">删除</button>
                                        <button href="111" class="btn btn-sm btn-primary role-perm">权限分配</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="hide">fss56d4896a1f74915s123e5af434ka1</td>
                                    <td>3</td>
                                    <td>admin</td>
                                    <td>管理员</td>
                                    <td>
                                        <button href="fss" class="btn btn-sm btn-info role-modify">修改</button>
                                        <button href="fss" class="btn btn-sm btn-danger role-delete">删除</button>
                                        <button href="fss" class="btn btn-sm btn-primary role-perm">权限分配</button>
                                    </td>
                                </tr>
                --%>
                </tbody>
            </table>
        </div>
    </div>

    <div class="row pull-right">
        <ul id="paginator" class="pagination"></ul>
    </div>

    <%-- 存储 iframe 页面相关请求（增/改）的返回值，JSON 字符串 --%>
    <input type="hidden" id="iframeResult" value=""/>
    <%-- iframe 页面是否按了右上角的取消键，因为取消键除了触发 cancel 事件外，还会再触发 end 事件 --%>
    <input type="hidden" id="iframeCancel" value=""/>
</div>

<script type="text/javascript" src="${baseUrl}/resources/js/jquery.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/js/common.js"></script>

<script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/js/jqPaginator.min.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.min.js"></script>
<script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.excheck.min.js"></script>

<script type="text/javascript">
    $(function () {

        var baseUrl = "${baseUrl}";
        var registeredUserRoleId = '85d4eb03bc3b407e93a447ee5e27d4c4';

        // 立即执行
        var total = ${page.totalPages};
        if (total == 0) {
            $.jqPaginator('#paginator', {
                totalPages: 1,
                visiblePages: 7,
                currentPage: 1,
                first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                onPageChange: onPageChange
            });
            $('#paginator').hide();
        } else {
            $.jqPaginator('#paginator', {
                totalPages: ${page.totalPages},
                visiblePages: 7,
                currentPage: ${page.curPageOffset + 1},
                first: '<li class="first"><a href="javascript:void(0);">首页</a></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</a></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</a></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</a></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</a></li>',
                onPageChange: onPageChange
            });
        }
        $('#reset').on('click', function () {
            $("#roleName").val('');
            renderPageChange(0, DEFAULT_PAGE_SIZE);
        });

        function onPageChange(num, type) {
            if (type !== 'change') { // 只处理 change 事件
                return;
            }
            var pageOffset = num - 1;
            var pageSize = DEFAULT_PAGE_SIZE;
            // 分页处理时确认是否有搜素条件
            var searchText = $('#roleName').val();
            renderPageChange(pageOffset, pageSize, searchText);
        }

        function updatePaginator(page) {
            var paginator = $('#paginator');
            if (page.totalPages == 0) {
                paginator.hide();
            } else {
                paginator.show();
                paginator.jqPaginator('option', {
                    totalPages: page.totalPages,
                    currentPage: page.curPageOffset + 1
                });
            }
        }

        function renderPageChange(pageOffset, pageSize, roleName) {
            if (!roleName) {
                roleName = '';
            }
            $.ajax({
                url: "${baseUrl}/role/page/" + pageOffset + "/" + pageSize,
                data: {roleName: roleName},
                type: "POST",
                cache: false,
                success: function (data) {
                    updatePaginator(data);
                    renderTable(data);
                }
            });
        }

        function renderTable(page) {
            var body = $('table.table> tbody');
            if (page.content.length == 0) {
                var searchText = $("#roleName").val();
                if (searchText.length > 0) {
                    body.html("<tr><td colspan='4' class='text-danger'>暂无名称中包含“" + searchText + "”的角色</td></tr>");
                } else {
                    body.html("<tr><td colspan='4' class='text-danger'>暂无数据</td></tr>");
                }
                return;
            }
            var rolePage = page.content;
            var i, role, index = page.reqPageOffset * page.reqPageSize + 1;
            var result = "";
            var modifyUrl;
            var deleteUrl;
            var grantPermUrl;
            var grantCategoryUrl;
            var remarks;

            for (i = 0; role = rolePage[i]; i++, index++) {
                modifyUrl = baseUrl + "/role/" + role.id + "/modify";
                deleteUrl = baseUrl + "/role/" + role.id + "/delete";
                grantPermUrl = baseUrl + "/role/" + role.id + "/perm/grant";
                grantCategoryUrl = baseUrl + "/role/" + role.id + "/category/grant";
                remarks = '';
                result += "<tr>";
                result += "<td class='hide'>" + role.id + "</td>";
                result += "<td>" + index + "</td>";
                result += "<td>" + role.roleName + "</td>";
                result += "<td>" + role.roleNameCn + "</td>";
                if (!!role.remarks) {
                    remarks = role.remarks;
                }
                result += "<td>" + remarks + "</td>";
                if (role.roleName == 'visitor' || role.id == registeredUserRoleId) {
                    result += "<td>" +
                            "<button href='" + grantPermUrl + "' class='btn btn-sm btn-primary role-perm'>权限分配</button> " +
                            "<button href='" + grantCategoryUrl + "' class='btn btn-sm btn-primary role-category'>栏目分配</button>" +
                            "</td>";
                } else {
                    result += "<td>" +
                            "<button href='" + modifyUrl + "' class='btn btn-sm btn-info role-modify'>修改</button> " +
                            "<button href='" + deleteUrl + "' class='btn btn-sm btn-danger role-delete'>删除</button> " +
                            "<button href='" + grantPermUrl + "' class='btn btn-sm btn-primary role-perm'>权限分配</button> " +
                            "<button href='" + grantCategoryUrl + "' class='btn btn-sm btn-primary role-category'>栏目分配</button>" +
                            "</td>";
                }

                result += "</tr>";
            }
            body.html(result);
        }


        // ---------------------------------- 增删改 ----------------------------------
        $('#createRole').on('click', function (e) {
            layer.open({
                type: 2,
                title: '创建角色',
                content: baseUrl + "/role/append",
                area: ['50%', '75%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) { // 这里必须添加 cancel 方法来避免 end 方法发生冲突
                    $('#iframeCancel').val(true);
                    layer.close(index);
                },
                end: function () { // 弹出层销毁后的回调方法
                    var cancel = $('#iframeCancel');
                    if (cancel.val() == 'true') {
                        cancel.val('');
                        return;
                    }
                    var cb = $('#iframeResult').val();
                    var data = JSON.parse(cb);
                    if (data.status) {
                        renderPageChange(0, DEFAULT_PAGE_SIZE);
                        layer.open({
                            title: '创建角色成功',
                            icon: 6,
                            shadeClose: true,
                            content: data.msg
                        });
                    } else {
                        layer.open({
                            title: '创建角色失败',
                            icon: 5,
                            shadeClose: true,
                            content: data.msg
                        });
                    }
                }
            });
        });

        $('.table').on('click', '.role-modify', function () {
            // 必须通过 prevAll 遍历所有前面的兄弟节点（prev 仅仅遍历上一个兄弟节点，如果过滤条件不符合则返回空数组）
            // var roleId = $(this).parent('td').prevAll('.hide')[0].innerText;
            var roleNameCn = $(this).parent('td').prev().prev();
            var roleName = roleNameCn.prev();
            var info = roleName.text() + '(' + roleNameCn.text() + ')';
            var url = $(this).attr('href');
            layer.open({
                type: 2,
                title: '修改角色 -- ' + info,
                content: url,
                area: ['50%', '75%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) {
                    $('#iframeCancel').val(true);
                    layer.close(index);
                },
                end: function () { // 弹出层销毁后的回调方法
                    var cancel = $('#iframeCancel');
                    if (cancel.val() == 'true') {
                        cancel.val('');
                        return;
                    }
                    var cb = $('#iframeResult').val();
                    var data = JSON.parse(cb);
                    if (data.status) {
                        renderPageChange(0, DEFAULT_PAGE_SIZE);
                        layer.open({
                            title: '修改角色成功',
                            icon: 6,
                            shadeClose: true,
                            content: data.msg
                        });
                    } else {
                        layer.open({
                            title: '修改角色失败',
                            icon: 5,
                            shadeClose: true,
                            content: data.msg
                        });
                    }
                }
            });
        }).on('click', '.role-delete', function () {
            var url = $(this).attr('href');
            var roleNameCn = $(this).parent('td').prev().prev();
            var roleName = roleNameCn.prev();
            var text = "确定要删除" + roleName.text() + "（" + roleNameCn.text() + "）角色，该操作不可撤销，请谨慎操作！";
            layer.confirm(text, {
                btn: ['确认', '取消'],
                yes: function (index, layero) {
                    $.ajax({
                        type: "POST",
                        url: url,
                        success: function (data) {
                            if (data.status) {
                                renderPageChange(0, 10);
                                layer.msg(data.msg);
                            } else {
                                layer.alert(data.msg, {icon: 5, shadeClose: true});
                            }
                        }
                    });
                },
                btn2: function (index, layero) {
                    // layer.msg('不删除该角色');
                },
                cancel: function () { // 右上角关闭按钮
                    // return false; // 返回 false 将无法关闭该确认框
                    // layer.msg('不删除该角色');
                    return true;
                }
            });
        }).on('click', '.role-perm', function () {
            var roleNameCn = $(this).parent('td').prev().prev();
            var roleName = roleNameCn.prev();
            var info = roleName.text() + '(' + roleNameCn.text() + ')';
            var url = $(this).attr('href');
            layer.open({
                type: 2,
                title: '角色 -- ' + info + ' 权限分配',
                content: url,
                area: ['50%', '75%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) {
                    $('#iframeCancel').val(true);
                    layer.close(index);
                },
                end: function () { // 弹出层销毁后的回调方法
                    var cancel = $('#iframeCancel');
                    if (cancel.val() == 'true') {
                        cancel.val('');
                        return;
                    }
                    var cb = $('#iframeResult').val();
                    var data = JSON.parse(cb);
                    if (data.status) {
                        layer.open({
                            title: '角色权限分配成功',
                            icon: 6,
                            shadeClose: true,
                            content: data.msg
                        });
                    } else {
                        layer.open({
                            title: '角色权限分配失败',
                            icon: 5,
                            shadeClose: true,
                            content: data.msg
                        });
                    }
                }
            });
        }).on('click', '.role-category', function () {
            var roleNameCn = $(this).parent('td').prev().prev();
            var roleName = roleNameCn.prev();
            var info = roleName.text() + '(' + roleNameCn.text() + ')';
            var url = $(this).attr('href');
            layer.open({
                type: 2,
                title: '角色 -- ' + info + ' 栏目分配',
                content: url,
                area: ['50%', '75%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) {
                    $('#iframeCancel').val(true);
                    layer.close(index);
                },
                end: function () { // 弹出层销毁后的回调方法
                    var cancel = $('#iframeCancel');
                    if (cancel.val() == 'true') {
                        cancel.val('');
                        return;
                    }
                    var cb = $('#iframeResult').val();
                    var data = JSON.parse(cb);
                    if (data.status) {
                        layer.open({
                            title: '角色栏目分配成功',
                            icon: 6,
                            shadeClose: true,
                            content: data.msg
                        });
                    } else {
                        layer.open({
                            title: '角色栏目分配失败',
                            icon: 5,
                            shadeClose: true,
                            content: data.msg
                        });
                    }
                }
            });
        });
        // ---------------------------------- 增删改 ----------------------------------


        // ---------------------------------- 搜索 ----------------------------------
        $('.search-box').on('click', '.search', function () {
            var text = $('#roleName').val().trim();
            if (text.length == 0) {
                layer.open({
                    type: 0,
                    title: '错误',
                    icon: 5,
                    content: '请输入要搜索的角色名称',
                    shadeClose: true
                });
                return;
            }
            renderPageChange(0, DEFAULT_PAGE_SIZE, text);
        });
        // ---------------------------------- 搜索 ----------------------------------

    });
</script>
</body>
</html>
