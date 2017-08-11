var sysParGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','proNmae','proValue','desc'],
	autoLoad: true,
	proxy : {
		type : 'ajax',
		url : 'syspro/get',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
	
//	data:[
//		{'title':'磁盘类型','value':'NAS','desc':'硬盘存储'}
//		
//	]
});

//参数列表
var sysParGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'系统配置',
    	width:'85%',
    	height:'100%',
    	autoWidth:true,
    	region : 'center',
    	layout:{
    		type : "border"
    	},
    	autoScroll:true,
		items:[{
			xtype:'grid',
			layout : 'fit',
			region : 'center',
			height:'100%',
			widht:'100%',
			autoWidth:true,
			columnLines:true,
			store: sysParGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'), 
			columns : [
				{
					header:"序号",
					xtype : "rownumberer",
					align : 'center',
					hideable:false,
					width : '4%'
				},
				{
					header:"参数名",
					dataIndex:"proNmae",
					align : 'center',
					width : '30%'
				},
			    {
					header:"参数值",
					dataIndex:"proValue",
					align : 'center',
					width : '30%'
			    },
			    {
					header:"描述",
					dataIndex:"desc",
					align : 'center',
					width : '34%'
			    }
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:sysParGridStore,
				displayInfo : true,
				width : '100%',
				height : 30,
				anchorSize : 100,
				displayMsg : '显示 {0} - {1} 条，共计 {2} 条',
				beforePageText : '第',
				afterPageText : '页-共{0}页',
				emptyMsg : "没有内容可以显示",
				nextText : '',
				prevText : '',
				firstText : '',
				lastText : ''
		    } 
		]
});