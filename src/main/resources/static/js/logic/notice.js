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
            {field:'name',title:'公告名称',align:'center'},
            {field:'author',title:'管理员',align:'center'},
            {field:'review',title:'阅读人数',align:'center'},
            {field:'datetime',title:'编辑日期',align:'center'},
            {field:'work', title:'操作', align:'center',width:'250',
                templet:function(rowData) {
                    var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="content">'+
                        '<i class="layui-icon layui-icon-form"></i>内容</button>';
                    var btnDelete = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="delete">'+
                        '<i class="layui-icon layui-icon-delete"></i>删除</button>';
                    var btnUpdate = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="update">'+
                        '<i class="layui-icon layui-icon-edit"></i>修改</button>';
                    return btnContent+btnDelete+btnUpdate;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[],

    });

    var search = function (){
        table.reload('tblResult',{
            url:'/logic/article/search',
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
            page:true,
            limit:10,
            skin:'line',
            even:true
        });
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐
    }

    search();
    $('#btnSearch').click(function (event) {
        search()
    });

    $('#btnAdd').click(function (event) {

        layer.open({
            type: 1
            , offset: 'auto'
            , content: $('#divNoticeAdd')
            ,title:'公告'
            , btn: ['增加', '取消']
            , btnAlign: 'c'
            , shade: 0
            , anim: 5
            , yes: function (index, lo) {
                $.post('/logic/article/add',
                    {
                        'name':$('#addName').val(),
                        'content': $('#addContent').val(),
                        'type':2
                    },
                    function (result) {
                        layer.msg("success!！");
                        if (result != null) {
                            layer.msg("新增公告");
                            layer.close(index);
                        } else {
                            layer.msg("您输入的内容有错误")
                        }
                        search();
                    }
                );
            }
        });

    });

    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据

        if ('content'===obj.event){
            $('#noticeName').html(data.name);
            $('#noticeAuthor').html(data.author);
            $('#pContent').html(data.content);
            // alert("jlkajdsf");
            layer.open({
                type:1
                ,offset:'auto',
                title:'公告'
                ,content:$('#divNoticeInfo')
                // ,btn:['改分','取消']
                ,btnAlign:'c'
                ,shade:0
                ,anim: 5

            })
        }
        if ('delete'===obj.event){
            layer.confirm("是否删除？",{icon:3,title:'提示'},function(index){
                $.post('/logic/article/delete',
                    {
                        'id':data.id
                    },
                    function (result) {
                        if (result === 'ok'){
                            layer.msg("删除成功！");
                        }else {
                            layer.msg("异常！");
                        }
                        search();
                    }
                )
            });

        }
        if ('update'===obj.event) {
            // alert("jlkajdsf");
            $.post('/logic/article/' + obj.data.id,
                {},
                function (result) {
                    $('#updateName').html(result.name);
                    $('#updateAuthor').html(obj.data.author);
                    $('#updateContent').html(obj.data.content);
                    layer.open({
                        type: 1
                        , offset: 'auto'
                        , content: $('#divNoticeUpdate')
                        ,title:'公告'
                        , btn: ['修改', '取消']
                        , btnAlign: 'c'
                        , shade: 0
                        , anim: 5
                        , yes: function (index, lo) {
                            $.post('/logic/article/update',
                                {
                                    'id': obj.data.id,
                                    'name':obj.data.name,
                                    'content': $('#updateContent').val()
                                },
                                function (result) {
                                    layer.msg("success!！");
                                    if (result != null) {
                                        layer.msg("修改成功！");
                                        layer.close(index);
                                    } else {
                                        layer.msg("您输入的内容有错误")
                                    }
                                    search();
                                }
                            );
                        }
                    });
                });
        }


    });




    // // 监听全选
    // form.on('checkbox(checkall)', function(data){
    //
    //     if(data.elem.checked){
    //         $('tbody input').prop('checked',true);
    //     }else{
    //         $('tbody input').prop('checked',false);
    //     }
    //     form.render('checkbox');
    // });




});