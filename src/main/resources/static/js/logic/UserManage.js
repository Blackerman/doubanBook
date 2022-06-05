layui.use(['jquery','form','table','layer'],function () {

    var $ = layui.jquery;
    var form = layui.form;
    var table = layui.table;
    var layer = layui.layer;

    var deleteUser = document.getElementById("delete");



    table.render({
        elem:'#tblResult',
        page:true,
        limit:10,
        cols:[[
            {field:'name',title:'用户名',align:'center'},
            {field:'state',title:'用户状态',align:'center',
            templet:function (rowData) {
                switch (rowData.state) {
                    case 0:
                        return '<font color="green"><b>用户</b></font>';
                            // '<i class="layui-icon layui-icon-about"></i>'+

                    case 1:
                        return '<font color="blue"><b>管理员</b></font>';
                    case 2:
                        return '<font color="red"><b>已屏蔽</b></font>';
                }
            }},
            {
                field:'id',title:'操作',align:'center',
                templet:function (rowData) {
                    var btnInfo = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="info">'+
                        '<i class="layui-icon layui-icon-user"></i>权限管理</button>'
                    return btnInfo;
                }
            }
        ]],
        data:[],

    });

    table.on('tool(tblResult)',function (obj) {
        if ('info'===obj.event){
            $.post('/douban/user/'+obj.data.id,
                {},
                function (result) {
                    $('#divUidForUpdate').html(obj.data.name);
                    layer.open({
                        type:1
                        ,offset:'auto'
                        ,content:$('#divUserInfo')
                        ,btn:['保存','取消']
                        ,btnAlign:'c'
                        ,shade:0
                        ,anim: 5
                        ,yes:function (index,lo) {
                            $.post('/douban/user/update',
                                {
                                    'id':obj.data.id,
                                    'state':$('#updateState').val()
                                },
                                function (result) {
                                    if (result!=null){
                                        layer.msg("修改成功！请再次点击检索按钮！");
                                        layer.close(index);
                                    }else {
                                        layer.msg("您输入的内容有错误,请检查后重新提交！")
                                    }
                                }

                            );
                        }
                    });
                });
        }
    })

    var search = function (){
        table.reload('tblResult',{
            url:'/douban/user/search',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'name' : $('#name').val(),
                'state': $('#selState').val()
            },
            skin:'line',
            even:true
        })
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐
    }

    search();
    $('#btnSearch').click(function (event) {
        search();
    });

    deleteUser.onclick=function(){
        layer.open({
            type:1
            ,offset:'auto'
            ,content:$("#divDeleteUser")
            ,btn:['注销','取消']
            ,btnAlign:'c'
            ,shade:0
            ,anim: 5
            ,yes:function (index,lo) {
                $.post("/douban/user/delete",
                    {
                        'name': $('#name').val(),
                        'password': $('#password').val()
                    },
                    function (result) {
                        layer.msg(result);
                        if (result==="ok"){
                            layer.msg("您的账户已注销!",function(){
                                window.location.href="http://localhost:8080/html/index.html";
                            });
                            layer.close(index);
                        }else {

                            layer.msg("您输入的信息有错误")
                        }
                    }

                );
            }
        });

    }


});