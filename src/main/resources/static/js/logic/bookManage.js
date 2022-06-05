layui.use(['jquery','form','table'],function (){
    var $ =layui.jquery,
        form = layui.form,
        table = layui.table;



    function imageLoad(url){
        return  url + '?' + Math.random()
    }


    table.render({
        elem:'#tblResult',
        page:true,
        limit:10,

        cols:[[
            // {
            //     field: 'bookCollect', title: '收藏', align: 'center', width: '50',
            //     templet: function (rowData) {
            //         var btnCollect = '<button class="layui-btn layui-btn-sm layui-btn" lay-event="collect">' +
            //             '<i class="layui-icon layui-icon-about"></i>收藏</button>';
            //         return btnCollect;
            //     }
            // },
            {field:'bookName', title:'图书名称', align:'center',edit:'text'},
            {field:'bookAuthor', title:'作者', align:'center',edit:'text'},
            {field:'bookScore', title:'评分', align:'center'},
            {field:'bookScoreNum', title:'评价人数', align:'center'},
            {field:'bookQuote', title:'引言', align:'center'},
            {field:'bookDTags', title:'标签', align:'center',edit:'text'},
            {field:'focus', title:'操作', align:'center',width:'350',
                templet:function(rowData) {
                    var btnInfo = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="info">'+
                        '<i class="layui-icon layui-icon-picture"></i>图片</button>';
                    var btnArticle = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="article">'+
                        '<i class="layui-icon layui-icon-form"></i>文章</button>';
                    var btnReset = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="edit">'+
                        '<i class="layui-icon layui-icon-edit"></i>评分</button>';
                    // var btnCollect = '<button class="layui-btn layui-btn-sm layui-btn" lay-event="collect">'+
                    //     '<i class="layui-icon layui-icon-about"></i>收藏</button>';

                    var btnCollect;
                    if(rowData.focus){
                        btnCollect = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="collect">'+
                            '<i class="layui-icon layui-icon-star-fill"></i>收藏</button>'
                    }else{
                        btnCollect = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="collect">'+
                            '<i class="layui-icon layui-icon-star"></i>收藏</button>'
                    }
                    return btnInfo+btnArticle+btnReset+btnCollect;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[]

    });




    /**
     * 页面上的检索按钮的绑定，单击激活检索事件
     * 1.向指定的服务器接口（url的属性链接）以指定的方法（Post/GET）发送页面收集的数据（where属性中的JSON数据）
     * 2.并将返回的查询结果填充至指定的LayUI表格控件
     */

    var search = function (){
        table.reload('tblResult',{
            url:'/logic/book/search',
            method:'post',
            where:{
                'bookName': $('#book_name').val(),
                'bookAuthor':$('#book_author').val(),
                'bookDTags': $('#book_d_tags').val(),
                'bookHotTags': $('#book_d_hotTag').val(),
                'bookHotAuthor': $('#book_d_hotauthor').val()

            },
            skin:'line',
            even:true,
        })
        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐
    }
    search();
    $('#btnSearch').click(function(event){

        search()
    })

    table.on('edit(tblResult)', function(obj){
        var value = obj.value //得到修改后的值
        var data = obj.data; //得到所在行所有键值
        var field = obj.field;//得到相应属性
        var name = obj.data.bookName;

        if(field === 'bookName'){
            // layer.msg(name + '更改名称为：'+ value);
            $.post('/logic/book/update',
                {
                    'id':obj.data.id,
                    'bookName':obj.value
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
        if(field === 'bookAuthor'){
            // layer.msg(name + '更改名称为：'+ value);
            $.post('/logic/book/updateScore',
                {
                    'id':obj.data.id,
                    'bookAuthor':obj.value
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


        // else if(field === 'bookAuthor')
        //     layer.msg(name + '更改作者名称为：'+ value);
        // else if(field === 'bookDTags')
        //     layer.msg(name + '更改标签为：'+ value);

    });

    table.on('tool(tblResult)', function (obj) {
        var data = obj.data;//修改的当行数据
        if('info'===obj.event){

            //破解防盗链，https://www.jb51.net/article/203101.htm
            var url =  '\''+  data.bookPicture + '?' + Math.random() +'\'';
            layer.open({
                title: data.bookName,
                offset: 't'
                ,content: '<img  layer-src='+url +'src='+url +'alt="图片名">'
            });
            // showImg(data.bookPicture);
        }
        if('article'===obj.event){
            $('#addMyArticleTags').html(data.bookName+","+data.bookAuthor);
            layer.open({
                type: 1
                , offset: 'auto'
                , content: $('#divMyArticleAdd')
                , title: data.bookName
                , btn: ['增加', '取消']
                , btnAlign: 'c'
                , shade: 0
                , anim: 5
                , yes: function (index, lo) {
                    $.post('/douban/info/add',
                        {
                            'name':  $('#addMyArticleName').val(),
                            'tags': data.bookName+","+data.bookAuthor,
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
                        }
                    );

                }
            });
        }
        if ('edit'===obj.event){
            // alert("jlkajdsf");
            $.post('/logic/book/'+obj.data.id,
                {},
                function (result) {
                    $('#name').html(obj.data.bookName);
                    $('#author').html(obj.data.bookAuthor);
                    $('#info').html(obj.data.bookQuote);
                    layer.open({
                        type:1
                        ,offset:'auto'
                        ,content:$('#divBookInfo')
                        ,btn:['改分','取消']
                        ,btnAlign:'c'
                        ,shade:0
                        ,anim: 5
                        ,yes:function (index,lo) {
                            $.post('/logic/book/updateScore',
                                {
                                    'id':obj.data.id,
                                    'bookScore':$('#score').val()
                                },
                                function (result) {
                                    layer.msg("success!！");
                                    if (result!=null){
                                        layer.msg("修改成功！");
                                        layer.close(index);
                                    }else {
                                        layer.msg("您输入的内容有错误")
                                    }
                                    search();
                                }

                            );
                        }
                    });
                });
        }
        if ('collect'===obj.event){
            $.post('/douban/collection/addCollection',
                {
                    'id':obj.data.id,
                    'focus':obj.data.focus
                },
                function (result) {
                    layer.msg(result);
                    search();
                }
            )
        }



    });


});
