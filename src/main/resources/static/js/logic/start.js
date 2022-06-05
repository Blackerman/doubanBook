layui.use(['jquery','form','table','laydate','layer','carousel'],function () {
    var $ = layui.jquery;
    var table = layui.table;
    var laydate = layui.laydate;
    var form = layui.form;
    var layer = layui.layer;
    var carousel = layui.carousel;
    //轮播图
    carousel.render({
        elem: '#test1'
        ,width: '100%' //设置容器宽度
        ,arrow: 'always' //始终显示箭头
        //,anim: 'updown' //切换动画方式
    });

    //轮播图实现
    $.post('/douban/statistics/carousel',
        {},
        function (result){
            var urls=[];
            for(i = 0; i<5;i++){
                var url =  '\''+  result[i].bookPicture+ '?' + Math.random() +'\'';
                var str =  '<a href="../html/book/bookManage.html" >' +  '<img  src='+ url +'></a>'
                urls.push(str);
            }

            $('#item1').html(urls[0]);
            $('#item2').html(urls[1]);
            $('#item3').html(urls[2]);
            $('#item4').html(urls[3]);
            $('#item5').html(urls[4]);

            $('#item1').click(function(event) {
                $.post('/douban/statistics/clickURL', {
                    'str': result[0].bookName,
                }, function (result) {
                })
            })

            $('#item2').click(function(event) {
                $.post('/douban/statistics/clickURL', {
                    'str': result[1].bookName,
                }, function (result) {
                })
            })

            $('#item3').click(function(event) {
                $.post('/douban/statistics/clickURL', {
                    'str': result[2].bookName,
                }, function (result) {
                })
            })

            $('#item4').click(function(event) {
                $.post('/douban/statistics/clickURL', {
                    'str': result[3].bookName,
                }, function (result) {
                })
            })

            $('#item5').click(function(event) {
                $.post('/douban/statistics/clickURL', {
                    'str': result[4].bookName,
                }, function (result) {
                })
            })

        })

    //个人信息统计
    $.post('/douban/statistics/personal',
        {
        },
        function (result) {
            $('#adminName').html(result.name);
            $('#myBook').html(result.collect);
            $('#myArticle').html(result.userArticle);
            $('#myLove').html(result.userLove);
            $('#myLoved').html(result.userLoved);
            $('#myMessage').html(result.message);
        }
    )

    //平台信息统计
    $.post('/douban/statistics/platform',
        {
        },
        function (result) {
            $('#sumBook').html(result[0].book);
            $('#sumArticle').html(result[0].article);
            $('#sumNotice').html(result[0].notice);
            $('#sumUser').html(result[0].user);
            $('#sumAdmin').html(result[0].admin);

            $('#todayBook').html(result[0].book);
            $('#todayArticle').html(result[0].article);
            $('#todayNotice').html(result[0].notice);
            $('#todayUser').html(result[0].user);
            $('#todayAdmin').html(result[0].admin);

            $('#yeBook').html(result[1].book);
            $('#yeArticle').html(result[1].article);
            $('#yeNotice').html(result[1].notice);
            $('#yeUser').html(result[1].user);
            $('#yeAdmin').html(result[1].admin);

            $('#weekBook').html(result[2].book);
            $('#weekArticle').html(result[2].article);
            $('#weekNotice').html(result[2].notice);
            $('#weekUser').html(result[2].user);
            $('#weekAdmin').html(result[2].admin);

            $('#monthBook').html(result[3].book);
            $('#monthArticle').html(result[3].article);
            $('#monthNotice').html(result[3].notice);
            $('#monthUser').html(result[3].user);
            $('#monthAdmin').html(result[3].admin);
        }
    )



    //折线图
    var data=[];
    var xData = [];
    var articleData = [];
    var noticeData = [];
    var userData = [];
    var adminData = [];

    $.post('/douban/statistics/line',{},
        function (result){
            data = result;
            for( i = 0; i<data.length;i++){
                let str = data[i].year + "." +data[i].month + "." + data[i].day
                xData.push(str);
                articleData.push(data[i].article);
                noticeData.push(data[i].notice);
                userData.push(data[i].user);
                adminData.push(data[i].admin);
                chart2();
            }
        })

    function chart2() {
        var myChart = echarts.init(document.getElementById('chart4'));


        var option = {
            tooltip: {trigger: 'axis',axisPointer: {lineStyle: {color: '#000'}}},
            legend: {
                icon: 'rect',
                itemWidth: 14,itemHeight: 5,itemGap:10,
                data: ['文章', '公告', '用户','管理员'],
                right: '10px',top: '0px',
                textStyle: {fontSize: 12,color: '#000'}
            },
            grid: {x:40,y:50,x2:45,y2:40},
            xAxis: [{
                type: 'category',boundaryGap: false,axisLine: {lineStyle: {color: '#57617B'}},axisLabel: {textStyle: {color:'#000'}},
                data:xData,
            }],
            yAxis: [{
                type: 'value',
                axisTick: {
                    show: false
                },
                axisLine: {lineStyle: {color: '#57617B'}},
                axisLabel: {margin: 10,textStyle: {fontSize: 12},textStyle: {color:'#000'},formatter:'{value}个'},
                splitLine: {lineStyle: {color: '#57617B'}}
            },{
                type: 'value',
                axisTick: {
                    show: false
                },
                axisLine: {lineStyle: {color: '#57617B'}},
                axisLabel: {margin: 10,textStyle: {fontSize: 12},textStyle: {color:'#000'},formatter:'{value}名'},
                splitLine: {show: false,lineStyle: {color: '#57617B'}}
            }],
            // yAxis:[{type:'value'},{type: 'value'}],
            series: [{
                name: '文章',type: 'line',smooth: true,lineStyle: {normal: {width: 2}},
                yAxisIndex:0,
                areaStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(185,150,248,0.3)'
                        }, {
                            offset: 0.8,
                            color: 'rgba(185,150,248,0)'
                        }], false),
                        shadowColor: 'rgba(0, 0, 0, 0.1)',
                        shadowBlur: 10
                    }
                },
                itemStyle: {normal: { color: '#B996F8'}},
                data: articleData
            }, {
                name: '公告',type: 'line',smooth: true,lineStyle: { normal: {width: 2}},
                yAxisIndex:0,
                areaStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(3, 194, 236, 0.3)'
                        }, {
                            offset: 0.8,
                            color: 'rgba(3, 194, 236, 0)'
                        }], false),
                        shadowColor: 'rgba(0, 0, 0, 0.1)',
                        shadowBlur: 10
                    }
                },
                itemStyle: {normal: {color: '#03C2EC'}},
                data: noticeData
            }, {
                name: '用户',type: 'line',smooth: true,lineStyle: {normal: {width: 2}},
                yAxisIndex:1,
                areaStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(218, 57, 20, 0.3)'
                        }, {
                            offset: 0.8,
                            color: 'rgba(218, 57, 20, 0)'
                        }], false),
                        shadowColor: 'rgba(0, 0, 0, 0.1)',
                        shadowBlur: 10
                    }
                },
                itemStyle: {normal: {color: '#DA3914'}},
                data:userData
            },{
                name: '管理员',type: 'line',smooth: true,lineStyle: {normal: {width: 2}},
                yAxisIndex:1,
                areaStyle: {
                    normal: {
                        color: new echarts.graphic.LinearGradient(0, 0, 0, 1, [{
                            offset: 0,
                            color: 'rgba(232, 190, 49, 0.3)'
                        }, {
                            offset: 0.8,
                            color: 'rgba(232, 190, 49, 0)'
                        }], false),
                        shadowColor: 'rgba(0, 0, 0, 0.1)',
                        shadowBlur: 10
                    }
                },
                itemStyle: {normal: {color: '#E8BE31'}},

                data:adminData
            }]


        };
        /*var myChart = echarts.init(document.getElementById('channel_handle_detail'));
        myChart.clear();
        if(data.handleTimeData.length>0){
            myChart.setOption(option);
        }else{
            noDataTip($("#channel_handle_detail"));
        }*/
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
        window.addEventListener("resize",function(){
            myChart.resize();
        });
    }



    //点击跳转链接
    $('#top1').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top1").text(),
        }, function (result) {
        })
    })
    $('#top2').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top2").text(),
        }, function (result) {
        })
    })
    $('#top3').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top3").text(),
        }, function (result) {
        })
    })
    $('#top4').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top4").text(),
        }, function (result) {
        })
    })
    $('#top5').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top5").text(),
        }, function (result) {
        })
    })
    $('#top6').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top6").text(),
        }, function (result) {
        })
    })
    $('#top7').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top7").text(),
        }, function (result) {
        })
    })
    $('#top8').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top8").text(),
        }, function (result) {
        })
    })
    $('#top9').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top9").text(),
        }, function (result) {
        })
    })
    $('#top10').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top10").text(),
        }, function (result) {
        })
    })
    $('#top11').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top11").text(),
        }, function (result) {
        })
    })
    $('#top12').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top12").text(),
        }, function (result) {
        })
    })
    $('#top13').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top13").text(),
        }, function (result) {
        })
    })
    $('#top14').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top14").text(),
        }, function (result) {
        })
    })
    $('#top15').click(function(event) {
        $.post('/douban/statistics/clickURL', {
            'str': $("#top15").text(),
        }, function (result) {
        })
    })




})