/**
 * 标准库
 */
var standardStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','tittle','createDate','createrUserName	','createrUserNickName'],
	pageSize : 20,
	proxy : {
		type : 'ajax',
		url : 'standard/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
});

//词条
var standardGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'标准列表',
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
			store: standardStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'), 
			columns : [
				
				{
					header:"ID",
					dataIndex:"id",
					align : 'center',
					width : '5%'
				},
				{
					header:"标题",
					dataIndex:"title",
					align : 'center',
					width : '50%'
				},
			    {
					header:"创建账号",
					dataIndex:"createrUserName",
					align : 'center',
					width : '15%'
			    },
			    {
					header:"创建昵称",
					dataIndex:"createrUserNickName",
					align : 'center',
					width : '15%'
			    },
				{
					header:"创建时间",
					dataIndex:"createDate",
					align : 'center',
					width : '13%'
				}
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:standardStore,
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