<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/2/27 21:01
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>文档上传</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/common.css"/>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/css/buttons.css"/>
    <%-- ztree，必须先引入 jQuery --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.js"></script>
    <%-- ztree，必须先引入 jQuery --%>

    <%-- layer 弹出层提示框，必须先引入 jQuery --%>
    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>
    <%-- layer 弹出层提示框，必须先引入 jQuery --%>
    <style type="text/css">
        .fileTag {
            padding: 4px 10px;
            height: 30px;
            line-height: 20px;
            position: relative;
            cursor: pointer;
            color: #d01e99;
            background: #e1ffcd;
            border: 1px solid #ddd;
            border-radius: 4px;
            overflow: hidden;
            display: inline-block;
            *display: inline;
            *zoom: 1;
        }

        .fileTag input {
            position: absolute;
            font-size: 100px;
            /* 必须将 top 和 right 都设置成 0，否则点击时无法打开文件选择框 */
            top: 0;
            right: 0;
            opacity: 0;
            filter: alpha(opacity=0);
            cursor: pointer;
        }

        .fileTag :hover {
            color: #444;
            background: #eee;
            border-color: #ccc;
            text-decoration: none;
        }

        .filenameList {
            top: 0;
            right: 0;
            font-size: 20px;
        }
    </style>
</head>
<body>
<%--
        pdf: application/pdf
        png: image/png
        gif: image/gif
        jpg: image/jpeg
        doc: application/msword
        xls: application/vnd.ms-excel
        ppt: application/vnd.ms-powerpoint
        docx: application/vnd.openxmlformats-officedocument.wordprocessingml.document
        xlsx: application/vnd.openxmlformats-officedocument.spreadsheetml.sheet
        pptx: application/vnd.openxmlformats-officedocument.presentationml.presentation
        流文件： application/octet-stream
--%>

<div>
    <label for="category">上传到栏目：
        <input id="category" type="text" readonly value="" style="width: 200px;"/>
    </label>
    <input id="categoryId" type="hidden"/>
</div>
<div id="menuContainer" style="display:none; position: absolute;">
    <ul id="categoryMenu" class="ztree" style="margin-top:0; width:240px;background-color: #E8E3C0;"></ul>
</div>
<form id="batch" enctype="multipart/form-data" method="post" action="${baseUrl}/doc/upload">
    <p>
        <a class="fileTag">
            选择文件
            <input type="file" multiple="multiple"
            <%-- 只接受 pdf，doc，docx，xls，xlsx，ppt，pptx，后续 JS 也会执行一步简单判断 --%>
                   accept="application/pdf,
                       application/msword,
                       application/vnd.ms-excel,
                       application/vnd.ms-powerpoint,
                       application/vnd.openxmlformats-officedocument.wordprocessingml.document,
                       application/vnd.openxmlformats-officedocument.spreadsheetml.sheet,
                       application/vnd.openxmlformats-officedocument.presentationml.presentation"/>
        </a>
        <br/>
        <span class="filenameList"></span>
    </p>
    <a id="append" class="button button-raised button-primary button-pill">继续添加</a>
    <a id="upload" class="button button-action button-pill">上传</a>
</form>
<script type="text/javascript">
    // 全局变量，用来存储本次需要上传的文件，用于文件过滤
    var fileArr = [];

    $(function () {
        var batch = $("#batch");
        batch.on('change', "input[type='file'][multiple]", function (event) {
            event.stopPropagation();
            event.preventDefault();
            var fileList = $(this)[0].files; // HTML5 的 files 属性得到的不是 Array 对象，而是类似于 List 的一种对象
            var filenameArr = [];
            for (var i = 0, file; file = fileList[i]; i++) {
                console.log({
                    filename: file.name,
                    fileSize: file.size + 'Byte',
                    fileType: file.type,
                    lastModifiedDate: file.lastModifiedDate
                });
                filenameArr.push(file.name);
                // 将文件压入到文件数组中
//                fileArr.push(file);
            }
            $(this).parent().next().next('.filenameList').html('已选择：　' + filenameArr.join('　｜　'));
        });

        batch.on("click", "#append", function (event) {
            event.stopPropagation();
            event.preventDefault();
//            $("#batch input[type='file'][multiple]").forEach();
            $("#append").before("<p>" +
                    "    <a class='fileTag'>" +
                    "            选择文件" +
                    "            <input type='file' name='batch' multiple='multiple'" +
                    "    accept='application/pdf," +
                    "    application/msword," +
                    "    application/vnd.ms-excel," +
                    "    application/vnd.ms-powerpoint," +
                    "    application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                    "    application/vnd.openxmlformats-officedocument.spreadsheetml.sheet," +
                    "    application/vnd.openxmlformats-officedocument.presentationml.presentation'/>" +
                    "    </a>" +
                    "    <br/>" +
                    "    <span class='filenameList'></span>" +
                    "            </p>");
        }).on('click', '#upload', function (event) {
            event.stopPropagation();
            event.preventDefault();
            /*
             // 排序
             fileArr.sort(sortedByFilename);
             // 去重
             fileArr = uniqueByFilename(fileArr);
             */

            var categoryId = $("#categoryId").val();
            // 如果没有选择学科则提示错误并终止本次上传
            if (categoryId.length != 32) {
                layer.open({
                    icon: 5,
                    title: "错误",
                    content: '请先选择要上传的学科栏目',
                    shadeClose: true //开启遮罩关闭
                });
                return;
            }

            var formData = new FormData();
            // 添加 categoryId 参数
            formData.append('categoryId', categoryId);
            var fileItems = $("input[type='file'][multiple]");
            var fileList;
            fileArr.length = 0; // 清空数组
            // 将文件逐个添加到数组中
            for (var i = 0, item; item = fileItems[i]; i++) {
                fileList = $(item)[0].files;
                for (var j = 0, file; file = fileList[j]; j++) {
                    fileArr.push(file);
                }
            }
            for (i = 0; file = fileArr[i]; i++) {
                formData.append(file.name, file);
            }
            $.ajax({
                url: "${baseUrl}/doc/upload",
                type: "POST",
                data: formData,
                async: false,
                cache: false,
                contentType: false,
                processData: false,
                success: function (data) {
                    /*
                     data = JSON.parse(data);
                     if (!data[statusKey]) {
                     data = JSON.parse(data);
                     }
                     */
                    var statusKey = "${global:statusKey()}";
                    if (data[statusKey] === "${global:statusOk()}") {

                        // 清空上传菜单栏
                        $("#category").val("");
                        $("#categoryId").val("");
                        // 清空文件域
                        batch.find("p").remove();
                        $("#append").before("<p>" +
                                "    <a class='fileTag'>" +
                                "            选择文件" +
                                "            <input type='file' name='batch' multiple='multiple'" +
                                "    accept='application/pdf," +
                                "    application/msword," +
                                "    application/vnd.ms-excel," +
                                "    application/vnd.ms-powerpoint," +
                                "    application/vnd.openxmlformats-officedocument.wordprocessingml.document," +
                                "    application/vnd.openxmlformats-officedocument.spreadsheetml.sheet," +
                                "    application/vnd.openxmlformats-officedocument.presentationml.presentation'/>" +
                                "    </a>" +
                                "    <br/>" +
                                "    <span class='filenameList'></span>" +
                                "            </p>");

                        Tip.success(data["${global:successMsgKey()}"]);
                    } else {
                        Tip.danger(data["${global:errorMsgKey()}"]);
                    }
                }
            });
        });

        /**
         * 根据文件的名称执行比较操作
         * @param f1 文件1
         * @param f2 文件2
         * @returns {number}
         */
        function sortedByFilename(f1, f2) {
            return f1.name.localeCompare(f2.name);
        }

        /**
         * 将文件数组根据文件名执行排重
         * @param fileArr 文件数组
         * @returns {Array} 根据文件名排重后的文件数组
         */
        function uniqueByFilename(fileArr) {
            var result = [], hash = {};
            for (var i = 0, file; (file = fileArr[i]) != null; i++) {
                if (!hash[file.name]) {
                    result.push(file);
                    hash[file.name] = true;
                }
            }
            return result;
        }

        // ztree 全局配置
        var setting = {
            view: {
                // 禁用双击非叶子节点时切换非叶子节点的展开和折叠状态
                dblClickExpand: false,
                // 禁用多选
                selectedMulti: false
            },
            data: {
                simpleData: {
                    enable: true,
                    idKey: 'id',
                    pIdKey: "parentId",
                    rootPId: null
                }
            },
            callback: {
                beforeClick: beforeClick,
                onClick: onClick
            }
        };

        // ztree 节点
        var zNodes = ${menuTree};

        function beforeClick(treeId, treeNode) {
            var check = (treeNode && treeNode.attachable);
            if (!check) {
                Tip.error("请选择上传至最低层次的学科");
                return false;
            }
            return true;
        }

        function onClick(e, treeId, treeNode) {
            // 获取 ztree 对象
            // $.fn.zTree.getZTreeObj(treeId)
            // var zTree = $.fn.zTree.getZTreeObj("categoryMenu");
            $("#category").val(treeNode.name);
            $("#categoryId").val(treeNode.id);
        }

        function showMenu() {
            var category = $("#category");
            // 获取 输入框 的偏移
            var cityOffset = category.offset();
            // 确定 ztree 的偏移并滑动显示菜单（ztree）
            $("#menuContainer").css({
                left: cityOffset.left + "px",
                top: cityOffset.top + category.outerHeight() + "px"
            }).slideDown("fast");
            // 为 body 绑定鼠标点击事件
            $("body").on("mousedown", onBodyDown);
        }

        function hideMenu() {
            // 以淡出方式隐藏菜单（ztree）
            $("#menuContainer").fadeOut("fast");
            // 解除 body 上绑定的鼠标点击事件
            $("body").off("mousedown", onBodyDown);
        }

        function onBodyDown(event) {
            var target = event.target;
            if (!(target.id == "category" ||
                    target.id == "menuContainer" ||
                    $(target).parents("#menuContainer").length > 0)) {
                hideMenu();
            }
        }

        $(document).ready(function () {
            $.fn.zTree.init($("#categoryMenu"), setting, zNodes);
            $("#category").on('click', function () {
                showMenu();
                return false;
            });
        });

    });

</script>
</body>
</html>
