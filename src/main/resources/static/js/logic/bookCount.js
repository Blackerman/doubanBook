layui.use(['jquery', 'layer'],function () {

    var $ = layui.$;
        layer = layui.layer;

    var myChart0 = echarts.init(document.getElementById('count0'));
    var myChart = echarts.init(document.getElementById('count3'));
    var myChart1 = echarts.init(document.getElementById('count4'));
    var myChart2 = echarts.init(document.getElementById('count1'));
    var myChart3 = echarts.init(document.getElementById('count2'));


    $.post(
        "/logic/book/count/scorecount",
        {},
        function (result) {


            var score =[];
            var count =[];
            var pieCount =[];

            for(i = 0; i <result.length; i++){
                score.push(result[i].score);
                count.push(result[i].count);
                // if (i <= 10) {
                    pieCount.push({'value':result[i].count,'name':result[i].score});
                // }
            }

            var option = {
                backgroundColor:'' ,//设置无背景色
                title:{
                    text:'图书全部分数统计-柱状图'
                },
                tooltip:{},
                legend:{
                    data:['评分']
                },
                xAxis:{
                    data:score
                },
                yAxis:{},
                series:[{
                    name:'评分',
                    type:'bar',
                    data:count
                }]


            };

            var option1={

                title:{
                    text:'图书全部分数统计-饼状图',
                    // subtext:'统计九分以上图书所占百分比',
                    left:'center',
                    top:20
                },
                tooltip:{
                    trigger:'item',
                    formatter:'{a} <br/>{b} : {c} ({d}%)'
                },

                visualMap:{
                    show:false,
                    min:80,
                    max:600,
                    inRange:{
                        colorLightness:[0,1]
                    }
                },
                series:[
                    {name: '评分',
                        type: 'pie',
                        radius:'65%',
                        clockWise : false,
                        center:['50%','50%'],
                        data:pieCount.sort(function (a,b) {return a.value-b.value;}),
                        roseType:'radius',
                        label:{
                            color:'rgba(0,0,0,0.6)'
                        },
                        labelLine:{
                            lineStyle:{
                                color:'rgba(0,0,0,0.6)'
                            },
                            smooth:0.2,
                            length:10,
                            length2:20
                        },
                        itemStyle:{
                            color:'#33bec2',
                            shadowBlur:100,
                            shadowColor:'rgba(0, 255, 0,1)'
                        },
                        animationType:'scale',
                        animationEasting:'elasticOut',
                        animationDelay:function (idx) {
                            return Math.random()*200;
                        }
                    }
                ]
            };

            myChart.setOption(option);
            myChart1.setOption(option1);



        }
    )
    $.post(
        "/logic/book/count/suncount",
        {},
        function(result){


            // var jsObject = eval('(' + result+ ')');
            // result = result.replace(/"/g, '\'');
            // var jsObject = eval('(' + result+ ')');
            var option = {
                backgroundColor:'' ,
                title:{
                    text:'图书数据分析-旭日图',
                    subtext:'图书评价分析，热门图书分析',
                    left:'15%',
                    top:30
                },
                // textStyle:{
                //     color:'#2647cc'
                // },
                legend:{
                    data:['图书评价','热门图书']
                },
                series: {
                    type: 'sunburst',
                    // highlightPolicy: 'ancestor',
                    data: result,
                    radius: [0, '90%'],
                    label: {
                        rotate: 'radial'
                    }
                }
            };
            myChart0.setOption(option);
        }



    )

    $.post(
        '/logic/book/count/rankcount',
        {},
        function (result) {
            var option = {
                title: {
                    text: '图书等级分析-漏斗图',
                    subtext: '对爬取的书籍的评分分析',
                    left:'15%',
                    top:'70%'
                },
                tooltip: {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c}%"
                },
                toolbox: {
                    feature: {
                        dataView: {readOnly: false},
                        restore: {},
                        saveAsImage: {}
                    }
                },
                legend: {
                    data: ['A级图书','B级图书','C级图书','D级图书','E级图书']
                },

                series: [
                    {
                        name:'漏斗图',
                        type:'funnel',
                        left: '10%',
                        top: 80,
                        //x2: 80,
                        bottom: 20,
                        width: '80%',
                        // height: {totalHeight} - y - y2,
                        min: 0,
                        max: 100,
                        minSize: '0%',
                        maxSize: '100%',
                        sort: 'descending',
                        gap: 2,
                        label: {
                            show: true,
                            position: 'inside'
                        },
                        labelLine: {
                            length: 10,
                            lineStyle: {
                                width: 1,
                                type: 'solid'
                            }
                        },
                        itemStyle: {
                            borderColor: '#fff',
                            borderWidth: 1
                        },
                        emphasis: {
                            label: {
                                fontSize: 20
                            }
                        },
                        data: [
                            {value: result[0], name: 'A级图书'},
                            {value: result[1], name: 'B级图书'},
                            {value: result[2], name: 'C级图书'},
                            {value: result[3], name: 'D级图书'},
                            {value: result[4], name: 'E级图书'}
                        ]
                    }
                ]
            };
            myChart2.setOption(option);
        }

    )

    $.post(
        '/logic/book/count/hotauthor',
        {},
        function (result) {
            var one = result[0],
                two = result[1],
                three=result[2],
            option = {

                legend: {},
                tooltip: {},
                dataset: {
                    dimensions: ['product', '五星', '四星', '三星','两星','一星'],
                    source: [
                        {product: one.hotAuthor, '五星': one.five/100, '四星': one.four/100, '三星': one.three/100,'两星':one.two/100,'一星':one.one/100},
                        {product: two.hotAuthor, '五星': two.five/100, '四星': two.four/100, '三星': two.three/100,'两星':two.two/100,'一星':two.one/100},
                        {product: three.hotAuthor, '五星': three.five/100, '四星': three.four/100, '三星': three.three/100,'两星':three.two/100,'一星':three.one/100},
                    ]
                },
                xAxis: {type: 'category'},
                yAxis: {},
                // Declare several bar series, each will be mapped
                // to a column of dataset.source by default.
                series: [
                    {type: 'bar'},
                    {type: 'bar'},
                    {type: 'bar'},
                    {type: 'bar'},
                    {type: 'bar'}
                ]
            };
            myChart3.setOption(option);
        }
    )




})