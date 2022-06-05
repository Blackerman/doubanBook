layui.use(['jquery','form','table','laydate','layer'],function (){
    var $ =layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var  form = layui.form;
    var layer = layui.layer;

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
            {field:'msgTime',title:'时间',align:'center'},
            {field:'name',title:'用户',align:'center'},
            {field:'state',title:'状态',align:'center'},
            {field:'type',title:'类型',align:'center'},
            {field:'work', title:'操作', align:'center',width:'250',
                templet:function(rowData) {
                    var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="content">'+
                        '<i class="layui-icon layui-icon-form"></i>内容</button>';
                    var btnDelete = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="delete">'+
                        '<i class="layui-icon layui-icon-delete"></i>删除</button>';
                    var btnAnswer = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="interflow">'+
                        '<i class="layui-icon layui-icon-reply-fill"></i>回复</button>';
                    // var btnCollect = '<button class="layui-btn layui-btn-sm layui-btn" lay-event="collect">'+
                    //     '<i class="layui-icon layui-icon-about"></i>收藏</button>';
                    return btnContent+btnDelete+btnAnswer;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[],

    });

    var search = function (){
        table.reload('tblResult',{
            url:'/douban/message/msgSearch',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'type':$('#type').val(),
                'state':$('#state').val(),
                // 'state': $('#selState').val()
            },
            skin:'line',
            even:true
        });
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐
    }
    search();
    $('#btnSearch').click(function (event) {
        search();
    });



    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据

        if ('content' === obj.event) {
            $('#user').html(data.name);
            $('#content').html(data.content);
            // alert("jlkajdsf");
            layer.open({
                type: 1
                , offset: 'auto',
                title: '文章'
                , content: $('#divMessageInfo')
                ,btn: '我知道了'
                // ,btn:['改分','取消']
                , btnAlign: 'c'
                , shade: 0
                , anim: 5
                ,yes: function(){
                    $.post('/douban/message/msgRead',
                        {
                            'mid':data.mid,
                        },
                        function (result) {

                            search();
                        }
                    )
                    layer.closeAll();
                }
            })
        }
        if ('delete' === obj.event) {
            layer.confirm("是否删除？", {icon: 3, title: '提示'}, function (index) {
                $.post('/douban/message/msgDelete',
                    {
                        'mid': data.mid,
                    },
                    function (result) {
                        if (result === 'ok') {
                            layer.msg("删除成功！");
                        } else {
                            layer.msg("异常！");
                        }
                        search();
                    }
                )
            });
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
                        'id':data.uid,
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

