//词条表格数据
var itemGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','title','etitle','keywords','resCode','versionDesc','content','versionCount','itemid'],
	proxy : {
		type : 'ajax',
		url : 'icmsc/findByCid',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
	

});



//词条根据资源码获取的数据
var itemVersionGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','title','etitle','bookTitle','keywords','versionDesc','content','versionCount','userName','userNickName','createDate'],
	proxy : {
		type : 'ajax',
		url : 'item/listItemByVersion',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
	

});

//词条分类树数据
var itemCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
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
					if(record.data.id==2){
						record.data.text='条目库';
					}else{
						record.data.text=record.data.name;
					}
					
					record.commit();
				});


			}
	}
		
	}
});


//词库分类树
var itemCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '词条管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : itemCategoryTreeStore,
    rootVisible: false,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   itemGridStore.proxy.url='icmsc/findByCid';
	    	   if(itemCategoryTreeStore.getRoot().childNodes[0].id==record.data.id){
	    		   itemGridStore.proxy.extraParams={};  
	    	   }else{
	    		   itemGridStore.proxy.extraParams={catid:record.data.id};
	    	   }
	    	   itemGridStore.load();
	    	   //
	    	   
	       },
	       'afteritemexpand':function(node){
	    	   //console.info(node);
	       },
	       'load':function( store , records , successful , operation , node , eOpts ) {
				if(successful&&node.id=='1'&&records.length>0){
					
					this.getRootNode().getChildAt(0).expand();
					
				}
				
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
	                        text:'创建产品',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        	 var form=Ext.getCmp("createItemProductForm");
	                        	 form.reset();
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('createItemProductCatId').setValue(id);
	                        	 Ext.getCmp('createItemProductCatName').setValue(record.data.text);
	                        	 Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 320,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[createItemProductPanel],
												listeners:{
													'close':function(win){
														win.removeAll(false);
													}
												}
												
											}
					   					).show();
	                        }  
	                    },{
	                    	 text:'创建词条',
		                     icon : $ctx+'/static/images/btn/add_0906.png',
		                     handler:function(){

  								var itemAddWindow=Ext.create("Ext.window.Window",{
				                    		autoWidth : true,
				                    		modal:true,
				                    		title:'创建词条',
				                    		resizable:false,
				                    		width: 800,
				                    		height:600,
				                    		constrain:true,
				                    		overflowY:'scroll',
				                    		overflowX:'hidden',
				                    		items:[
												{
												   	id:'creatBookPanelPut',
												   	xtype:'panel',
													width:'100%',
												   	height:'100%',
												   	region : 'center',
												   	autoWidth:true,
												   	autoScroll:true,
												   	items: [
														{
															xtype:'form',
															id:'creatItemForm',
															width:'100%',
															style:'border-width:0 0 0 0;margin:auto',
															autoScroll : true,
															buttonAlign: 'center',
															items:[
																{
																	xtype:'fieldset', 
																	title:'词条基本属性', 
																	collapsible:false,
																	width : '100%',
																	items:[
																		{	
																			xtype: "textfield",
																			name:'id',
																			hidden:true,
																			readOnly:true
																		},
																		{	
																			xtype: "textfield",
																			fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>标题',
																			labelAlign:'right',
																			labelWidth:66,
																			width:'100%',
																			name:'title',
																			allowBlank : false
																		},
																		{	
																			xtype: "textfield",
																			fieldLabel:'英文标题',
																			labelAlign:'right',
																			labelWidth:66,
																			width:'100%',
																			name:'etitle',
																			allowBlank : false
																		},
																		{	
																			xtype: "textfield",
																			fieldLabel:'版本说明',
																			labelAlign:'right',
																			labelWidth:66,
																			width:'100%',
																			name:'versionDesc'
																		},{	
																			xtype: "htmleditor",
																			fieldLabel:'正文',
																			width:'100%',
																			height:380,
																			fontFamilies: ["宋体", "隶书", "黑体"],
																			labelAlign:'right',
																			labelWidth:66,
																			name:'content'
																		},{	
																			xtype: "textfield",
																			fieldLabel:'分类',
																			hidden:true,
																			labelAlign:'right',
																			labelWidth:66,
																			width:'100%',
																			name:'catid',
																			value:record.data.id,
																			allowBlank : false
																		}
																	
																		
																		
																	]
																}
															
															],
															buttons:[{
															    
																text: '保存',
																
																handler:function(){
																	var form=Ext.getCmp("creatItemForm");
																	if (form.isValid()) {
																		 form.submit({
										                		                	url:$ctx+'/item/save',
										                		                    success: function(form, action) {
										                		                       Ext.Msg.alert('提示', '修改成功');
										                		                       itemGridStore.proxy.extraParams={catid:record.data.id};
										                		                       itemGridStore.load();
										                		                       itemAddWindow.close();
										                		                    },
										                		                    failure: function(form, action) {
										                		                        Ext.Msg.alert('提示', action.result.msg);
										                		                    }
										                		                });
																	}else{
																		 Ext.Msg.alert('提示', '表单信息有误,请核查');
																	}
																	
																	
																	
																	
																	
																}
															},{ 
																			text: '返回',
																			handler:function(){
																				
																				itemAddWindow.close();
																				//userCenPanel.removeAll(false);
																				//userCenPanel.add(allUsersPanel);
																			}
																		}
																	]
														
														}
												   	]
												}
				                    		       ],
				                        	listeners:{
				                        		'close':function(panel){
				                        			//panel.removeAll(false);	
				                        			
				                        		}
				                        	}
				                        }).show();
									
		                     }
	                    },
//	                    {
//	                    	text:'下载',
//	                    	icon:$ctx+'/static/images/btn/close_0906.png',
//	                    	handler:function(){
//	                    		var form=$("<form>");//定义一个form表单
//	                    		form.attr("style","display:none");
//	                    		form.attr("target","");
//	                    		form.attr("method","post");
//	                    		form.attr("action","item/download");
//	                    		var input1=$("<input>");
//	                    		input1.attr("name","catid");
//	                    		input1.attr("value",record.data.id);
//	                    		$("body").append(form);//将表单放置在web中
//	                    		form.append(input1);
//
//	                    		form.submit();//表单提交
//	                    	}
//	                    },
	                    {
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


//词库查询结果显示  'id','title','etitle','bookTitle','keywords','createDate'
var itemGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'词条列表',
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
			id:'itemGridPanel',
			region : 'center',
			height:'100%',
			widht:'100%',
			autoWidth:true,
			columnLines:true,
			store: itemGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'),
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
								        id:'itemSearchWord',
								        listeners:{
	    	     							'specialKey':function(tf,e){
	    	     								if (e.keyCode == 13) {
	    	     									Ext.getCmp('itemSearchButton').fireEvent('click');
	    	     							        }
	    	     							}
	    	     							
	    	     						}
								    }, {
								        xtype: 'button',
								        text:'查询',
								        id:'itemSearchButton',
								        listeners:{
								        	'click':function(){
								        		itemGridStore.proxy.url='item/search';
								 	    	   itemGridStore.proxy.extraParams={searchWord:Ext.getCmp("itemSearchWord").getValue()};
								 	    	  itemGridStore.loadPage(1);
								        	}
								        }
								       
								    },
								    {
								        xtype: 'button',
								        text:'添加到产品',
								        listeners:{
								        	'click':function(){
								        		mixProductGridStore.loadPage(1);
								        		Ext.getCmp("mixProductItemGrid").setValue("itemGridPanel");
								        		Ext.create('Ext.window.Window',
														{
															title : '产品列表',
															autoScroll:false,
															constrain : true,
															modal : true,
															width: document.body.clientWidth * 0.9,
															height: document.body.clientHeight * 0.9,
															//width : 1550,
															layout : 'fit',
															items:[mixProductGridPanel],
															listeners:{
																'close':function(win){
																	win.removeAll(false);
																}
															}
														}
								   					).show();
								        		
								        	}
								        }
								       
								    },{
								        xtype: 'button',
								        text:'添加到专题',
								        listeners:{
								        	'click':function(){
								        		subjectCommonGridStore.loadPage(1);
								        		Ext.getCmp('subjectCommonType').setValue(0)
								        		Ext.create('Ext.window.Window',
														{
															title : '专题列表',
															autoScroll:false,
															constrain : true,
															modal : true,
															width: document.body.clientWidth * 0.9,
															height: document.body.clientHeight * 0.9,
															//width : 1550,
															layout : 'fit',
															items:[subjectCommonGridPanel],
															listeners:{
																'close':function(win){
																	win.removeAll(false);
																}
															}
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
					
					$.ajax({
						   url: "item/findById",
						   data: {id:record.data.id},
						   async:false,
						   dataType: 'json',
						   success: function(msg){
							   if(!msg.content){
									Ext.Msg.alert('提示', '该条目没有正文');
									return;
								}
									Ext.create('Ext.window.Window',
										{
											title : '详细列表',
											autoScroll:true,
											constrain : true,
											modal : true,
											width: document.body.clientWidth * 0.9,
											height: document.body.clientHeight * 0.9,
											layout : 'fit',
											bodyCls:'itemDetail',
											html:msg.content
										}
				   					).show();
						   }});
					
					
				},
			'cellclick':function( panel , td , cellIndex , record , tr , rowIndex , e , eOpts){
				if(cellIndex==5){
					//单击查看该版本的
					//'userName','userNickName','createDate'
					itemVersionGridStore.proxy.extraParams={'resCode':record.data.resCode};
					itemVersionGridStore.load();
					Ext.create('Ext.window.Window', {
					    title: '条目列表',
					    height: 600,
					    width: 1200,
					    layout: 'fit',
					    items: [{
					    	xtype:'grid',
						    store:itemVersionGridStore,
						    columnLines:true,
						    columns: [

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
									width : '30%'
							    },
							    {
									header:"关键字",
									dataIndex:"keywords",
									align : 'center',
									width : '15%'
							    },
							    {
									header:"创建者",
									dataIndex:"userName",
									align : 'center',
									width : '15%',
									renderer:function(value){
										if(value){
											return value;
										}
										return "系统管理员";
									}
							    },{
									header:"创建时间",
									dataIndex:"createDate",
									align : 'center',
									width : '10%'
							    },
							    {
									header:"版本说明",
									dataIndex:"versionDesc",
									align : 'center',
									width : '15%'
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
											    	var record=itemVersionGridStore.getAt(rowIndex);
											    	$.ajax({
														   url: "item/findByIdForEdit",
														   data: {id:record.data.id},
														   async:false,
														   dataType: 'json',
														   success: function(data){
														    	var itemAddWindow=Ext.create("Ext.window.Window",{
									                    		autoWidth : true,
									                    		modal:true,
									                    		title:'修改条目',
									                    		resizable:false,
									                    		width: 800,
									                    		height:500,
									                    		constrain:true,
									                    		overflowY:'scroll',
									                    		overflowX:'hidden',
									                    		items:[
																	{
																	   	id:'creatBookPanelPut',
																	   	xtype:'panel',
																		width:'100%',
																	   	height:'100%',
																	   	region : 'center',
																	   	autoWidth:true,
																	   	autoScroll:true,
																	   	items: [
																			{
																				xtype:'form',
																				id:'creatItemForm',
																				width:'100%',
																				style:'border-width:0 0 0 0;margin:auto',
																				autoScroll : true,
																				buttonAlign: 'center',
																				items:[
																					{
																						xtype:'fieldset', 
																						title:'条目基本属性', 
																						collapsible:false,
																						width : '100%',
																						items:[
																							{	
																								xtype: "textfield",
																								name:'id',
																								hidden:true,
																								value:data.id,
																								readOnly:true
																							},
																							{	
																								xtype: "textfield",
																								fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>标题',
																								labelAlign:'right',
																								labelWidth:66,
																								width:'100%',
																								name:'title',
																								value:data.title,
																								allowBlank : false
																							},
																							{	
																								xtype: "textfield",
																								fieldLabel:'英文标题',
																								labelAlign:'right',
																								labelWidth:66,
																								width:'100%',
																								name:'etitle',
																								value:data.etitle
																							},
																							{	
																								xtype: "textfield",
																								fieldLabel:'版本说明',
																								labelAlign:'right',
																								labelWidth:66,
																								width:'100%',
																								value:data.versionDesc,
																								name:'versionDesc'
																							},{	
																								xtype: "htmleditor",
																								fieldLabel:'正文',
																								width:'100%',
																								height:300,
																								fontFamilies: ["宋体", "隶书", "黑体"],
																								labelAlign:'right',
																								labelWidth:66,
																								value:data.content,
																								name:'content'
																							},{	
																								xtype: "textfield",
																								fieldLabel:'分类',
																								hidden:true,
																								labelAlign:'right',
																								labelWidth:66,
																								width:'100%',
																								name:'catid',
																								value:record.data.id
																							}
																						
																						]
																					}
																				
																				],
																				buttons:[{
																				    
																					text: '保存',
																					handler:function(){
																						var form=Ext.getCmp("creatItemForm");
																						if (form.isValid()) {
																							 form.submit({
															                		                	url:$ctx+'/item/save',
															                		                    success: function(form, action) {
															                		                       Ext.Msg.alert('提示', '修改成功');
															                		                       itemVersionGridStore.load();
															                		                       itemGridStore.load();
															                		                       itemAddWindow.close();
															                		                    },
															                		                    failure: function(form, action) {
															                		                        Ext.Msg.alert('提示', action.result.msg);
															                		                    }
															                		                });
																						}else{
																							 Ext.Msg.alert('提示', '表单信息有误,请核查');
																						}
																						
																					}
																				},{ 
																								text: '返回',
																								handler:function(){
																									
																									itemAddWindow.close();
																								}
																							}
																						]
																			
																			}
																	   	]
																	}
									                    		       ],
									                        	listeners:{
									                        		'close':function(panel){
									                        			//panel.removeAll(false);	
									                        			
									                        		}
									                        	}
									                        }).show();

															   
														   }
										 		});
											    	
											    	
											    }
											}
					                        ]}
							   
						    ],
						    height:'100%',
						    width: '100%',
						    bbar : [
							    {
									xtype : 'pagingtoolbar',
									store:itemVersionGridStore,
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
						  
						}]
					}).show();
					
					
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
					header:"<div style='text-align:center'>标题</div>",
					dataIndex:"title",
					align : 'left',
					width : '26.5%'
			    },
			    {
					header:"关键字",
					dataIndex:"keywords",
					align : 'center',
					width : '23%'
			    },
			    {
					header:"版本说明",
					dataIndex:"versionDesc",
					align : 'center',
					width : '23%'
			    },{
			    	header:"版数",
					dataIndex:"versionCount",
					align : 'center',
					width : '13%',
					renderer:function(value){
				            return '<span style="color:blue;cursor:pointer">'+value+'</span>';
				    }
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
							    	var record=itemGridStore.getAt(rowIndex);
							    	$.ajax({
										   url: "item/findByIdForEdit",
										   data: {id:record.data.id},
										   async:false,
										   dataType: 'json',
										   success: function(data){
										    	var itemAddWindow=Ext.create("Ext.window.Window",{
					                    		autoWidth : true,
					                    		modal:true,
					                    		title:'修改条目',
					                    		resizable:false,
					                    		width: 1100,
					                    		height:650,
					                    		constrain:true,
					                    		overflowY:'scroll',
					                    		overflowX:'hidden',
					                    		items:[
													{
													   	id:'creatBookPanelPut',
													   	xtype:'panel',
														width:'100%',
													   	height:'100%',
													   	region : 'center',
													   	autoWidth:true,
													   	autoScroll:true,
													   	items: [
															{
																xtype:'form',
																id:'creatItemForm',
																width:'100%',
																style:'border-width:0 0 0 0;margin:auto',
																autoScroll : true,
																buttonAlign: 'center',
																items:[
																	{
																		xtype:'fieldset', 
																		title:'条目基本属性', 
																		collapsible:false,
																		width : '100%',
																		items:[
																			{	
																				xtype: "textfield",
																				name:'id',
																				hidden:true,
																				value:data.id,
																				readOnly:true
																			},
																			{	
																				xtype: "textfield",
																				fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>标题',
																				labelAlign:'right',
																				labelWidth:66,
																				width:'100%',
																				name:'title',
																				value:data.title,
																				allowBlank : false
																			},
																			{	
																				xtype: "textfield",
																				fieldLabel:'英文标题',
																				labelAlign:'right',
																				labelWidth:66,
																				width:'100%',
																				name:'etitle',
																				value:data.etitle
																			},
																			{	
																				xtype: "textfield",
																				fieldLabel:'版本说明',
																				labelAlign:'right',
																				labelWidth:66,
																				width:'100%',
																				value:data.versionDesc,
																				name:'versionDesc'
																			},{	
																				xtype: "htmleditor",
																				fieldLabel:'正文',
																				width:'100%',
																				height:420,
																				fontFamilies: ["宋体", "隶书", "黑体"],
																				labelAlign:'right',
																				labelWidth:66,
																				value:data.content,
																				name:'content'
																			},{	
																				xtype: "textfield",
																				fieldLabel:'分类',
																				hidden:true,
																				labelAlign:'right',
																				labelWidth:66,
																				width:'100%',
																				name:'catid',
																				value:record.data.id
																			}
																		
																		]
																	}
																
																],
																buttons:[{
																    
																	text: '保存',
																	handler:function(){
																		var form=Ext.getCmp("creatItemForm");
																		if (form.isValid()) {
																			 form.submit({
											                		                	url:$ctx+'/item/save',
											                		                    success: function(form, action) {
											                		                       Ext.Msg.alert('提示', '修改成功');
											                		                       itemGridStore.load();
											                		                       itemAddWindow.close();
											                		                    },
											                		                    failure: function(form, action) {
											                		                        Ext.Msg.alert('提示', action.result.msg);
											                		                    }
											                		                });
																		}else{
																			 Ext.Msg.alert('提示', '表单信息有误,请核查');
																		}
																		
																	}
																},{ 
																				text: '返回',
																				handler:function(){
																					
																					itemAddWindow.close();
																				}
																			}
																		]
															
															}
													   	]
													}
					                    		       ],
					                        	listeners:{
					                        		'close':function(panel){
					                        			//panel.removeAll(false);	
					                        			
					                        		}
					                        	}
					                        }).show();

											   
										   }
						 		});
							    	
							    	
							    }
							},{},{
								icon: $ctx+'/static/images/btn/delete_0906.png',  // Use a URL in the icon config
							    tooltip: '删除',
							    handler: function(grid, rowIndex, colIndex) {
							    	Ext.MessageBox.confirm(   
							                "请确认"  
							               ,"确定删除吗？"  
							               ,function( button,text ){  
							                   if( button == 'yes'){
							                	   var record=itemGridStore.getAt(rowIndex);
											    	$.ajax({
														   url: "item/delete",
														   data: {id:record.data.id},
														   async:false,
														   dataType: 'json',
														   success: function(data){
															   if(data.success){
																   itemGridStore.load();
																   Ext.Msg.alert('提示', '修改成功');
																   
															   }else{
																   Ext.Msg.alert('提示', data.msg);
															   }
															   
														   }}); 
							                   }  
							               }   
							           );
							    	
							    	
							    }
							},{},{

								icon: $ctx+'/static/images/btn/server_add.png',  // Use a URL in the icon config
							    tooltip: '添加索引词',
							    handler: function(grid, rowIndex, colIndex) {
							    	var record=itemGridStore.getAt(rowIndex);
							    	var indexEditWindow=Ext.create("Ext.window.Window",{
			                    		autoWidth : true,
			                    		modal:true,
			                    		title:'添加索引词',
			                    		resizable:false,
			                    		width: 800,
			                    		height:250,
			                    		constrain:true,
			                    		overflowY:'scroll',
			                    		overflowX:'hidden',
			                    		items:[
											{
											   	xtype:'panel',
												width:'100%',
											   	height:'100%',
											   	region : 'center',
											   	autoWidth:true,
											   	autoScroll:true,
											   	items: [
													{
														xtype:'form',
														id:'addIndexForm',
														width:'100%',
														style:'border-width:0 0 0 0;margin:auto',
														autoScroll : true,
														buttonAlign: 'center',
														items:[
															{
																xtype:'fieldset', 
																title:'索引词基本属性', 
																collapsible:false,
																width : '100%',
																items:[
																	{	
																		xtype: "textfield",
																		name:'itemId',
																		hidden:true,
																		value:record.id,
																		readOnly:true
																	},
																	{	
																		xtype: "textfield",
																		fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>索引词',
																		labelAlign:'right',
																		labelWidth:66,
																		width:'100%',
																		name:'indexName',
																		allowBlank : false
																	},{	
																		xtype: "textfield",
																		fieldLabel:'条目标题',
																		labelAlign:'right',
																		labelWidth:66,
																		width:'100%',
																		readOnly:true,
																		value:record.data.title
																	},
																	{	
																		xtype: "textfield",
																		fieldLabel:'英文标题',
																		labelAlign:'right',
																		labelWidth:66,
																		width:'100%',
																		readOnly:true,
																		value:record.data.etitle
																	}
																	
																
																]
															}
														
														],
														buttons:[{
														    
															text: '保存',
															handler:function(){
																var form=Ext.getCmp("addIndexForm");
																if (form.isValid()) {
																	 form.submit({
									                		                	url:$ctx+'/itemrel/save',
									                		                    success: function(form, action) {
									                		                       Ext.Msg.alert('提示', '修改成功');
									                		                       indexEditWindow.close();
									                		                    },
									                		                    failure: function(form, action) {
									                		                        Ext.Msg.alert('提示', action.result.msg);
									                		                    }
									                		                });
																}else{
																	 Ext.Msg.alert('提示', '表单信息有误,请核查');
																}
																
															}
														},{ 
																		text: '返回',
																		handler:function(){
																			
																			indexEditWindow.close();
																		}
																	}
																]
													
													}
											   	]
											}
			                    		       ],
			                        	listeners:{
			                        		'close':function(panel){
			                        			//panel.removeAll(false);	
			                        			
			                        		}
			                        	}
			                        }).show();
							    }
							
							}
	                        ]}
			   ],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:itemGridStore,
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


var itemMainPanel = new Ext.Panel({
   	id:'itemMainPanel',
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [itemCategoryWestPanel, itemGridPanel ]
});