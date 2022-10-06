
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>账户挂失</title>
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
        <h2>账户挂失</h2>
    </blockquote>
    <table id="grdNoticeList" lay-filter="grdNoticeList"></table>
</div>
<div class="ns-container">
    <h1 style="text-align: center;margin-bottom: 20px">账户挂失</h1>
    <form class="layui-form">


        <!--改密码-->
        <div class="layui-form-item">
            <label class="layui-form-label">请输入要挂失的账户</label>
            <div class="layui-input-block layui-col-space5">
                <input name="userName" type="text"  lay-verify="required|mobile" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>
        <div class="layui-form-item">
            <label class="layui-form-label">请输入该账户的密码</label>
            <div class="layui-input-block layui-col-space5">
                <input name="password" type="password"  lay-verify="required|mobile" placeholder="" autocomplete="off" class="layui-input">
            </div>
        </div>


        <!--提交按钮-->
        <div class="layui-form-item " style="text-align: center">
            <button class="layui-btn" type="button" lay-submit lay-filter="sub">确认挂失</button>
        </div>
    </form>
</div>

<script src="/resources/layui/layui.js"></script>
<script src="/resources/sweetalert2.all.min.js"></script>

<script>

    //表单提交事件
    layui.form.on('submit(sub)', function(data){
        //console.info("向服务器提交的表单数据",data.field);
        console.log(data);
        layui.$.post("/work/zhgs",data.field,function (json) {
            console.info("服务器返回数据",json);
            if(json.code == "0"){
                /*SweetAlert2确定对话框*/
                swal({
                    type: 'success',
                    html: "<h2>挂失成功</h2>",
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