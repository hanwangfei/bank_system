<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>消息通知</title>
    <link rel="stylesheet" href="/resources/layui/css/layui.css">
</head>
<body>
<div class="layui-row">
    <blockquote class="layui-elem-quote">
        <h2>消息通知</h2>
    </blockquote>
    <table id="grdNoticeList" lay-filter="grdNoticeList"></table>
</div>

<script src="/resources/layui/layui.js"></script>

<script>
    layui.table.render({
        elem : "#grdNoticeList" ,
        id : "grdNoticeList" ,
        url : "/notice/list" ,
        page : false ,
        cols :[[
            {field : "" , title : "序号" , width:"10%" , style : "height:60px" , type:"numbers"},
            {field : "create_time" , title : "通知时间" , width : "20%" , templet: function (d) {
                    var newDate = new Date(d.createTime);
                    var strHour;
                    var strMin;
                    var strSecond;
                    if((strHour = newDate.getHours())<10)
                        strHour = "0"+strHour;
                    if((strMin = newDate.getMinutes())<10)
                        strMin = "0"+strMin;
                    if((strSecond = newDate.getSeconds())<10)
                        strSecond = "0"+strSecond;

                    return newDate.getFullYear() + "-" +
                        (newDate.getMonth() + 1) + "-" + newDate.getDate()
                        + " " + strHour + ":" + strMin + ":" + strSecond;
                }},
            {field : "content" , title : "通知内容" , width : "60%"}
        ]]
    })

</script>
</body>
</html>