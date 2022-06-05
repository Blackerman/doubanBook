layui.use(['layer','form','jquery'], function () {
    var layer = layui.layer;
    var form = layui.form;
    var $ = layui.jquery;

    form.on('submit(saveAndCommit)', function (data) {
        $.post(
            '/douban/user/addUser',
            {
                'name' : data.field.name,
                'password' : data.field.password
            },
            function (returnData) {
            if (returnData!=null){
                layer.msg("用户注册成功！！！");
                window.location.href="http://localhost:8080/html/login.html"
            }
            else {
                layer.msg("用户已经存在！！！请重新输入用户名！！！");
            }
            }
        )
    });
});
