layui.use(['layer','jquery','form'],function () {
    var form = layui.form;
    var $ = layui.jquery;
    var layer = layui.layer;

    // form.render();
    // $("#modify").click(function(data) {
    form.on('submit(formDemo)',function (data) {
    //     form.render();
        $.post(
            '/douban/user/modifyPsw',
            {
                'p1':data.field.p1,
                'p2':data.field.p2,
                'p3':data.field.p3
            },
            function (returnData) {
                console.log("!");
                if (returnData==='ok'){
                    console.log("1");
                    layer.msg("修改密码成功！！！");
                }
                else if (returnData === 'error1') {
                    console.log("0");
                    layer.msg("原密码错误！！！");
                }
                else {
                    layer.msg("两次输入密码不一致！！！")
                }
            }

        )
    })

    //演示动画开始
    $('.layui-anim').on('click', function(){
        var othis = $(this), anim = othis.data('anim');

        //停止循环
        if(othis.hasClass('layui-anim-loop')){
            return othis.removeClass(anim);
        }
        othis.removeClass(anim);
        setTimeout(function(){
            othis.addClass(anim);
        });
        //恢复渐隐
        if(anim === 'layui-anim-fadeout'){
            setTimeout(function(){
                othis.removeClass(anim);
            }, 1300);
        }
    });
    //演示动画结束



});