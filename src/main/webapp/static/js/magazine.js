//期刊表格数据
var magazineGridStore=Ext.create("Ext.data.Store",{
	fields:['magid','title','keywords','pageNums','publisherName','publisherAddress','pubDateStr'],
	proxy : {
		type : 'ajax',
		url : 'mcmc/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}

});

var updateMagazineCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'mcc/getGategoryWithMagid'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		name:'期刊分类',
		flag:true,
		id:'1'
	},
	fields: ['id','text'],
	nodeParam:'id',
	listeners:{
		load:function( store , records , successful , eOpts ) {
			Ext.getCmp('magazineEntryFormCategroy').expandAll();
	}
		
	}
});

//期刊分类树数据
var magazineCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'mcc/getSons'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		text:'期刊分类',
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
var updateMagazineCategoryPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	items: [
	{
	xtype:'form',
	width:350,
	id:"magazineCategoryMagageForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'magazineCategoryMagageTitle',
			title:'新增分类信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'分类名称<font color="red">*</font>',
					id:'magazineCategoryMagageFormName',
					name:'name',
					allowBlank: false,
					blankText: '分类名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'分类Id<font color="red">*</font>',
					id:'magazineCategoryMagageFormId',
					hidden:true,
					name:'id',
					blankText: '分类Id为必填项'
					
				},
				{
					xtype: "textfield",
					id:'magazineCategoryMagageFormResourceType',
					hidden:true,
					value:'1',
					name:'resourcesType'
					
				}
				,{
					xtype: "textfield",
					fieldLabel:'父分类Id<font color="red">*</font>',
					id:'magazineCategoryMagageFormParentId',
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
					var form=Ext.getCmp("magazineCategoryMagageForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/mcc/save',
        		                    success: function(form, action) {
        		                    updateMagazineCategoryPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '修改成功');
        		                       magazineCategoryTreeStore.proxy.extraParams={};
        		                       magazineCategoryTreeStore.load();
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
					updateMagazineCategoryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});


//期刊分类树
var magazineCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '期刊管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : magazineCategoryTreeStore,
    rootVisible: true,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   magazineGridStore.proxy.extraParams={cid:record.id};
	    	   magazineGridStore.loadPage(1);
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
					    	 Ext.getCmp('magazineCategoryMagageTitle').setTitle('修改分类信息');
					    	 var id = record.data.id; //节点id
					    	 Ext.getCmp('magazineCategoryMagageFormId').setValue(id);
					    	 Ext.getCmp('magazineCategoryMagageFormName').setValue(record.data.name);
					    	 Ext.getCmp('magazineCategoryMagageFormParentId').setValue(record.parentNode.data.id);
					    	 
					  	  
					    	 Ext.create('Ext.window.Window',
										{
											modal : true,
											height : 320,
										   	width : 380,
										   	region : 'center',
										   	autoWidth:true,
										   	layout: "border",
											items:[updateMagazineCategoryPanel],
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
	                        	 var form=Ext.getCmp("magazineCategoryMagageForm");
	                        	 form.reset();
	                        	 Ext.getCmp('magazineCategoryMagageTitle').setTitle('新增分类信息');
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('magazineCategoryMagageFormParentId').setValue(id);
	              	    	  
	                        	 Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 320,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[updateMagazineCategoryPanel],
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
											url : $ctx+"/mcc/delete",
											params : { "id" : id },
											disableCaching : false,
											success: function(resp,opts){  
												Ext.Msg.alert("提示：" ,"删除成功！");
												magazineCategoryTreeStore.proxy.extraParams={};
												magazineCategoryTreeStore.load();
												
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

//期刊实体面板
var updateMagazineEntryPanel=Ext.create('Ext.panel.Panel',{
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
	id:"magazineEntryForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'magazineEntryTitle',
			title:'新增期刊信息', 
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
					fieldLabel:'期刊名称<font color="red">*</font>',
					name:'title',
					allowBlank: false,
					blankText: '期刊名称为必填项'
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
        	        id:'magazineEntryFormCategroy',
        	        store : updateMagazineCategoryTreeStore,
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
    	    	            header: '授权',  
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
        	    					var category = updateMagazineCategoryTreeStore.getAt(rowIndex);
        	    					var rootid=updateMagazineCategoryTreeStore.getRoot().id;
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
					var form=Ext.getCmp("magazineEntryForm");
					var ids="";
					//遍历勾选的节点
					updateMagazineCategoryTreeStore.each(function(record){
						if(record.data.flag){
							ids+=record.id+";";
						}
					});
					form.getForm().findField('cid').setValue(ids);
					
					
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/magazine/save',
        		                    success: function(form, action) {
        		                    	updateMagazineEntryPanel.findParentByType("window").close();
        		                      // Ext.Msg.alert('提示', '操作成功');
        		                    	magazineGridStore.loadPage(1);
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
					updateMagazineEntryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});

//期刊查询结果显示
var magazineGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'专题列表',
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
			store: magazineGridStore,
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
								        text:'创建期刊',
								        listeners:{
								        	'click':function(){
								        		updateMagazineCategoryTreeStore.proxy.extraParams={
													    };
								        		updateMagazineCategoryTreeStore.load();

					                    		 var form=Ext.getCmp("bookEntryForm");
					                        	 form.reset();
					                    		 Ext.getCmp('magazineEntryTitle').setTitle('新增期刊信息');
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
					                    		
					                    		window.add(updateMagazineEntryPanel);
					                    	
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
						    	var record=magazineGridStore.getAt(rowIndex);

				        		updateMagazineCategoryTreeStore.proxy.extraParams={
				        				magid:record.data.magid
									    };
				        		updateMagazineCategoryTreeStore.load();

	                    		 var form=Ext.getCmp("magazineEntryForm");
	                        	 form.reset();
	                        	 
	                        	 form=form.getForm();
	                        	 form.findField('id').setValue(record.data.magid);
	                        	 form.findField('title').setValue(record.data.title);
	                        	 form.findField('keywords').setValue(record.data.keywords);
	                        	 form.findField('pageNums').setValue(record.data.pageNums);
	                        	 form.findField('publisherName').setValue(record.data.publisherName);
	                        	// form.findField('pubDate').setValue(record.data.pubDate);
	                        	 form.findField('publisherAddress').setValue(record.data.publisherAddress);
	                        	 
	                    		 Ext.getCmp('magazineEntryTitle').setTitle('修改期刊信息');
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
	                    		
	                    		window.add(updateMagazineEntryPanel);
	                    	
				        	
						    	
						    }
						},{},{
							icon: $ctx+'/static/images/btn/delete_0906.png',  // Use a URL in the icon config
						    tooltip: '删除',
						    handler: function(grid, rowIndex, colIndex) {
						    	var record=magazineGridStore.getAt(rowIndex);
						    	$.ajax({
									   url: "magazine/delete",
									   data: {id:record.data.magid},
									   async:false,
									   dataType: 'json',
									   success: function(data){
										   if(data.success){
											   magazineGridStore.loadPage(1);
											   
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
				store:magazineGridStore,
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


var magazineMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [magazineCategoryWestPanel, magazineGridPanel ]
});