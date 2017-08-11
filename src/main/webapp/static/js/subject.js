//专题表格数据
var subjectGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','title','desc','createrNickName','createrName','createDate'],
	proxy : {
		type : 'ajax',
		url : 'subject/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
	

});

var subjectItemGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','title','subjectid','keyword','type'],
	pageSize:20,
	proxy : {
		type : 'ajax',
		url : 'subjectItem/findBySubjectid',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	},
	listeners:{
		'load':function(){
				$(".full-length4").myPlugin({'store':subjectItemGridStore,'flag':3});
				$(".fullScreenPic").bigic();
				$(".tcdPageCodeSubject").createPage({
			        pageCount:Math.ceil(subjectItemGridStore.totalCount/20),
			        current:subjectItemGridStore.currentPage,
			        backFn:function(p){
			        	subjectItemGridStore.loadPage(p);
			        }
			    });
			
		}
	}
	

});

//专题分类树数据
var subjectCategoryTreeStore =Ext.create('Ext.data.TreeStore', {
		autoLoad: false,
		proxy:{
			type : 'ajax',
			url : 'subjectCategory/getSons'
		},
		root:{
			nodeType : 'async', 
			expanded : true,
			text:'专题库',
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

//修改专题分类
var updateSubjectCategoryTreeStore =Ext.create('Ext.data.TreeStore', {
		autoLoad: false,
		proxy:{
			type : 'ajax',
			url : 'subjectCategory/getSonsWihtSbjectid'
		},
		root:{
			nodeType : 'async', 
			expanded : true,
			text:'专题库',
			id:'1'
		},
		listeners:{
			'load':function(){
				Ext.getCmp('subjectEntryFormCategroy').expandAll();
			}
		},
		fields: ['id','text','flag'],
		nodeParam:'id'
	});

//专题分类树
var subjectCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '分类管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : subjectCategoryTreeStore,
    rootVisible: true,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   
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
				
				var rootFlag=(record.data.id==1);
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
					    disabled:rootFlag,
					    handler:function(){
					    	 var id = record.data.id; //节点id
					    	Ext.Ajax.request({
								url : $ctx+"/subjectCategory/findById",
								params : { "id" : id },
								disableCaching : false,
								success: function(resp,opts){  
									var subject=Ext.util.JSON.decode(resp.responseText);
									 var form=Ext.getCmp("subjectForm");
							    	 form.reset();
							    	 Ext.getCmp('subjectTitle').setTitle('修改分类信息');
							    	
							    	 Ext.getCmp('subjectFormId').setValue(id);
							    	 Ext.getCmp('subjectFormName').setValue(subject.name);
							    	 Ext.getCmp('subjectFormParentId').setValue(record.parentNode.data.id);
							  	  
							    	 Ext.create('Ext.window.Window',
												{
													modal : true,
													height : 320,
												   	width : 380,
												   	region : 'center',
												   	autoWidth:true,
												   	layout: "border",
													items:[updateSubjectPanel],
													listeners:{
														'close':function(win){
															win.removeAll(false);
														}
													}
													
												}
												).show();
									
								
									
		                        },  
		                        failure: function(resp,opts){  
		                        	Ext.Msg.alert("提示：" ,"分类不存在");
		                        }
							});
					    	
					    	
					    	 
					    	 
					    }  
					},
                  {  
	                        text:'新增',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        		
	                        	 var form=Ext.getCmp("subjectForm");
	                        	 form.reset();
	                        	 Ext.getCmp('subjectTitle').setTitle('新增分类信息');
	                        	 Ext.getCmp('subjectFormParentId').setValue(record.data.id);
	              	    	  
	                        	 Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 320,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[updateSubjectPanel],
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
	                        disabled:rootFlag,
	                        handler:function(){
	                        	
	                            var tipstr = "是否删除？";											
								Ext.Msg.confirm("提示：",	tipstr,function(btn) {
									if (btn == "yes") {
										var id = record.data.id; //节点id
			                            Ext.Ajax.request({
											url : $ctx+"/subjectCategory/delete",
											params : { "id" : id },
											disableCaching : false,
											success: function(resp,opts){  
												Ext.Msg.alert("提示：" ,"删除成功！");
												subjectCategoryTreeStore.proxy.extraParams={};
												subjectCategoryTreeStore.load();
												
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
var checkSubjectCat={};
//专题实体面板
var updateSubjectEntryPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	scrollable:true,
    listeners:{
	added:function(){
		//Ext.getCmp('subjectEntryFormCategroy').expandAll();
	}
},
   	items: [
	{
	xtype:'form',
	width:350,
	id:"subjectEntryForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'subjectEntryTitle',
			title:'新增专题信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'专题名称<font color="red">*</font>',
					id:'subjectEntryFormName',
					name:'title',
					allowBlank: false,
					blankText: '专题名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'专题Id<font color="red">*</font>',
					id:'subjectEntryFormId',
					name:'id',
					hidden:true
					
				},
				 {
	                    xtype: 'textareafield',
	                    fieldLabel: '描述',
	                    maxRows: 4,
	                    id:'subjectEntryFormDesc',
	                    name: 'desc'
	                }
				,{
					xtype: "textfield",
					fieldLabel:'专题分类Id<font color="red">*</font>',
					id:'subjectEntryFormParentId',
					hidden:true,
					name:'cids'
					
				},{
					xtype:'treepanel',
					split: true, //显示分隔条
        	        autoScroll:true,
        	        rootVisible: false,
        	        id:'subjectEntryFormCategroy',
        	        store : updateSubjectCategoryTreeStore,
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
    	    	           // header: '授权',  
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
        	    					var category = updateSubjectCategoryTreeStore.getAt(rowIndex);
        	    					if(checked){
        	    						category.set('flag',true);
        		                		
        	    					}else{
        	    						category.set('flag',false);
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
					var ids="";
					//遍历勾选的节点
					updateSubjectCategoryTreeStore.each(function(record){
						if(record.data.flag){
							ids+=record.id+";";
						}
					});
					Ext.getCmp("subjectEntryFormParentId").setValue(ids);
					
					var form=Ext.getCmp("subjectEntryForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/subject/save',
        		                    success: function(form, action) {
        		                    	updateSubjectEntryPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '操作成功');
        		                       subjectGridStore.load();
        		                     //  subjectCategoryTreeStore.proxy.extraParams={};
        		                      // subjectCategoryTreeStore.load();
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
					updateSubjectEntryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});


//专题分类表单
var updateSubjectPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	items: [
	{
	xtype:'form',
	width:350,
	id:"subjectForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'subjectTitle',
			title:'新增分类信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'分类名称<font color="red">*</font>',
					id:'subjectFormName',
					name:'title',
					allowBlank: false,
					blankText: '分类名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'分类Id<font color="red">*</font>',
					id:'subjectFormId',
					hidden:true,
					name:'id'
					
				},
				,{
					xtype: "textfield",
					fieldLabel:'父分类Id<font color="red">*</font>',
					id:'subjectFormParentId',
					hidden:true,
					name:'pid',
					allowBlank: false
					
				}
				
		        
				
			]
		}
	],
	buttons:[
		{
			text: '保存',
			listeners:{
				click:function(){
					var form=Ext.getCmp("subjectForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/subjectCategory/save',
        		                    success: function(form, action) {
        		                    	updateSubjectPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '修改成功');
        		                       subjectCategoryTreeStore.proxy.extraParams={};
        		                       subjectCategoryTreeStore.load();
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
					updateSubjectPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});

var subjectItemGridPanel=Ext.create('Ext.panel.Panel',{
	width:'85%',
	height:'100%',
	autoWidth:true,
	region : 'center',
	layout:{
		type : "border"
	},
	autoScroll:true,
	listeners:{
	},items:[{
		xtype:'grid',
		layout : 'fit',
		region : 'center',
		height:'100%',
		widht:'100%',
		autoWidth:true,
		columnLines:true,
		store: subjectItemGridStore,
		listeners : {
			'itemdblclick' : function(view,record, item, index, e,eOpts) {}
		},
		columns : [
			
			{
			header:"序号",
			xtype : "rownumberer",
			align : 'center',
			hideable:false,
			width : '4%'
		},{
				header:"名称",
				dataIndex:"title",
				align : 'center',
				width : '40%'
		    },{
				header:"创建者",
				dataIndex:"createrNickName",
				align : 'center',
				width : '10%'
		    },{
				header:"创建时间",
				dataIndex:"createDate",
				align : 'center',
				width : '15%'
		    },{
		    	header:"缩略图",
		    	dataIndex:"type",
		    	align : 'center',
		    	width : '20%',
		    	renderer:function(value,cellmeta,record,rowIndex,columnIndex,stroe){
		    		
		    		switch (value) {
					case 1:
						//图片
	            		return "<img class='smallPic' src='"+$ctx+'/photo/getSmallImage?id='+record.data.itemid+"' />"
						break;
					case 2:
						//视频
						return "<img class='smallPic' src='"+$ctx+"/video/getVideoPic/"+record.data.itemid+"/2' />";
						break;
					default:
						return '文字稿';
						break;
					}
		    		
	            	
	            	
	            	
	            	
	            },
		    },
		  {
                xtype:'actioncolumn',
                header:"操作",
                align : 'center',
                width:'8%',
                items: [
					{
							icon: $ctx+'/static/images/btn/unokay.png',  // Use a URL in the icon config
						    tooltip: '删除',
						    handler: function(grid, rowIndex, colIndex) {

						    	Ext.MessageBox.confirm(   
						                "请确认"  
						               ,"确定删除吗？"  
						               ,function( button,text ){  
						                   if( button == 'yes'){
						                	   var record=subjectItemGridStore.getAt(rowIndex);
						                	   var subject=getGridSelectionRecords('subjectGrid');
						                	   
						                	   
										    	$.ajax({
													   url: "subject/deleteItems",
													   data: {subjectid:subject[0].data.id,subjectItemdids:record.data.id},
													   async:false,
													   dataType: 'json',
													   success: function(msg){
														   Ext.Msg.alert('提示', '删除成功');
														   subjectItemGridStore.loadPage(1);
														   
													   }});
						                   }  
						               }   
						           );
						    
						    }
						}
                        ]}
		]
	}],
	bbar : [
	    {
			xtype : 'pagingtoolbar',
			store:subjectItemGridStore,
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
//专题查询结果显示
var subjectGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'专题列表',
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
      			//hidden:true,
      			enableOverflow:true,
      			items :[
				    {
				        xtype: 'button',
				        text:'创建专题',
				        listeners:{
				        	'click':function(){
				        		
				        		updateSubjectCategoryTreeStore.proxy.extraParams={
									    };
				        		updateSubjectCategoryTreeStore.load();

	                    		 var form=Ext.getCmp("subjectEntryForm");
	                        	 form.reset();
	                    		 Ext.getCmp('subjectEntryTitle').setTitle('新增专题信息');
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
	                    		
	                    		window.add(updateSubjectEntryPanel);
	                    	
				        	}
				        }
				       
				    }
      			   ]
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
			id:'subjectGrid',
			store: subjectGridStore,
			listeners : {
				'itemdblclick' : function(view,record, item, index, e,eOpts) {
					subjectItemGridStore.proxy.extraParams={'subjectid':record.data.id};
					subjectItemGridStore.loadPage(1);
					Ext.create('Ext.window.Window',
							{
								autoScroll:false,
								constrain : true,
								modal : true,
								title:'专题',
								width: document.body.clientWidth * 0.84,
								height: document.body.clientHeight * 0.9,
								layout: "border",
								listeners:{
									'close':function(win){
										win.removeAll(false);
									}
								},
								items : [subjectItemGridPanel]
							}
	   					).show();
				}
			},
			//'id','title','desc','createrNickName','createrName'
			columns : [
				
				{
				header:"序号",
				xtype : "rownumberer",
				align : 'center',
				hideable:false,
				width : '4%'
			},{
					header:"名称",
					dataIndex:"title",
					align : 'center',
					width : '47.5%'
			    },{
					header:"创建者",
					dataIndex:"createrNickName",
					align : 'center',
					width : '20%'
			    },{
					header:"创建时间",
					dataIndex:"createDate",
					align : 'center',
					width : '20%'
			    },
			  {
	                xtype:'actioncolumn',
	                header:"操作",
	                align : 'center',
	                width:'8%',
	                items: [
	                	{
							icon: $ctx+'/static/images/btn/edit.gif',  // Use a URL in the icon config
						    tooltip: '编辑',
						    getClass:function(v,m,r){
						    	if(r.data.status==1){
						    		//return 'x-hidden';   隐藏
						    		return this.disabledCls;
						    	}
			                	
			                },
						    handler: function(grid, rowIndex, colIndex) {

						    	var record=subjectGridStore.getAt(rowIndex);

				        		
				        		updateSubjectCategoryTreeStore.proxy.extraParams={
				        				sid:record.data.id
									    };
				        		updateSubjectCategoryTreeStore.load();
				        		
	                    		 var form=Ext.getCmp("subjectEntryForm");
	                        	 form.reset();
	                    		 Ext.getCmp('subjectEntryTitle').setTitle('修改专题信息');
	                    		 Ext.getCmp('subjectEntryFormName').setValue(record.data.title);
	                    		 Ext.getCmp('subjectEntryFormId').setValue(record.data.id);
	                    		 Ext.getCmp('subjectEntryFormDesc').setValue(record.data.desc);
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
	                    		
	                    		window.add(updateSubjectEntryPanel);
	                    	
				        	
						    
						    }
						},{},{

							icon: $ctx+'/static/images/btn/okay.png',  // Use a URL in the icon config
						    tooltip: '发布',
						    getClass:function(v,m,r){
						    	if(r.data.status==1||!subjectPubPermission){
						    		//return 'x-hidden';   隐藏
						    		return this.disabledCls;
						    	}
			                	
			                },
						    handler: function(grid, rowIndex, colIndex) {
						    	var record=subjectGridStore.getAt(rowIndex);
						    	$.ajax({
									   url: "subject/pub",
									   data: {subjectid:record.data.id},
									   async:false,
									   dataType: 'json',
									   success: function(msg){
										   Ext.Msg.alert('提示', '发布成功');
										   subjectGridStore.load();
										   
									   }});
						    }
						
						},
						{},{
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
							                	   var record=subjectGridStore.getAt(rowIndex);
											    	$.ajax({
														   url: "subject/delete",
														   data: {id:record.data.id},
														   async:false,
														   dataType: 'json',
														   success: function(msg){
															   Ext.Msg.alert('提示', '删除成功');
															   subjectGridStore.loadPage(1);
															   
														   }});
							                   }  
							               }   
							           );
							    
							    }
							}
	                        ]}
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:subjectGridStore,
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


var subjectMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [subjectCategoryWestPanel, subjectGridPanel ]
});