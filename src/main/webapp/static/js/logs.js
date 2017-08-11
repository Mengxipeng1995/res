var logsGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','message','logDate','objectid','objectName','operator','priority','message'],
	pageSize : 20,
	proxy : {
		type : 'ajax',
		url : 'syslogs/search',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
});

//词条
var logsGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'日志列表',
    	width:'85%',
    	height:'100%',
    	autoWidth:true,
    	region : 'center',
    	layout:{
    		type : "border"
    	},
    	autoScroll:true,
    	dockedItems:[
 		{
 			xtype : 'toolbar',
 			enableOverflow:true,
 			items :[]
 		}
    	],
    	
    
		items:[{
			xtype:'grid',
			layout : 'fit',
			region : 'center',
			height:'100%',
			widht:'100%',
			autoWidth:true,
			columnLines:true,
			store: logsGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'), 
			columns : [
				
				{
					header:"ID",
					dataIndex:"id",
					align : 'center',
					width : '5%'
				},
				{
					header:"操作者",
					dataIndex:"operator",
					align : 'center',
					width : '10%'
				},
			    {
					header:"信息",
					dataIndex:"message",
					align : 'center',
					width : '37%'
			    },
			    {
					header:"操作对象",
					dataIndex:"objectName",
					align : 'center',
					width : '24%'
			    },
				{
					header:"日志级别",
					dataIndex:"priority",
					align : 'center'
				},{
					header:"时间",
					dataIndex:"logDate",
					align : 'center',
					width : '14%'
			    }
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:logsGridStore,
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