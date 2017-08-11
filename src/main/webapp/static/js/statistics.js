//用户管理左侧菜单
var statisticsWestMenu = new Ext.Panel({
	     title:"功能列表",
	     region:"west",
	     height:200,
	     split:true,
	     collapsible:true,
	     width:120,
	     layout:{
	    	 	type: 'vbox',
	     },
	     items:[
	           {
 	    	   xtype:'button', 
               // id:'allUsersBtn', 
                text:'按库统计',
                width:'100%',
                id:'statisticsPieButton',
                listeners:{
                click:function(){
                	$.ajax({
						   url: "statistics/getPieData",
						   async:false,
						   dataType: 'json',
						   success: function(msg){
							   // 基于准备好的dom，初始化echarts实例
							   $("#statisticsDiv").html("");
						        var myChart = echarts.init(document.getElementById('statisticsDiv'));

						        // 指定图表的配置项和数据
						        var  option = {
						        		
						    title : {
						        text: '资源库整体统计',
						        subtext: '按库统计',
						        x:'center'
						    },
						    tooltip : {
						        trigger: 'item',
						        formatter: "{a} <br/>{b} : {c} ({d}%)"
						    },
						    legend: {
						        orient: 'vertical',
						        left: 'left',
						        data: msg.items
						    },
						    series : [
						        {
						            name: '稿件量',
						            type: 'pie',
						            radius : '55%',
						            left:'center',
						            center: ['50%', '60%'],
						            data:msg.data,
						            itemStyle: {
						                emphasis: {
						                    shadowBlur: 10,
						                    shadowOffsetX: 0,
						                    shadowColor: 'rgba(0, 0, 0, 0.5)'
						                }
						            }
						        }
						    ]
						};
						        myChart.setOption(option);
							  
						   }
						});
                }
	           }
 	       }
 	       
 	      
	     ]
});
var statisticsCenPanel=new Ext.Panel({
   	height : "85%",
   	width : "100%",
   	region : 'center',
   	autoWidth:true,
   	layout: "border",
   	html:"<div id='statisticsDiv' style='width:1000px;height:500px'></div>"
});

var statisticsMainPanel=new Ext.Panel({
	height : "85%",
	width : "100%",
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [ statisticsWestMenu, statisticsCenPanel ]
});