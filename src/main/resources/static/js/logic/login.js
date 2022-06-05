layui.use(['jquery'], function () {
    var $ = layui.jquery;

    $("#ToRegister").click(function(event) {
        $('#loginInfo').hide();
        $('#registerInfo').show();
    })

    $("#returnLogin").click(function(event) {
        $('#loginInfo').show();
        $('#registerInfo').hide();
    })

    $("#login").click(function(event) {

        $.post(
            "/douban/user/login",
            {
                'name': $('#name').val(),
                'password': $('#password').val()
            },
            function (result) {
                if (result==='ok')
                {
                    login2();
                }
                else if(result==="l1")
                    layer.msg("用户名不存在");
                else if(result==="pswd error")
                    layer.msg("密码错误");
                else if(result==="q0")
                    login3();
                else if(result==="q2")
                    layer.msg("用户已屏蔽，请联系管理员");

                else
                    layer.msg(result);
            }
        );
    })

    function login2() {
        layer.msg("登录成功");
        $.post(
            "/douban/user/login2",
            {
                'name': $('#name').val(),
                'password': $('#password').val()
            },
            function (result) {
                if (result)
                    window.location.href='http://localhost:8080/mode/index.html';

            }
        )
    }

    function login3() {
        layer.msg("登录成功");
        $.post(
            "/douban/user/login2",
            {
                'name': $('#name').val(),
                'password': $('#password').val()
            },
            function (result) {
                if (result)
                    window.location.href='http://localhost:8080/mode/userIndex.html';

            }
        )
    }



    $("#register").click(function(event) {
        var  newName=document.getElementById('newName').value;
        var newPassword1=document.getElementById('newPassword1').value;
        var newPassword2=document.getElementById('newPassword2').value;
        if(newName)
        {
            if (newPassword1===newPassword2)
            {
                $.post(
                    '/douban/user/addUser',
                    {
                        'name' : $('#newName').val(),
                        'password' : $('#newPassword1').val()
                    },
                    function (returnData) {
                        if (returnData!=null){
                            layer.msg("用户注册成功");
                            window.location.href="http://localhost:8080/html/index.html"
                        }
                        else {
                            layer.msg("用户名已经存在,请重新输入用户名！");
                        }
                    }
                );
            }
            else
            {
                layer.msg("两次输入的密码不一致");
            }

        }
        else
            layer.msg("用户名不能为空");


    })

});
