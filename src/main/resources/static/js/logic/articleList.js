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
            {field:'work', title:'操作', align:'center',width:'450',
                templet:function(rowData) {
                    var btnContent = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="content">'+
                        '<i class="layui-icon layui-icon-form"></i>内容</button>';
                    // var btnDelete = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="delete">'+
                    //     '<i class="layui-icon layui-icon-reduce-circle"></i>删除</button>';
                    var btnLove;
                    if(rowData.love){
                        btnLove = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="love">'+
                            '<i class="layui-icon layui-icon-heart-fill"></i>点赞</button>'
                    }else{
                        btnLove = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="love">'+
                            '<i class="layui-icon layui-icon-heart"></i>点赞</button>'
                    }
                    var btnMsg = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="interflow">'+
                        '<i class="layui-icon layui-icon-reply-fill"></i>评论</button>';
                    var btnFocus;
                    if(rowData.focus){
                        btnFocus = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="focus">'+
                            '<i class="layui-icon layui-icon-heart-fill"></i>关注</button>'
                    }else{
                        btnFocus = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="focus">'+
                            '<i class="layui-icon layui-icon-heart"></i>关注</button>'
                    }
                    return btnContent+btnLove+btnMsg+btnFocus;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[],

    });

    var search = function (){
        table.reload('tblResult',{
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
            layer.open({
                type:1
                ,offset:'auto',
                title:'文章'
                ,content:$('#divArticleInfo')
                ,btnAlign:'c'
                ,shade:0
                ,anim: 5
            })
        }
        if ('love'===obj.event){
            $.post('/logic/article/love',
                {
                    'id':data.id,
                    'author':data.author,
                    'love':data.love,
                },
                function (result) {
                    if (result === 'love'){
                        layer.msg("点赞！");
                    }else if(result==='cancel'){
                        layer.msg("取消点赞！");
                    }else{
                        layer.msg("异常!");
                    }
                    search();
                }
            )
        }
        if ('focus' === obj.event) {

            $.post('/douban/info/focusLove',
                {
                    'id':data.aid,
                    'focus':data.focus
                },
                function (result) {
                    if (result === 'focus'){
                        layer.msg("关注用户成功！");
                    }else {
                        layer.msg("已取消关注用户！！");
                    }
                    search();
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
                        'id':data.aid,
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