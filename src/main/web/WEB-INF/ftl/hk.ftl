<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
    <title>还款</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        .form-item {
            padding: 10px;
        }

        .form-item-value {
            padding: 10px;
        }
    </style>
</head>
<body>
<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h1>还款</h1>
    </blockquote>
    <!--待审批列表-->
    <table id="grdFormList" lay-filter="grdFormList"></table>
</div>
<!--请假详情对话框-->
<div id="divDialog" style="display: none;padding: 10px">
    <form class="layui-form">

        <div class="layui-form-item">
            <div class="layui-row">
                <div class="layui-col-xs2 form-item">姓名</div>
                <div class="layui-col-xs4 form-item-value" id="name"></div>
                <div class="layui-col-xs2 form-item">手机号</div>
                <div class="layui-col-xs4 form-item-value" id="phone_number"></div>
            </div>
            <div class="layui-row">
                <div class="layui-col-xs2 form-item">账户</div>
                <div class="layui-col-xs4 form-item-value" id="username"></div>
                <div class="layui-col-xs2 form-item">地址</div>
                <div class="layui-col-xs4 form-item-value" id="address"></div>

            </div>
            <div class="layui-row">
                <div class="layui-col-xs2 form-item">起始时间</div>
                <div class="layui-col-xs4 form-item-value" id="startTime"></div>
                <div class="layui-col-xs2 form-item">结束时间</div>
                <div class="layui-col-xs4 form-item-value" id="endTime"></div>
            </div>
            <div class="layui-row">
                <div class="layui-col-xs2 form-item">贷款原因</div>
                <div class="layui-col-xs10 form-item-value" id="reason"></div>
                <div class="layui-col-xs2 form-item">贷款金额</div>
                <div class="layui-col-xs10 form-item-value" id="count"></div>
            </div>
            <!--表单Id-->
            <input type="hidden" name="formId" id="formId">

        </div>

        <div class="layui-form-item">
            <button class="layui-btn layui-btn-fluid " lay-submit lay-filter="audit">确认还款</button>

        </div>
    </form>
</div>

<script src="/resources/layui/layui.js"></script>
<script src="/resources/sweetalert2.all.min.js"></script>

<script>
    var $ = layui.$;

    //将毫秒数转换为"yyyy-MM-dd HH时"字符串格式
    function formatDate(time) {
        var newDate = new Date(time);
        return newDate.getFullYear() + "-" +
            (newDate.getMonth() + 1) + "-" + newDate.getDate()
            + " " + newDate.getHours() + "时";
    }

    // 将table渲染为数据表格
    layui.table.render({
        elem: "#grdFormList", //选择器
        id: "grdFormList", //id
        url: "/work/userList", //ajax请求url
        page: false, //是否分页 true-是 false-否
        cols: [[ //列描述
            {title: "", width: 70, style: "height:60px", type: "numbers"}, // numbers代表序号列
            {
                field: "create_time", title: "申请时间", width: 150, templet: function (d) {
                    //templet代表对数据进行加工后再显示
                    return formatDate(d.create_time)
                }
            },
            {
                field: "form_type", title: "类型", width: 100, templet: function (d) {
                    switch (d.form_type) {
                        case 1:
                            return "房贷";
                        case 2:
                            return "车贷";
                        case 3:
                            return "学贷";
                        case 4:
                            return "其他";
                    }
                }
            },
            {field: "name", title: "姓名", width: 80},
            {field: "phone_number", title: "手机", width: 80},
            {field: "username", title: "账户", width: 80},
            {field: "address", title: "地址", width: 80},
            {field: "count", title: "金额", width: 80},
            {
                field: "start_time", title: "起始时间", width: 150, templet: function (d) {
                    return formatDate(d.start_time)
                }
            },
            {
                field: "end_time", title: "结束时间", width: 150, templet: function (d) {
                    return formatDate(d.end_time)
                }
            },
            {field: "reason", title: "贷款原因", width: 350},
            {
                title: "", width: 150, type: "space", templet: function (d) {
                    var strRec = JSON.stringify(d);
                    console.info("请假单数据", strRec);
                    //将请假单数据存放至data-laf属性中
                    return "<button class='layui-btn layui-btn-danger layui-btn-sm btn-audit' data-laf=" + strRec + " >还款</button>";
                }
            }
        ]]
    })

    // 绑定每一行的审批按钮
    $(document).on("click", ".btn-audit", function () {
        //初始化表单
        $("#divDialog form")[0].reset();
        $("#divDialog form form-item-value").text("");
        //获取当前点击按钮的请假单数据,回填至显示项
        var laf = $(this).data("laf");
        $("#name").text(laf.name);
        $("#phone_number").text(laf.phone_number);
        $("#address").text(laf.address);
        $("#username").text(laf.username);
        $("#startTime").text(formatDate(laf.start_time));
        $("#endTime").text(formatDate(laf.end_time));
        $("#reason").text(laf.reason);
        $("#count").text(laf.count);
        $("#formId").val(laf.form_id);
        //弹出layui对话框
        layui.layer.open({
            type: "1", //页面层
            title: "贷款审批", //标题
            content: $("#divDialog"), //指定对话框容器对象
            area: ["600px", "500px"], //尺寸
            end: function () { //销毁后触发事件
                $("#divDialog").hide();
            }
        })
    })
    /**
     * 提交审批数据
     */
    layui.form.on("submit(audit)", function (data) {
        $.ajax({
            url: "/work/hk", //审核URL
            data: data.field,
            type: "post",
            dataType: "json",
            success: function (json) {
                //关闭所有layui对话框
                layui.layer.closeAll();
                //显示处理结果
                if (json.code == "0") {
                    swal({
                        type: 'success',
                        html: "<h2>还款完毕</h2>",
                        confirmButtonText: "确定"
                    }).then(function (result) {
                        window.location.href = "/forward/notice";
                    });
                } else {
                    swal({
                        type: 'warning',
                        html: "<h2>" + json.message + "</h2>",
                        confirmButtonText: "确定"
                    });
                }
            }
        })
        return false;
    })

</script>
</body>
</html>