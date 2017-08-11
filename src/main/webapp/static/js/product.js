//产品表格数据
var productGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','name','createrNickName','status','createDate'],
	proxy : {
		type : 'ajax',
		url : 'product/list',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
	

});
/**
 * 混合产品条目数据源
 */
var mixProductItemGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','title','productid','itemKeywords'],
	pageSize:20,
	proxy : {
		type : 'ajax',
		url : 'productItem/list',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	},
	listeners:{
		'load':function(){
			
		}
	}
	

});


var productItemGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','title','productid','itemKeywords'],
	pageSize:20,
	proxy : {
		type : 'ajax',
		url : 'rcmc/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	},
	listeners:{
		'load':function(){
			
		}
	}
	

});
//产品分类树数据
var productCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'category/getSons'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		id:'1'
	},
	fields: ['id','text'],
	nodeParam:'id',
	listeners:{
		load:function( store , records , successful , eOpts ) {
			if(successful){
				Ext.Array.each(records, function(record, index, countriesItSelf) {
					record.data.text=record.data.name;
					record.commit();
				});


			}
	}
		
	}
});

//产品分类树
var productCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '产品分类',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : productCategoryTreeStore,
    rootVisible: true,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   productItemGridStore.proxy.extraParams.catid=record.id;
				productItemGridStore.loadPage(1);
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
/**
 * 混合产品表格
 */
var mixProductItemGridPanel=Ext.create('Ext.panel.Panel',{
    	width:'85%',
    	height:'100%',
    	autoWidth:true,
    	region : 'center',
    	bodyStyle: {
    		padding:'30px',
    		background:'#fff'
    	},
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
    		store: mixProductItemGridStore,
    		columns : [
    			
    			{
    			header:"序号",
    			xtype : "rownumberer",
    			align : 'center',
    			hideable:false,
    			width : '4%'
    		},
    			{
    				header:"<div style='text-align:center'>标题</div>",
    				dataIndex:"itemTitle",
    				align : 'left',
    				width : '27.5%'
    		    },
    		    {
    				header:"创建者",
    				dataIndex:"createrNickName",
    				align : 'center',
    				width : '20%'
    		    },
    		   {
    				header:"状态",
    				dataIndex:"status",
    				align : 'center',
    				width : '20%',
    				renderer:function(value){
    					if(value=='1'){
    						return '已发布';
    					}else{
    						return '编辑';
    					}
    				}
    		    },
    		    {
    				header:"缩略图",
    				dataIndex:"type",
    				align : 'center',
    				width : '20%',
    				renderer:function(value,metaData,record){
    					var str='';
    					switch (value) {
						case 0:
							//图片
							str='文字稿'
							break;
						case 1:
							//图片
							str='<img src="photo/getSmallImage?id='+record.data.itemid+'"/>'
							break;
						case 2:
							//图片
							str='<img src="video/getVideoPic/'+record.data.itemid+'/2" />'
							break;
							
						}
    					
    					return str;
    				}
    		    }
    		]
    	}],
    	bbar : [
    	    {
    			xtype : 'pagingtoolbar',
    			store:mixProductItemGridStore,
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
/**
 * 根据分类树创建的条目表格
 */
var productItemGridPanel=Ext.create('Ext.panel.Panel',{
	title:'产品列表',
	//width:'40%',
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
		store: productItemGridStore,
		columns : [
			
			{
			header:"序号",
			xtype : "rownumberer",
			align : 'center',
			hideable:false,
			width : '4%'
		},
			{
				header:"<div style='text-align:center'>标题</div>",
				dataIndex:"itemTitle",
				align : 'left',
				width : '27.5%'
		    },
		    {
				header:"创建者",
				dataIndex:"createrNickName",
				align : 'center',
				width : '20%'
		    },
		   {
				header:"状态",
				dataIndex:"status",
				align : 'center',
				width : '20%',
				renderer:function(value){
					if(value=='1'){
						return '已发布';
					}else{
						return '编辑';
					}
				}
		    },
		    {
				header:"创建时间",
				dataIndex:"createDate",
				align : 'center',
				width : '20%'
		    }
		]
	}],
	bbar : [
	    {
			xtype : 'pagingtoolbar',
			store:productItemGridStore,
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
var disableProductPubFlag=!(productPubPermission||$userName=='admin');
var productType=0;
//产品查询结果显示
var productGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'产品列表',
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
			store: productGridStore,
			dockedItems:[
							{
									xtype : 'toolbar',
									//hidden:true,
									enableOverflow:true,
									items :[
									        {
										        xtype: 'button',
										        text:'创建产品',
										        listeners:{
										        	'click':function(){
										        		 var form=Ext.getCmp("createItemProductForm");
							                        	 form.reset();
										        		//
										        		Ext.getCmp('createItemProductCatName').hide();
										        		Ext.getCmp('createItemProductType').setValue(3);
										        		Ext.create('Ext.window.Window',
																{
																	title : '产品列表',
																	autoScroll:false,
																	constrain : true,
																	modal : true,
																	width: 500,
																	height: 500,
																	//width : 1550,
																	layout : 'fit',
																	listeners:{
																		'close':function(win){
																			win.removeAll(false);
																			Ext.getCmp('createItemProductCatName').show();
															        		Ext.getCmp('createItemProductType').setValue(1);
																		}
																	},
																	items:[createItemProductPanel]
																}
										   					).show();
										        		
										        	}
										        }
										       
										    }
									        ]
							}
			             ],
			listeners : {
				'itemdblclick' : function(view,record, item, index, e,eOpts) {
					
					if(record.data.type!=3){
						productItemGridStore.proxy.extraParams={'catid':record.data.catid,'productid':record.data.id};
						productItemGridStore.loadPage(1);
						productType=record.data.type;
					productCategoryTreeStore.proxy.url="category/getSons?resourcesType="+record.data.type;
					productCategoryTreeStore.setConfig('root',{
						nodeType : 'async', 
						text : record.data.name,
						draggable : false,
						id : record.data.catid,
						expanded : true
				});
					productCategoryTreeStore.reload();
					
					Ext.create('Ext.window.Window',
							{
								title : '产品条目',
								autoScroll:false,
								constrain : true,
								modal : true,
								width: document.body.clientWidth * 0.9,
								height: document.body.clientHeight * 0.9,
								layout: "border",
								listeners:{
									'close':function(win){
										win.removeAll(false);
									}
								},
								items : [productCategoryWestPanel,productItemGridPanel]
							}
	   					).show();
					
					}else{
						productType=3;
						mixProductItemGridStore.proxy.url="productItem/list?productid="+record.data.id;
						mixProductItemGridStore.loadPage(1);
						Ext.create('Ext.window.Window',
								{
									title : '产品条目',
									autoScroll:false,
									constrain : true,
									modal : true,
									width: document.body.clientWidth * 0.84,
									height: document.body.clientHeight * 0.9,
									layout: "border",
									listeners:{
										'close':function(win){
											win.removeAll(false);
										}
									},
									items : [mixProductItemGridPanel]
								}
		   					).show();
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
					header:"<div style='text-align:center'>名字</div>",
					dataIndex:"name",
					align : 'left',
					width : '27.5%'
			    },
			    {
					header:"创建者",
					dataIndex:"createrNickName",
					align : 'center',
					width : '20%'
			    },
			   {
					header:"状态",
					dataIndex:"status",
					align : 'center',
					width : '20%',
					renderer:function(value){
						if(value=='1'){
							return '已发布';
						}else{
							return '编辑';
						}
					}
			    },
			    {
					header:"创建时间",
					dataIndex:"createDate",
					align : 'center',
					width : '20%'
			    },
			    {
	                xtype:'actioncolumn',
	                header:"操作",
	                align : 'center',
	                dataIndex:"status",
	                width:'8%',
	                getClass:function(v,m,r){
	                	console.info(r);
	                	return 'x-hidden';
	                },
	                items: [
							{
								icon: $ctx+'/static/images/btn/unokay.png',  // Use a URL in the icon config
							    tooltip: '删除',
							    getClass:function(v,m,r){
							    	if(r.data.status==1){
							    		//return 'x-hidden';   隐藏
							    		return this.disabledCls;
							    	}
				                	
				                },
							    handler: function(grid, rowIndex, colIndex) {
							    	Ext.MessageBox.confirm(   
							                "请确认"  
							               ,"确定删除吗？"  
							               ,function( button,text ){  
							                   if( button == 'yes'){
							                	   var record=productGridStore.getAt(rowIndex);
											    	$.ajax({
														   url: "product/delete",
														   data: {id:record.data.id},
														   async:false,
														   dataType: 'json',
														   success: function(msg){
															   Ext.Msg.alert('提示', '删除成功');
															   productGridStore.loadPage(1);
															   
														   }});
							                   }  
							               }   
							           );
							    	
							    	
							    	
							    }
							},{},{

								icon: $ctx+'/static/images/btn/okay.png',  // Use a URL in the icon config
							    tooltip: '发布',
							    getClass:function(v,m,r){
							    	if(r.data.status==1||!productPubPermission){
							    		//return 'x-hidden';   隐藏
							    		return this.disabledCls;
							    	}
				                	
				                },
							    handler: function(grid, rowIndex, colIndex) {
							    	var record=productGridStore.getAt(rowIndex);
							    	$.ajax({
										   url: "product/pubProduct",
										   data: {id:record.data.id},
										   async:false,
										   dataType: 'json',
										   success: function(msg){
											   Ext.Msg.alert('提示', '发布成功');
											   productGridStore.loadPage(1);
											   
										   }});
							    }
							
							}
	                        ]}
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:productGridStore,
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


var productMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [productGridPanel ]
});