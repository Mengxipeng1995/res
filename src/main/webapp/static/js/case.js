//案例表格数据
var caseGridStore=Ext.create("Ext.data.Store",{
	
	fields:['name','pub_status'],
	data:[
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':0},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':0},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':0},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':0},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':1},
	      {'name':'叶片马达工作原理','pub_status':1}
	      ]
	

});

//案例分类树数据
var caseCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
    root: {
        expanded: true,
        text:'案例库',
        children: [
            { text: '机械工程基础理论', leaf: false },
            { text: '综合技术与管理',leaf: false },
            { text: '机械工程材料', leaf: false},
            { text: '机械设计基础', leaf: false},
            { text: '机械工艺与设备', leaf: false},
            { text: '控制与检测结束', leaf: false},
            { text: '器械设备', leaf: false},
            { text: '电工基础知识', leaf: false},
            { text: '电工设备', leaf: false},
            { text: '电力系统', leaf: false},
            { text: '电力应用', leaf: false},
            { text: '加工自动化', leaf: false},
            { text: '电器通讯', leaf: false}
        ]
    }
});

//案例分类树
var caseCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '案例管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : caseCategoryTreeStore,
    rootVisible: true,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   
	       },
	       'afteritemexpand':function(node){
	    	   //console.info(node);
	       },
	       'expand':function(){
	    	   console(this);
	       },
	       'beforerender':function(){
	    	  
	       }
    }
});

//案例查询结果显示
var caseGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'案例列表',
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
			store: caseGridStore,
			dockedItems:[
		    	      		{
		    	      			xtype : 'toolbar',
		    	      			//hidden:true,
		    	      			enableOverflow:true,
		    	      			items :[
		    	      			        {
								        xtype: 'textfield',
								        fieldLabel: '检索词',
								        labelWidth:50,
								        name: 'query'
								    }, {
								        xtype: 'button',
								        text:'查询'
								       
								    }
		    	      			   ]
		    	      		}
		    	      		],
			listeners : {
				'itemdblclick' : function(view,record, item, index, e,eOpts) {
//					fields:['id','itemid','name','pub_status'],
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
					header:"名字",
					dataIndex:"name",
					align : 'center',
					width : '45%'
			    },
			   {
					header:"发布状态",
					dataIndex:"pub_status",
					align : 'center',
					width : '42.5%',
					renderer:function(value){
						if(value=='1'){
							return '已发布';
						}else{
							return '未发布';
						}
					}
			    },{
	                xtype:'actioncolumn',
	                header:"操作",
	                align : 'center',
	                width:'8%',
	                items: [
							{
								icon: $ctx+'/static/images/btn/unokay.png',  // Use a URL in the icon config
							    tooltip: '删除',
							    handler: function(grid, rowIndex, colIndex) {}
							}
	                        ]}
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:caseGridStore,
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


var caseMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [caseCategoryWestPanel, caseGridPanel ]
});