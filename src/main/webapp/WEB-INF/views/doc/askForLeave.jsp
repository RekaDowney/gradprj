<%--
  Created by IntelliJ IDEA.
  User: Zhong Junbin
  Date: 2017/3/21 10:51
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/include/staticRef.jsp" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>请假</title>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/ztree/css/zTreeStyle.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/ztree/js/jquery.ztree.core.js"></script>
    <%-- ztree --%>

    <%-- bootstrap --%>
    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/bootstrap/css/bootstrap.min.css"/>
    <%--<script type="text/javascript" src="${baseUrl}/resources/bootstrap/js/bootstrap.min.js"></script>--%>
    <%-- bootstrap --%>

    <script type="text/javascript" src="${baseUrl}/resources/layer/layer.js"></script>

    <script type="text/javascript" src="${baseUrl}/resources/moment/moment.js"></script>
    <script type="text/javascript" src="${baseUrl}/resources/moment/DateTime.js"></script>

    <link type="text/css" rel="stylesheet" href="${baseUrl}/resources/jeDate/skin/jedate.css"/>
    <script type="text/javascript" src="${baseUrl}/resources/jeDate/jquery.jedate.min.js"></script>

    <style type="text/css">
        .datePicker {
            width: 200px;
            height: 30px;
            border: 1px #A5D2EC solid;
        }

        .dateIcon {
            background: url("data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAABkAAAAQCAYAAADj5tSrAAAABHNCSVQICAgIfAhkiAAAAAlwSFlzAAALEgAACxIB0t1+/AAAABZ0RVh0Q3JlYXRpb24gVGltZQAwNi8xNS8xNGnF/oAAAAAcdEVYdFNvZnR3YXJlAEFkb2JlIEZpcmV3b3JrcyBDUzVxteM2AAAAoElEQVQ4jWPceOnNfwYqAz9dYRQ+E7UtwAaGjyUsDAyYYUgJ2HT5LXZLcEmSCnA6duOlN///////H0bDALl8dPH/////Z8FuNW6Qtvw2nL3lyjsGBgYGhlmRqnj1kGwJuqHIlhJlCXq8EOITEsdqCXLEbbr8FisfFkTo+vBZRFZwERNEFFkCiw90nxJtCalxQmzegltCzVyP1RJq5HZ8AABuNZr0628DMwAAAABJRU5ErkJggg==") no-repeat right center;
        }

        hr {
            margin-top: 0;
            margin-bottom: 10px;
            border: 0;
            border-top: 1px solid #8C0C0C;
        }

        .jedatebox li {
            font-weight: normal;
        }

        /*#askForLeave input[type='text']*/
        .underlineInput {
            border: 0 solid #878787;
            border-bottom-width: 1px;
            text-align: center;
            /*width: 20px;*/
        }
    </style>

</head>
<body>
<!-- layer API -- http://www.layui.com/doc/modules/layer.html -->
<div id="askForLeave" style="width: 100%;height: 100%;padding-left: 10px;">
    <hr/>
    <form id="aflForm" action="${baseUrl}/ask/for/leave" ENCTYPE="multipart/form-data" method="POST">
        <p>
            <label for="college">学　　院</label>
            <input id="college" type="text" name="college" class="underlineInput" size="20"/>
        </p>
        <p>
            <label for="profession">专业班级</label>
            <input id="profession" type="text" name="profession" class="underlineInput" size="20"/>
        </p>
        <!--
            <p>
                <label for="class">班级</label>
                <input id="class" type="text" name="class" class="underlineInput" size="20"/>
            </p>
        -->
        <p>
            <label for="student">姓　　名</label>
            <input id="student" type="text" name="student" class="underlineInput" size="20"/>

        </p>
        <p>
            请假时间：从 <input id="start" class="datePicker dateIcon" type="text" placeholder="开始日期" name="start" readonly>
            到 <input id="end" class="datePicker dateIcon" type="text" placeholder="结束日期" name="end" readonly>
        </p>

        <p>
            <label for="reason">请假理由</label>
            <input id="reason" type="text" name="reason" class="underlineInput" size="80"/>
        </p>
        <p>
            <label for="leaveType">请假类型
                <!--<select class="form-control input-lg">-->
                <select id="leaveType" name="leaveType" class="form-control">
                    <option value="事假">事假</option>
                    <option value="病假">病假</option>
                </select>
            </label>
        </p>

        <p>
            <label>相关证明图片（可选）
                <input id="image" name="certificate" type="file" accept="image/*"/>
            </label>
        </p>

        <button id="create" type="button" class="btn btn-primary">生成请假条</button>
    </form>
</div>
<script type="text/javascript">
    $(function () {
        // 妈的，必须要有小时才能够联动
        var start = {
            isinitVal: true,
            minDate: $.nowDate(0),
            maxDate: $.nowDate(+15), // 最大日期
            format: 'YYYY-MM-DD hh:mm',
            choosefun: function (elem, date) {
                end.minDate = date; // 开始日选好后，重置结束日的最小日期
            }
        };
        var end = {
            minDate: $.nowDate(0), // 设定最小日期为当前日期
            maxDate: $.nowDate(+30),
            format: 'YYYY-MM-DD hh:mm',
            choosefun: function (elem, date) {
                start.maxDate = date;  // 将结束日的初始值设定为开始日的最大日期
            }
        };

        $("#start").jeDate(start);
        $("#end").jeDate(end);
        <!--<input type="text" onkeydown="this.onkeyup();" onkeyup="this.size=(this.value.length>4?this.value.length:4);" size="4">-->

        /*
         $('#askForLeave').on('keyup', '.underlineInput', function () {
         var _this = $(this);
         var preSize = _this.attr('size');
         var curSize = _this.val().length * 2;
         /!*
         if (curSize < 10) {
         curSize += 6;
         } else {
         curSize += 12;
         }
         *!/
         if (curSize > preSize) {
         _this.attr('size', curSize);
         }
         });
         */

        $('#askForLeave').on('click', '#create', function () {
            if (validate()) {
                var form = $('#aflForm')[0];
                var formData = new FormData(form);

                $.ajax({
                    url: "${baseUrl}/ask/for/leave",
                    type: "POST",
                    data: formData,
                    async: false,
                    cache: false,
                    contentType: false,
                    processData: false,
                    success: function (data) {
                        if (data.url) {
                            parent.$('#aflResult').val(JSON.stringify(data));
                            var index = parent.layer.getFrameIndex(window.name); //先得到当前iframe层的索引
                            parent.layer.close(index); //再执行关闭
                        } else {
                            layer.open({
                                title: '错误',
                                icon: 5,
                                content: "请假出错",
                                shadeClose: true
                            });
                        }
                    }
                });
            }
        });

        function validate() {
            if ($('#college').val().trim().length <= 0) {
                showError("请输入学院名称");
                return false;
            }
            if ($('#profession').val().trim().length <= 0) {
                showError("请输入专业名称");
                return false;
            }
            /*
             if ($('#class').val().trim().length <= 0) {
             showError("请输入班级名称");
             return false;
             }
             */
            if ($('#student').val().trim().length <= 0) {
                showError("请输入学生姓名");
                return false;
            }
            var regex = /[0-9]{4}-[0-9]{2}-[0-9]{2} [0-9]{2}:[0-9]{2}/;
            var startTime = $('#start').val();
            if (!regex.test(startTime)) {
                showError('开始时间未指定或者格式不正确');
                return false;
            }
            var endTime = $('#end').val();
            if (!regex.test(endTime)) {
                showError('结束时间未指定或者格式不正确');
                return false;
            }
            startTime = DateTime.parse(startTime, 'yyyy-MM-dd HH:mm');
            endTime = DateTime.parse(endTime, 'yyyy-MM-dd HH:mm');
            if (startTime.isAfter(endTime)) {
                showError('开始时间必须小于结束时间');
                return false;
            }
            if ($('#reason').val().trim().length <= 0) {
                showError("请输入请假理由");
                return false;
            }
            var leaveType = $('#leaveType').val();
            if (leaveType !== '事假' && leaveType !== '病假') {
                showError('请假类型错误');
                return false;
            }
            return true;
        }

        function showError(msg) {
            layer.open({
                type: 0,
                icon: 5,
                title: "错误",
                content: msg,
                shadeClose: true, //开启遮罩关闭
                resize: false
            });
        }

    });
</script>
</body>
</html>
