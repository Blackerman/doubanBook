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
            {field:'name',title:'文章名称',align:'center'},
            {field:'author',title:'作者',align:'center'},
            {field:'review',title:'点赞人数',align:'center'},
            {field:'tags',title:'标签',align:'center'},
            {field:'datetime',title:'编辑日期',align:'center'},
            {field:'work', title:'操作', align:'center',width:'250',
                templet:function(rowData) {
                    var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="content">'+
                        '<i class="layui-icon layui-icon-form"></i>内容</button>';
                    var btnDelete = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="delete">'+
                        '<i class="layui-icon layui-icon-delete"></i>删除</button>';
                    // var btnCollect = '<button class="layui-btn layui-btn-sm layui-btn" lay-event="collect">'+
                    //     '<i class="layui-icon layui-icon-about"></i>收藏</button>';
                    return btnContent+btnDelete;
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

    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据

        if ('content'===obj.event){
            $('#articleName').html(data.name);
            $('#articleAuthor').html(data.author);
            $('#content').html(data.content);
            // alert("jlkajdsf");
            layer.open({
                type:1
                ,offset:'auto',
                title:'文章'
                ,content:$('#divArticleInfo')
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
                        'id':data.id,
                        'type':1
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