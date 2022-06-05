layui.use(['jquery','form','table','laydate','layer'],function (){
    var $ =layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var  form = layui.form;
    var layer = layui.layer;

    var isAll = false;
    // //执行一个laydate实例
    // laydate.render({
    //     elem: '#start' //指定元素
    // });
    //
    // //执行一个laydate实例
    // laydate.render({
    //     elem: '#end' //指定元素
    // });

    table.render({
        elem:'#tblResult',
        page:true,
        limit:10,
        cols:[[
            {field:'name',title:'用户名',align:'center'},
            {field:'userLove',title:'关注用户',align:'center'},
            {field:'userLoved',title:'粉丝',align:'center'},
            {field:'userArticle',title:'发表文章',align:'center'},
            {field:'focus', title:'操作', align:'center',width:'250',
                templet:function(rowData) {
                    var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="interflow">'+
                        '<i class="layui-icon layui-icon-reply-fill"></i></button>';
                    var btnLove;
                    if(rowData.focus){
                        btnLove = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="focus">'+
                                        '<i class="layui-icon layui-icon-heart-fill"></i></button>'
                    }else{
                        btnLove = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="focus">'+
                                        '<i class="layui-icon layui-icon-heart"></i></button>'
                    }


                    return btnContent+btnLove;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[],

    });

    //搜索关注用户
    var searchLove= function (){
        table.reload('tblResult',{
            url:'/douban/info/searchLove',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'name' : $('#name').val(),
                // 'state': $('#selState').val()
            },
            skin:'line',
            even:true
        });
    }
    //搜索按钮点击事件
     $('#btnSearch').click(function (event) {

         isAll = false;
         searchLove();
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐

    });


    var searchAdvise= function (){
        table.reload('tblResult',{
            url:'/douban/info/searchAdvise',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'name' : $('#name').val(),
                // 'state': $('#selState').val()
            },
            skin:'line',
            even:true
        });
    }
    //搜索推荐用户
    $('#btnSearchAdvise').click(function (event) {

        isAll = false;
        searchAdvise();
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐

    });

    // 搜索全部用户
    var searchAll = function (){
        table.reload('tblResult',{
            url:'/douban/info/searchAll',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'name' : $('#name').val(),
                // 'state': $('#selState').val()
            },
            skin:'line',
            even:true
        });
    }
    searchAll();
    $('#btnSearchAll').click(function (event) {

        isAll = true;
        searchAll();
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐

    });



    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据


        if ('focus' === obj.event) {

                $.post('/douban/info/focusLove',
                    {
                        'id':data.id,
                        'focus':data.focus
                    },
                    function (result) {
                        if (result === 'focus'){
                            layer.msg("关注用户成功！");
                        }else {
                            layer.msg("已取消关注用户！！");
                        }
                        if(isAll)
                            searchAll();
                        else
                            searchLove();
                    }
                )

        }

        if ('interflow' === obj.event) {

            layer.prompt({
                formType: 2,
                value: '',
                title: '信息',
                area: ['800px', '350px'] //自定义文本域宽高
            }, function(value, index, elem){
                $.post('/douban/message/msg',
                    {
                        'id':data.id,
                        'content':value
                    },
                    function (result) {
                        if (result === 'success'){
                            layer.msg("发送信息成功！");
                        }else {
                            layer.msg("发送信息失败！");
                        }

                    }
                )
                layer.close(index);
            })
        }

    });
})

