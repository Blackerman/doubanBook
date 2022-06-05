layui.use(['layer','jquery','form','table'],function () {

    var $ = layui.jquery,
        form = layui.form,
        layer = layui.layer,
        table = layui.table;


    table.render({
        elem:'#tblResult1',
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
            {field:'bookQuote', title:'quote', align:'center'},
            // {field:'bookDTags', title:'标签', align:'center',edit:'text'},
            {field:'focus', title:'操作', align:'center',width:'350',
                templet:function(rowData) {
                    var btnInfo = '<button class="layui-btn layui-btn-sm layui-btn-primary" lay-event="info">'+
                        '<i class="layui-icon layui-icon-picture"></i>图片</button>';
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
                    return btnInfo+btnReset+btnCollect;
                    // return btnInfo+btnReset;
                }
            }

        ]],
        data:[]

    });


    table.on('tool(tblResult1)', function (obj) {
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
                            $.post('/logic/book/update',
                                {
                                    'id':obj.data.id,
                                    'bookScore':$('#score').val()
                                },
                                function (result) {
                                    if (result!=null){
                                        layer.msg("修改成功！");
                                        layer.close(index);
                                    }else {
                                        layer.msg("您输入的内容有错误")
                                    }
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
                    'focus':data.focus
                },
                function (result) {
                    layer.msg(result);
                    search();
                }
            )
        }
    });


    var search = function (){
        table.reload('tblResult1',{
            url:'/douban/collection/search',
            method:'post'
            ,toolbar: '#toolbarDemo' //开启头部工具栏，并为其绑定左侧模板
            ,defaultToolbar: ['filter', 'exports', 'print', { //自定义头部工具栏右侧图标。如无需自定义，去除该参数即可
                title: '提示'
                ,layEvent: 'LAYTABLE_TIPS'
                ,icon: 'layui-icon-tips'
            }],
            where:{
                'bookName': $('#book_name').val(),
                'bookAuthor':$('#book_author').val(),
                'bookDTags': $('#book_d_tags').val(),
            },
            skin:'line',
            even:true
        })

        $('.layui-table-page').css('text-align','right');  // 数据表格分页右对齐
    }

    search();
    $('#btnSearch1').click(function (event) {

        search();

    });



});