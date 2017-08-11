//篇章表格数据
var chapterGridStore=Ext.create("Ext.data.Store",{
	fields:['chapterid','title','keywords','pageNums','publisherName','publisherAddress','pubDateStr'],
	proxy : {
		type : 'ajax',
		url : 'chapterCategoryMapping/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}

});
var updateChapterCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'chapterCategory/getGategoryWithChapterid'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		name:'篇章分类',
		flag:true,
		id:'1'
	},
	fields: ['id','text'],
	nodeParam:'id',
	listeners:{
		load:function( store , records , successful , eOpts ) {
			Ext.getCmp('chapterEntryFormCategroy').expandAll();
	}
		
	}
});

//篇章分类树数据
var chapterCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'chapterCategory/getSons'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		text:'篇章分类',
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

//篇章分类树
var chapterCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '篇章管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : chapterCategoryTreeStore,
    rootVisible: true,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   chapterGridStore.proxy.extraParams={cid:record.id};
	    	   chapterGridStore.loadPage(1);
	       },
	       'afteritemexpand':function(node){
	    	   //console.info(node);
	       },
	       'expand':function(){
	    	   console(this);
	       },
	       'beforerender':function(){
	    	  
	       },
	       'itemcontextmenu' : function(view,record,item,index,e,eOpts){
	        	 if(Ext.getCmp('rightClickMenu')!= null){
	        		 Ext.destroy(Ext.getCmp('rightClickMenu'));
	        	 }
	        	e.preventDefault();  
        e.stopEvent();
        var nodemenu = new Ext.menu.Menu({  
            floating:true,
            id:'rightClickMenu',
            align:'center',
            items:[
					{  
					    text:'修改',
					    icon : $ctx+'/static/images/btn/edit.gif',
					    handler:function(){
					    	
					    	 var form=Ext.getCmp("magazineCategoryMagageForm");
					    	 form.reset();
					    	 Ext.getCmp('chapterCategoryMagageTitle').setTitle('修改分类信息');
					    	 var id = record.data.id; //节点id
					    	 Ext.getCmp('chapterCategoryMagageFormId').setValue(id);
					    	 Ext.getCmp('chapterCategoryMagageFormName').setValue(record.data.name);
					    	 Ext.getCmp('chapterCategoryMagageFormParentId').setValue(record.parentNode.data.id);
					    	 
					  	  
					    	 Ext.create('Ext.window.Window',
										{
											modal : true,
											height : 320,
										   	width : 380,
										   	region : 'center',
										   	autoWidth:true,
										   	layout: "border",
											items:[updateChapterCategoryPanel],
											listeners:{
												'close':function(win){
													win.removeAll(false);
												}
											}
											
										}
										).show();
					    }  
					},
                {  
	                        text:'新增',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        	 var form=Ext.getCmp("chapterCategoryMagageForm");
	                        	 form.reset();
	                        	 Ext.getCmp('chapterCategoryMagageTitle').setTitle('新增分类信息');
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('chapterCategoryMagageFormParentId').setValue(id);
	              	    	  
	                        	 Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 320,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[updateChapterCategoryPanel],
												listeners:{
													'close':function(win){
														win.removeAll(false);
													}
												}
												
											}
					   					).show();
	                        }  
	                    },
	                    {  
	                        text:'删除',
	                        icon : $ctx+'/static/images/btn/delete_0906.png',
	                        handler:function(){
	                        	
	                            var tipstr = "是否删除？";											
								Ext.Msg.confirm("提示：",	tipstr,function(btn) {
									if (btn == "yes") {
										var id = record.data.id; //节点id
			                            Ext.Ajax.request({
											url : $ctx+"/chapterCategory/delete",
											params : { "id" : id },
											disableCaching : false,
											success: function(resp,opts){  
												Ext.Msg.alert("提示：" ,"删除成功！");
												chapterCategoryTreeStore.proxy.extraParams={};
												chapterCategoryTreeStore.load();
												
					                        },  
					                        failure: function(resp,opts){  
					                        	Ext.Msg.alert("提示：" ,"删除失败,其重试");
					                        }
										});
									}
								});
	                        }  
	                    },{
	                    	text:'关闭',
	                    	icon:$ctx+'/static/images/btn/close_0906.png',
	                    	handler:function(){
	                    		Ext.destroy(Ext.getCmp('rightClickMenu'));
	                    	}
	                    }
            ]  
              
        });  
        nodemenu.showAt(e.getXY());
	        
}
    }
});

//篇章实体面板
var updateChapterEntryPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	scrollable:true,
    listeners:{
	added:function(){
	}
},
   	items: [
	{
	xtype:'form',
	width:350,
	id:"chapterEntryForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'chapterEntryTitle',
			title:'新增篇章信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					hidden:true,
					name:'id'
				},
				{	
					xtype: "textfield",
					fieldLabel:'篇章名称<font color="red">*</font>',
					name:'title',
					allowBlank: false,
					blankText: '篇章名称为必填项'
				},
				{	
					xtype: "textfield",
					fieldLabel:'关键词',
					name:'keywords'
				},
				{	
					xtype: "numberfield",
					fieldLabel:'页数',
					name:'pageNums'
				},
				{	
					xtype: "textfield",
					fieldLabel:'出版单位',
					name:'publisherName'
				},
				{	
					xtype: "textfield",
					fieldLabel:'单位地址',
					name:'publisherAddress'
				},
				{	
					xtype: "textfield",
					fieldLabel:'出版时间',
					name:'pubDateStr',
					allowBlank:true
				}
				,{
					xtype: "textfield",
					hidden:true,
					name:'cid'
					
				},{
					xtype:'treepanel',
					split: true, //显示分隔条
        	        autoScroll:true,
        	        rootVisible: true,
        	        id:'chapterEntryFormCategroy',
        	        store : updateChapterCategoryTreeStore,
        	        columns:[
    	    			{  
    	    			    xtype: 'treecolumn', //this is so we know which column will show the tree  
    	    			    text: '分类：',  
    	    			    flex: 2,  
    	    			    sortable: false, 
    	    			    width: "50", 
    	    			    dataIndex: 'name'
    	    			},{  
    	    	            xtype: 'checkcolumn',  
    	    	          //  header: '授权',  
    	    	            dataIndex: 'flag',  
    	    	            width: "50%", 
    	    	            renderer:function(value,cellmeta,record,rowIndex,columnIndex,stroe){
    	    	            	if(value==true){
    	    	            		changeAuthorizeItems(record.id,true);
    	    	            	}else{
    	    	            		changeAuthorizeItems(record.id,false);
    	    	            	}
    	    	            	return (new Ext.grid.column.CheckColumn).renderer(value);
    	    	            	
    	    	            	
    	    	            },
    	    	            stopSelection: false,
    	    	            listeners:{
        	    				checkchange:function(item, rowIndex, checked, eOpts){
        	    					var category = updateChapterCategoryTreeStore.getAt(rowIndex);
        	    					var rootid=updateChapterCategoryTreeStore.getRoot().id;
        	    					if(checked){
        	    						category.set('flag',true);
        	    						if(category.id!=rootid)
        	    						cascadeUpdateTree(category,true);
        	    					}else{
        	    						//根节点必须勾选
        	    						
        	    						if(category.id==rootid){
        	    							//Ext.Msg.alert('提示', '根节点必须勾选');
        	    							category.set('flag',true);
        	    						}else{
        	    							category.set('flag',false);
        	    							cascadeUpdateTree(category,false);
        	    						}
        	    						
        	    					}
        	    					
        	    					category.commit();
        	    					
        	    				}
        	    			}
    	    	            }
    	    			]
				}
				
		        
				
			]
		}
	],
	buttons:[
		{
			text: '保存',
			listeners:{
				click:function(){
					var form=Ext.getCmp("chapterEntryForm");
					var ids="";
					//遍历勾选的节点
					updateChapterCategoryTreeStore.each(function(record){
						if(record.data.flag){
							ids+=record.id+";";
						}
					});
					form.getForm().findField('cid').setValue(ids);
					
					
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/chapter/save',
        		                    success: function(form, action) {
        		                    	updateChapterEntryPanel.findParentByType("window").close();
        		                      // Ext.Msg.alert('提示', '操作成功');
        		                    	chapterGridStore.loadPage(1);
        		                    },
        		                    failure: function(form, action) {
        		                        Ext.Msg.alert('提示', action.result.msg);
        		                    }
        		                });
					}else{
						 Ext.Msg.alert('提示', '表单信息有误,请核查');
					}
				}
			}
			
		},{
			text: '关闭',
			listeners:{
				click:function(){
					updateChapterEntryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});
var updateChapterCategoryPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	items: [
	{
	xtype:'form',
	width:350,
	id:"chapterCategoryMagageForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'chapterCategoryMagageTitle',
			title:'新增分类信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'分类名称<font color="red">*</font>',
					id:'chapterCategoryMagageFormName',
					name:'name',
					allowBlank: false,
					blankText: '分类名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'分类Id<font color="red">*</font>',
					id:'chapterCategoryMagageFormId',
					hidden:true,
					name:'id',
					blankText: '分类Id为必填项'
					
				},
				{
					xtype: "textfield",
					id:'chapterCategoryMagageFormResourceType',
					hidden:true,
					value:'1',
					name:'resourcesType'
					
				}
				,{
					xtype: "textfield",
					fieldLabel:'父分类Id<font color="red">*</font>',
					id:'chapterCategoryMagageFormParentId',
					hidden:true,
					name:'pid',
					allowBlank: false,
					blankText: '分类Id为必填项'
					
				}
				
		        
				
			]
		}
	],
	buttons:[
		{
			text: '保存',
			listeners:{
				click:function(){
					var form=Ext.getCmp("chapterCategoryMagageForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/chapterCategory/save',
        		                    success: function(form, action) {
        		                    updateChapterCategoryPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '修改成功');
        		                       chapterCategoryTreeStore.proxy.extraParams={};
        		                       chapterCategoryTreeStore.load();
        		                    },
        		                    failure: function(form, action) {
        		                        Ext.Msg.alert('提示', action.result.msg);
        		                    }
        		                });
					}else{
						 Ext.Msg.alert('提示', '表单信息有误,请核查');
					}
				}
			}
			
		},{
			text: '关闭',
			listeners:{
				click:function(){
					updateChapterCategoryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});
//篇章查询结果显示
var chapterGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'篇章列表',
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
			store: chapterGridStore,
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
								       
								    },{
								        xtype: 'button',
								        text:'创建篇章',
								        listeners:{
								        	'click':function(){
								        		updateChapterCategoryTreeStore.proxy.extraParams={
													    };
								        		updateChapterCategoryTreeStore.load();

					                    		 var form=Ext.getCmp("chapterEntryForm");
					                        	 form.reset();
					                    		 Ext.getCmp('chapterEntryTitle').setTitle('新增篇章信息');
					                    		var window=Ext.create('Ext.window.Window',
														{
															modal : true,
															height : 400,
														   	width : 380,
														   	region : 'center',
														   	autoWidth:true,
														   	layout: "border",
															items:[],
															listeners:{
																'close':function(win){
																	win.removeAll(false);
																}
															}
															
														}
								   					).show();
					                    		
					                    		window.add(updateChapterEntryPanel);
					                    	
								        	}
								        }
								       
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
					header:"<div style='text-align:center'>标题</div>",
					dataIndex:"title",
					align : 'left',
					width : '21.5%'
			    },
			    {
					header:"<div style='text-align:center'>关键字</div>",
					dataIndex:"keywords",
					align : 'left',
					width : '20%'
			    },
			    {
					header:"页数",
					dataIndex:"pageNums",
					align : 'center',
					width : '7%'
			    },
			    {
					header:"出版时间",
					dataIndex:"pubDateStr",
					align : 'center',
					width : '7%'
			    },
			   {
					header:"出版单位",
					dataIndex:"publisherName",
					align : 'center',
					width : '15%'
			    },
			    {
					header:"<div style='text-align:center'>单位地址</div>",
					dataIndex:"publisherAddress",
					align : 'left',
					width : '17%'
			    },{
	                xtype:'actioncolumn',
	                header:"操作",
	                align : 'center',
	                width:'8%',
	                items: [
	                	{
							icon: $ctx+'/static/images/btn/edit.gif',  // Use a URL in the icon config
						    tooltip: '修改',
						    handler: function(grid, rowIndex, colIndex) {
						    	var record=chapterGridStore.getAt(rowIndex);

				        		updateChapterCategoryTreeStore.proxy.extraParams={
				        				chapterid:record.data.chapterid
									    };
				        		updateChapterCategoryTreeStore.load();

	                    		 var form=Ext.getCmp("chapterEntryForm");
	                        	 form.reset();
	                        	 
	                        	 form=form.getForm();
	                        	 form.findField('id').setValue(record.data.chapterid);
	                        	 form.findField('title').setValue(record.data.title);
	                        	 form.findField('keywords').setValue(record.data.keywords);
	                        	 form.findField('pageNums').setValue(record.data.pageNums);
	                        	 form.findField('publisherName').setValue(record.data.publisherName);
	                        	// form.findField('pubDate').setValue(record.data.pubDate);
	                        	 form.findField('publisherAddress').setValue(record.data.publisherAddress);
	                        	 
	                    		 Ext.getCmp('chapterEntryTitle').setTitle('修改期刊信息');
	                    		var window=Ext.create('Ext.window.Window',
										{
											modal : true,
											height : 400,
										   	width : 380,
										   	region : 'center',
										   	autoWidth:true,
										   	layout: "border",
											items:[],
											listeners:{
												'close':function(win){
													win.removeAll(false);
												}
											}
											
										}
				   					).show();
	                    		
	                    		window.add(updateChapterEntryPanel);
	                    	
				        	
						    	
						    }
						},{},{
							icon: $ctx+'/static/images/btn/delete_0906.png',  // Use a URL in the icon config
						    tooltip: '删除',
						    handler: function(grid, rowIndex, colIndex) {
						    	var record=chapterGridStore.getAt(rowIndex);
						    	$.ajax({
									   url: "chapter/delete",
									   data: {id:record.data.chapterid},
									   async:false,
									   dataType: 'json',
									   success: function(data){
										   if(data.success){
											   chapterGridStore.loadPage(1);
											   
										   }else{
											   Ext.Msg.alert('提示', data.msg);
										   }
										   
									   }});
						    }
						}
	                ]
			    
			    }
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:chapterGridStore,
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


var chapterMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [chapterCategoryWestPanel, chapterGridPanel ]
});