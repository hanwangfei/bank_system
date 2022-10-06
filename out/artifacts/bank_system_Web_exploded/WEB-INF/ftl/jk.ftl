
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>申请借款</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
    <style>
        /*表单容器*/
        .ns-container {
            position: absolute;
            width: 500px;
            height: 450px;
            top: 150px;
            left: 50%;
            margin-left: -250px;
            padding: 20px;
            box-sizing: border-box;
            border: 1px solid #cccccc;
        }
    </style>
</head>
<body>
<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h2>申请借款</h2>
    </blockquote>
    <table id="grdNoticeList" lay-filter="grdNoticeList"></table>
</div>
<div class="ns-container">
    <h1 style="text-align: center;margin-bottom: 20px">借款申请单</h1>
    <form class="layui-form">
        <!--基本信息-->
        <div class="layui-form-item">
            <label class="layui-form-label">账户</label>
            <div class="layui-input-block">
                <div class="layui-col-md12" style="padding-top: 10px;">
                    ${login_user.username}
                </div>

            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">借款申请人</label>
            <div class="layui-input-block">
                <div class="layui-col-md12" style="padding-top: 10px;">
                    ${current_person.name}
                </div>

            </div>
        </div>
        <!--贷款类型下拉框-->
        <div class="layui-form-item">
            <label class="layui-form-label">借款类别</label>
            <div class="layui-input-block layui-col-space5">
                    <select name="formType" lay-verify="required" lay-filter="cityCode">
                        <option value="1">房贷</option>
                        <option value="2">车贷</option>
                        <option value="3">学贷</option>
                        <option value="4">其他</option>
                    </select>
            </div>
        </div>
        
        <!--贷款时长日期选择框-->
        <div class="layui-form-item">
            <label class="layui-form-label">贷款期限</label>
            <div class="layui-input-block layui-col-space5">
                    <input name="leaveRange" type="text" class="layui-input" id="daterange" placeholder=" - ">
                    <input id="startTime" name="startTime" type="hidden">
                    <input id="endTime" name="endTime" type="hidden">
            </div>
        </div>

        <!--贷款事由-->
        <div class="layui-form-item">
            <label class="layui-form-label">贷款原因</label>
            <div class="layui-input-block layui-col-space5">
                    <input name="reason" type="text"  lay-verify="required|mobile" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">贷款办理人账户</label>
            <div class="layui-input-block layui-col-space5">
                <input name="operatorName" type="text"  lay-verify="required|mobile" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <div class="layui-form-item">
            <label class="layui-form-label">贷款金额</label>
            <div class="layui-input-block layui-col-space5">
                <input name="count" type="number"  lay-verify="required|mobile" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>

        <!--提交按钮-->
        <div class="layui-form-item " style="text-align: center">
                <button class="layui-btn" type="button" lay-submit lay-filter="sub">立即申请</button>
        </div>
    </form>
</div>

<script src="/resources/layui/layui.js"></script>
<script src="/resources/sweetalert2.all.min.js"></script>

<script>

        var layDate = layui.laydate; //Layui日期选择框JS对象
        var layForm = layui.form; //layui表单对象
        var $ = layui.$; //jQuery对象
        //日期时间范围
        layDate.render({
            elem: '#daterange'
            ,type: 'datetime'
            ,range: true
            ,format: 'yyyy年M月d日H时'
            ,done: function(value, start, end){
                //选择日期后出发的时间,设置startTime与endTime隐藏域
                var startTime = start.year + "-" + start.month + "-" + start.date + "-" + start.hours;
                var endTime = end.year + "-" + end.month + "-" + end.date + "-" + end.hours;
                console.info("请假开始时间",startTime);
                $("#startTime").val(startTime);  //将数值放入
                console.info("请假结束时间",endTime);
                $("#endTime").val(endTime);
            }
        });

        //表单提交事件
        layForm.on('submit(sub)', function(data){
            //console.info("向服务器提交的表单数据",data.field);
            console.log(data);
            $.post("/work/jk",data.field,function (json) {
                console.info("服务器返回数据",json);
                if(json.code == "0"){
                    /*SweetAlert2确定对话框*/
                    swal({
                        type: 'success',
                        html: "<h2>贷款申请以提交，请等待审批</h2>",
                        confirmButtonText: "确定"
                    }).then(function (result) {  //当点击完swal的确定按钮后干什么
                        window.location.href="/forward/notice";
                    });
                }else{
                    swal({
                        type: 'warning',
                        html: "<h2>" + json.message + "</h2>",
                        confirmButtonText: "确定"
                    });
                }
            },"json");  //这里的Json时通知ajax服务器返回的是json对象
            return false;
        });


</script>
</body>
</html>