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
    <title>账户管理</title>

    <!--[if lt IE 9]>
    <meta http-equiv="refresh" content="0;ie.html"/>
    <![endif]-->

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link rel="shortcut icon" href="${baseUrl}/resources/backend/img/favicon.ico"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/font-awesome.min.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/animate.css"/>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/require.css"/>

    <!-- 该样式文件会对 zTree 造成影响 -->
    <%--<link type="text/css" rel="stylesheet" href="${baseUrl}/resources/backend/css/style.css"/>--%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/demo.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>

    <style type="text/css">
        td.text-danger {
            text-align: center;
            font-size: 20px;
        }

        #import-tip {
            border: none;
            margin-left: 0;
            padding-left: 0;
            font-size: 20px;
            padding-top: 6px;
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
            <strong style="font-weight: bold">注意！</strong>visitor 和 System 账户是基础账户，不允许删除和修改
        </div>
    </div>
    <div class="row">
        <div class="alert alert-danger" style="width: 98%;margin-left: 1%;">
            <a href="#" class="close" data-dismiss="alert">
                &times;
            </a>
            <strong style="font-weight: bold">注意！</strong>所有新增的账户都默认授予 user 角色，所以 Excel 导入之前请确认 user 角色的权限
        </div>
    </div>
    <div class="row">
        <div class="col-sm-1">
            <button id="createAccount" type="button" class="btn btn-primary">
                <span class="glyphicon glyphicon-user"></span>
                创建账户
            </button>
        </div>
        　
        <div class="col-sm-2 import-box">
            <%-- 只接收 Excel 文件 --%>
            <input id="excelFile" type="file" style="display: none"
                   accept="application/vnd.ms-excel,application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"/>
            <button id="import" type="button" class="btn btn-success">
                <span class="glyphicon glyphicon-import"></span>
                导入Excel
            </button>
            <button id="import-tip" type="button" class="btn btn-default">
                <span class="glyphicon glyphicon-question-sign"></span>
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
                <label for="principal"></label>
                <input id="principal" type="text" name="aPrincipal" class="form-control"
                       placeholder="输入账户名称"/>
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
                    <td class="hide">ID</td>
                    <th style="min-width: 20px">序号</th>
                    <th style="min-width: 120px">账户名</th>
                    <th style="min-width: 100px">操作</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty page.content}">
                        <tr>
                            <td colspan="4" class="text-danger">
                                还没有任何账户，点击左上角“创建账户”按钮来生成账户吧
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <c:forEach items="${page.content}" var="account" varStatus="status">
                            <tr>
                                <td class="hide">${account.id}</td>
                                <td>${status.count}</td>
                                <td>${account.principal}</td>
                                <td>
                                    <c:choose>
                                        <c:when test="${account.principal eq 'visitor' or account.principal eq 'System'}">
                                            <button href="${baseUrl}/account/${account.id}/role/grant"
                                                    class="btn btn-sm btn-primary account-role">角色分配
                                            </button>
                                        </c:when>
                                        <c:otherwise>
                                            <button href="${baseUrl}/account/${account.id}/pwd/reset"
                                                    class="btn btn-sm btn-info account-pwd-reset">重置密码
                                            </button>
                                            <button href="${baseUrl}/account/${account.id}/delete"
                                                    class="btn btn-sm btn-danger account-delete">删除
                                            </button>
                                            <button href="${baseUrl}/account/${account.id}/role/grant"
                                                    class="btn btn-sm btn-primary account-role">角色分配
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
                                    <td>
                                        <button class="btn btn-sm btn-info account-pwd-reset">重置密码</button>
                                        <button class="btn btn-sm btn-danger account-delete">删除</button>
                                        <button class="btn btn-sm btn-primary account-role">角色分配</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="hide">1l156489af1w23kl12j3kl12we22k3a1</td>
                                    <td>2</td>
                                    <td>user</td>
                                    <td>
                                        <button class="btn btn-sm btn-info account-pwd-reset">重置密码</button>
                                        <button class="btn btn-sm btn-danger account-delete">删除</button>
                                        <button class="btn btn-sm btn-primary account-role">角色分配</button>
                                    </td>
                                </tr>
                                <tr>
                                    <td class="hide">fss56d4896a1f74915s123e5af434ka1</td>
                                    <td>3</td>
                                    <td>admin</td>
                                    <td>
                                        <button class="btn btn-sm btn-info account-pwd-reset">重置密码</button>
                                        <button class="btn btn-sm btn-danger account-delete">删除</button>
                                        <button class="btn btn-sm btn-primary account-role">角色分配</button>
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
</div>

<%-- 存储 iframe 页面相关请求（增/改）的返回值，JSON 字符串 --%>
<input type="hidden" id="iframeResult" value=""/>
<%-- iframe 页面是否按了右上角的取消键，因为取消键除了触发 cancel 事件外，还会再触发 end 事件 --%>
<input type="hidden" id="iframeCancel" value=""/>

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

        // 立即执行
        var total = ${page.totalPages};
        if (total == 0) {
            $.jqPaginator('#paginator', {
                totalPages: 1,
                visiblePages: 7,
                currentPage: 1,
                first: '<li class="first"><a href="javascript:void(0);">首页</button></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</button></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</button></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</button></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</button></li>',
                onPageChange: onPageChange
            });
            $('#paginator').hide();
        } else {
            $.jqPaginator('#paginator', {
                totalPages: ${page.totalPages},
                visiblePages: 7,
                currentPage: ${page.curPageOffset + 1},
                first: '<li class="first"><a href="javascript:void(0);">首页</button></li>',
                prev: '<li class="prev"><a href="javascript:void(0);">上一页</button></li>',
                next: '<li class="next"><a href="javascript:void(0);">下一页</button></li>',
                last: '<li class="last"><a href="javascript:void(0);">尾页</button></li>',
                page: '<li class="page"><a href="javascript:void(0);">{{page}}</button></li>',
                onPageChange: onPageChange
            });
        }

        function onPageChange(num, type) {
            if (type !== 'change') { // 只处理 change 事件
                return;
            }
            var pageOffset = num - 1;
            var pageSize = DEFAULT_PAGE_SIZE;
            // 分页处理时确认是否有搜素条件
            var searchText = $('#principal').val();
            renderPageChange(pageOffset, pageSize, searchText);
        }

        function renderPageChange(pageOffset, pageSize, principal) {
            if (!principal) {
                principal = '';
            }
            $.ajax({
                url: "${baseUrl}/account/page/" + pageOffset + "/" + pageSize,
                data: {aPrincipal: principal},
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
                var searchText = $("#principal").val();
                if (searchText.length > 0) {
                    body.html("<tr><td colspan='4' class='text-danger'>暂无名称中包含“" + searchText + "”的账户</td></tr>");
                } else {
                    body.html("<tr><td colspan='4' class='text-danger'>暂无数据</td></tr>");
                }
                return;
            }
            var accountPage = page.content;
            var i, account, index = page.reqPageOffset * page.reqPageSize + 1;
            var result = "";
            var resetPwdUrl;
            var deleteUrl;
            var grantRoleUrl;

            for (i = 0; account = accountPage[i]; i++, index++) {
                resetPwdUrl = baseUrl + "/account/" + account.id + "/pwd/reset";
                deleteUrl = baseUrl + "/account/" + account.id + "/delete";
                grantRoleUrl = baseUrl + "/account/" + account.id + "/role/grant";

                result += "<tr>";
                result += "<td class='hide'>" + account.id + "</td>";
                result += "<td>" + index + "</td>";
                result += "<td>" + account.principal + "</td>";
                if (account.principal == 'visitor' || account.principal == 'System') {
                    result += "<td>" +
                            "<button href='" + grantRoleUrl + "' class='btn btn-sm btn-primary account-role'>角色分配</button> " +
                            "</td>";
                } else {
                    result += "<td>" +
                            "<button href='" + resetPwdUrl + "' class='btn btn-sm btn-info account-pwd-reset'>重置密码</button> " +
                            "<button href='" + deleteUrl + "' class='btn btn-sm btn-danger account-delete'>删除</button> " +
                            "<button href='" + grantRoleUrl + "' class='btn btn-sm btn-primary account-role'>角色分配</button> " +
                            "</td>";
                }
                result += "</tr>";
            }
            body.html(result);
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

        // -------------------------------- Excel 导入 --------------------------------
        var importTipText = "<div class='row'>" +
                "    <hr/>" +
                "    <div class='col-sm-12'>" +
                "        <div style='font-size: 16px'>" +
                "            　　Excel 账户文件要求<span class='text-danger'>只有两列</span>，放在 Excel 文件的 A，B 列（头两列）上。<br/>" +
                "            <span class='text-danger'>　　第一行为格式说明，固定为“账户名”和“密码”这两项；</span><br/>" +
                "            　　从第二行开始就是每个账户的账户名以及对应的密码，密码长度建议至少6位数。" +
                "        </div>" +
                "        <div style='font-size: 16px'>" +
                "            <strong><span class='glyphicon glyphicon-star text-danger'></span></strong>" +
                "            <strong><span class='glyphicon glyphicon-star text-danger'></span></strong>" +
                "            特别注意：如果发现账户列表中的账户名重复或者与当前数据库中的账户名重复，" +
                "            将会用后者的密码覆盖原先存在的密码，因此，请谨慎操作！<br/>" +
                "            <span style='font-weight: bold'>Excel 文件的具体格式可以参考下表：</span>" +
                "        </div>" +
                "        <table class='table table-bordered table-responsive'>" +
                "            <thead>" +
                "            <tr>" +
                "                <th>账户名</th>" +
                "                <th>密码</th>" +
                "            </tr>" +
                "            </thead>" +
                "            <tbody>" +
                "            <tr>" +
                "                <td>201330330229</td>" +
                "                <td>185465</td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td>201330330239</td>" +
                "                <td>186478</td>" +
                "            </tr>" +
                "            <tr>" +
                "                <td style='font-weight: bold'>...</td>" +
                "                <td style='font-weight: bold'>...</td>" +
                "            </tr>" +
                "            </tbody>" +
                "        </table>" +
                "    </div>" +
                "</div>";

        $('.import-box').on('click', '#import', function () {
            $('#excelFile').trigger('click');
        }).on('click', '#import-tip', function () {
            layer.open({
                type: 1,
                title: 'Excel账户文件格式说明',
                area: ['50%', '80%'],
                resize: false,
                content: importTipText
            })
        }).on('change', '#excelFile', function () {
            var fadeFilePath = $(this).val();
            // Excel 文件验证
            if (!fadeFilePath.endsWith('.xls') && !fadeFilePath.endsWith('.xlsx')) {
                layer.open({
                    type: 0,
                    title: '错误',
                    icon: 5,
                    content: '请选择Excel文件，文件格式说明可以点击提示框来查看',
                    shadeClose: true
                });
                return;
            }

            var files = $(this)[0].files;
            if (files.length == 1) {
                var f = files[0];
                var formData = new FormData();
                formData.append(f.name, f);
                clearFile('excelFile');
                $.ajax({
                    url: "${baseUrl}/account/excel/upload",
                    type: "POST",
                    data: formData,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if (data.status) {
                            renderPageChange(0, DEFAULT_PAGE_SIZE);
                            layer.open({
                                type: 0,
                                title: '录入账户成功',
                                icon: 6,
                                content: data.msg,
                                shadeClose: true
                            });
                        } else {
                            layer.open({
                                type: 0,
                                title: '录入账户失败',
                                icon: 5,
                                content: data.msg,
                                shadeClose: true
                            });
                        }
                    }
                })
            }
        });
        // -------------------------------- Excel 导入 --------------------------------

        // ---------------------------------- 增删改 ----------------------------------
        $('#createAccount').on('click', function (e) {
            layer.open({
                type: 2,
                title: '创建账户',
                content: baseUrl + "/account/append",
                area: ['50%', '50%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) { // 这里必须添加 cancel 方法来避免 end 方法发生冲突
                    $('#iframeCancel').val('true');
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
                            title: '创建账户成功',
                            icon: 6,
                            shadeClose: true,
                            content: data.msg
                        });
                    } else {
                        layer.open({
                            title: '创建账户失败',
                            icon: 5,
                            shadeClose: true,
                            content: data.msg
                        });
                    }
                }
            });
        });

        $('.table').on('click', '.account-pwd-reset', function () {
            var url = $(this).attr('href');
            var principal = $(this).parent('td').prev().text();
            var text = "确定要将“" + principal + "”账户的密码重置为123456，该操作不可撤销，请谨慎操作！";
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
        }).on('click', '.account-delete', function () {
            var url = $(this).attr('href');
            var principal = $(this).parent('td').prev().text();
            var text = "确定要删除“" + principal + "”账户，该操作不可撤销，请谨慎操作！";
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
        }).on('click', '.account-role', function () {
            var principal = $(this).parent('td').prev().text();
            var url = $(this).attr('href');
            layer.open({
                type: 2,
                title: '账户 -- ' + principal + ' 角色分配',
                content: url,
                area: ['50%', '50%'],
                resize: false,
                moveOut: true,
                cancel: function (index, layero) {
                    $('#iframeCancel').val('true');
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
                            title: '账户角色分配成功',
                            icon: 6,
                            shadeClose: true,
                            content: data.msg
                        });
                    } else {
                        layer.open({
                            title: '账户角色分配失败',
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
            var text = $('#principal').val().trim();
            if (text.length == 0) {
                layer.open({
                    type: 0,
                    title: '错误',
                    icon: 5,
                    content: '请输入要搜索的账户名称',
                    shadeClose: true
                });
                return;
            }
            renderPageChange(0, DEFAULT_PAGE_SIZE, text);
        });

        $('#reset').on('click', function () {
            $("#principal").val('');
            renderPageChange(0, DEFAULT_PAGE_SIZE);
        });
        // ---------------------------------- 搜索 ----------------------------------

        function clearFile(id) {
            var file = $('#' + id);
            file.after(file.clone().val(''));
            file.remove();
        }

    });
</script>
</body>
</html>
