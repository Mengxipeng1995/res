var sysUrl="http://"+window.location.host+$ctx+"/"
var interfaceGridStore=Ext.create('Ext.data.Store', {
	fields:['id','src','caption'],
    data: [
        {src:sysUrl+'statistics/getPieData', info:'统计' },
        {src:sysUrl+'item/outline?page=1&start=0&limit=25', info:'分页获取条目'},
        {src:sysUrl+'category/getSons?id=3', info:'根据父节点ID获取子分类'},
        {src:sysUrl+'item/findById?id=5533', info:'根据条目ID获取条目'},
        {src:sysUrl+'photo/getImage?id=289', info:'根据图片ID获取图片'},
        {src:sysUrl+'apply/list?page=1&start=0&limit=25', info:'分页获取流程'},
        {src:sysUrl+'itemrel/outline?page=1&start=0&limit=20', info:'分页获取索引词'},
        {src:sysUrl+'category/save', info:'新增、修改分类'},
        {src:sysUrl+'item/listItemByVersion?resCode=358f9c29db024f2c9b49195ecca35686&page=1&start=0&limit=25', info:'根据资源码分页获取条目'},
    ]
});

var interfaceGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'接口管理',
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
			store: interfaceGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'),
			dockedItems:[],
			listeners:{
				cellclick ( grid , td , cellIndex , record , tr , rowIndex , e , eOpts ) {
					if(cellIndex==2){
						window.open(record.data.src);
					}
				}
				
			},
			columns : [
				{
				header:"序号",
				xtype : "rownumberer",
				align : 'center',
				hideable:false,
				width : '4%'
			},
				{
					header:"<div style='text-align:center'>URL</div>",
					dataIndex:"src",
					align : 'left',
					width : '40%',
					renderer:function(value){
						return "<a target='_blank' hred='"+value+"'>"+value+"</a>"
					}
			    },
			    {
					header:"<div style='text-align:center'>描述</div>",
					dataIndex:"info",
					align : 'left',
					width : '54%'
			    }
			   ],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:interfaceGridStore,
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
}]});