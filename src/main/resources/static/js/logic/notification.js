layui.use(['jquery','form','table','laydate','layer'],function () {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;
    var layer = layui.layer;

    var data ;


    //未读公告的显示
    $.post('/logic/article/topNotice',{},
        function (result){
            data = result.data;
            $('#noticeNumber').html(result.data.length);
            $('#noticeNumber1').html(result.data.length);

        })

    //未读消息的显示
    $.post('/douban/message/unreadMsg',
        {},
        function (result) {
            $('#msgNumber1').html(result[0]);
            $('#msgNumber2').html(result[1]);
            $('#msgNumber3').html(result[2]);
            $('#msgNumber').html(result[0]+result[1]+result[2]);

        }
    )

    //用户名称的显示
    $.post('/douban/statistics/personal',
        {
        },
        function (result) {
            $('#adminName').html(result.name);
        }
    )





})