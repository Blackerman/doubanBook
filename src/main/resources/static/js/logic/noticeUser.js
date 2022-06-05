layui.use(['jquery','form','table','laydate','layer'],function () {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;
    var layer = layui.layer;

    var data ;


    var unread = function(){
        $.post('/logic/article/topNotice',{},
            function (result){
                data = result.data;
                table.render({
                    elem:'#tblResult',
                    page:true,
                    limit:10,
                    cols:[[
                        {field:'name',title:'公告名称',align:'center'},
                        {field:'author',title:'管理员',align:'center'},
                        {field:'review',title:'阅读人数',align:'center'},
                        {field:'datetime',title:'编辑日期',align:'center'},
                        {field:'work', title:'操作', align:'center',width:'250',
                            templet:function(rowData) {
                                var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="content">'+
                                    '<i class="layui-icon layui-icon-form"></i>内容</button>';
                                return btnContent;
                                // return btnInfo+btnReset;
                            }
                        }

                    ]],
                    data:data,
                });
            })
    }
    unread();


    var search = function (){
        table.reload('tblResult',{
            page:true,
            limit:10,
            url:'/logic/article/searchLove',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'name' : $('#name').val(),
                'author':$('#author').val(),
                'type':2
                // 'state': $('#selState').val()
            },

            skin:'line',
            even:true
        });
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐
    }

    $('#btnSearch').click(function (event) {
        search()
    });

    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据

        if ('content' === obj.event) {
            $('#pContent').html(data.content);
            $('#noticeName').html(data.name);
            $('#noticeAuthor').html(data.author);
            // alert("jlkajdsf");
            layer.open({
                type: 1
                ,title: '公告'
                ,offset:'auto'
                ,shade: 0.8
                ,id: 'LAY_layuipro' //设定一个id，防止重复弹出
                ,btn: '我知道了'
                // ,btn: ['火速围观', '残忍拒绝']
                ,btnAlign: 'c'
                ,moveType: 1 //拖拽模式，0或者1
                ,content: $('#divNoticeInfo')

                ,yes: function(){
                    $.post('/logic/article/readNotice',
                        {
                            'id':data.id,
                        },
                        function (result) {

                            unread();
                        }
                    )
                    layer.closeAll();
                }
            });
        }



    })

})