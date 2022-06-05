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
            {field:'name',title:'文章名称',align:'center',edit:'text'},
            {field:'tags',title:'标签',align:'center',edit:'text'},
            {field:'review',title:'点赞人数',align:'center'},
            {field:'datetime',title:'编辑日期',align:'center'},
            {field:'work', title:'操作', align:'center',width:'250',
                templet:function(rowData) {
                    var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="content">'+
                        '<i class="layui-icon layui-icon-form"></i>内容</button>';
                    var btnDelete = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="delete">'+
                        '<i class="layui-icon layui-icon-delete"></i>删除</button>';
                    var btnUpdate = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="update">'+
                        '<i class="layui-icon layui-icon-edit"></i>修改</button>';
                    // var btnCollect = '<button class="layui-btn layui-btn-sm layui-btn" lay-event="collect">'+
                    //     '<i class="layui-icon layui-icon-about"></i>收藏</button>';
                    return btnContent+btnDelete+btnUpdate;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[],

    });

    var search = function (){
        table.reload('tblResult',{
            url:'/douban/info/searchMyArticle',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'name' : $('#name').val(),
                'tags':$('#tags').val(),
                'type':1
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

    $('#btnAdd').click(function (event) {

        layer.open({
            type: 1
            , offset: 'auto'
            , content: $('#divMyArticleAdd')
            , title: '文章'
            , btn: ['增加', '取消']
            , btnAlign: 'c'
            , shade: 0
            , anim: 5
            , yes: function (index, lo) {
                $.post('/douban/info/add',
                    {
                        'name': $('#addMyArticleName').val(),
                        'tags': $('#addMyArticleTags').val(),
                        'content': $('#addMyArticleContent').val(),
                        'type': 1
                    },
                    function (result) {
                        layer.msg("success!！");
                        if (result != null) {
                            layer.msg("文章添加成功");
                            layer.close(index);
                        } else {
                            layer.msg("您输入的内容有错误")
                        }
                        search();
                    }
                );

            }
        });
    })

    table.on('edit(tblResult)', function(obj){
        var value = obj.value //得到修改后的值
        var data = obj.data; //得到所在行所有键值
        var field = obj.field;//得到相应属性
        var name = obj.data.bookName;

        if(field === 'name'){
            $.post('/logic/article/update',
                {
                    'id':data.id,
                    'name':value,
                    'tags':data.tags,
                    'content':data.content
                },
                function (result) {
                    if (result!=null){
                        layer.msg("修改成功！");
                        layer.close(index);
                    }else {
                        layer.msg("您输入的内容有错误")
                    }
                })
        }

        if(field === 'tags'){
            $.post('/logic/article/update',
                {
                    'id':data.id,
                    'name':data.tags,
                    'tags':value,
                    'content':data.content
                },
                function (result) {
                    if (result!=null){
                        layer.msg("修改成功！");
                        layer.close(index);
                    }else {
                        layer.msg("您输入的内容有错误")
                    }
                })
        }


    });

    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据

        if ('content' === obj.event) {
            $('#articleName').html(data.name);
            $('#articleAuthor').html(data.author);
            $('#content').html(data.content);
            // alert("jlkajdsf");
            layer.open({
                type: 1
                , offset: 'auto',
                title: '文章'
                , content: $('#divArticleInfo')
                // ,btn:['改分','取消']
                , btnAlign: 'c'
                , shade: 0
                , anim: 5
            })
        }
        if ('delete' === obj.event) {
            layer.confirm("是否删除？", {icon: 3, title: '提示'}, function (index) {
                $.post('/logic/article/delete',
                    {
                        'id': data.id,
                        'type':1
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
        if ('update'===obj.event) {
            // alert("jlkajdsf");
            $.post('/logic/article/' + obj.data.id,
                {},
                function (result) {
                    $('#updateName').html(result.name);
                    $('#updateTags').html(obj.data.tags);
                    $('#updateContent').html(obj.data.content);
                    layer.open({
                        type: 1
                        , offset: 'auto'
                        , content: $('#divArticleUpdate')
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
                                    'tags':obj.data.tags,
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
})

