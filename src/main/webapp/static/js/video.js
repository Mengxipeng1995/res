//视频数据
var videoGridStore=Ext.create('Ext.data.Store', {
	fields:['id','videoId','title','masId','createrUserName','createrNickName','createDate','status','desc'],
	proxy : {
		type : 'ajax',
		url : 'vcmc/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}

});



//视频分类树数据
var videoCategoryTreeStore = Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'category/getSons?resourcesType=2'
	},
	root:{
		nodeType : 'async', 
		expanded : true,
		id:'1'
	},
	fields: ['id','text','resourcesType'],
	nodeParam:'id',
	listeners:{
		load:function( store , records , successful , eOpts ) {
			if(successful){
				Ext.Array.each(records, function(record, index, countriesItSelf) {
					if(record.data.id==2){
						record.data.text='视频库';
					}else{
					if(record.data.resourcesType==2){
						record.data.text=record.data.name+"[视频]";
					}else{
						record.data.text=record.data.name;
					}
					}
					
					record.commit();
				});


			}
	}
		
	}
});



var updateVideoCategoryPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	items: [
	{
	xtype:'form',
	width:350,
	id:"videoCategoryMagageForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'videoCategoryMagageTitle',
			title:'新增分类信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'分类名称<font color="red">*</font>',
					id:'videoCategoryMagageFormName',
					name:'name',
					allowBlank: false,
					blankText: '分类名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'分类Id<font color="red">*</font>',
					id:'videoCategoryMagageFormId',
					hidden:true,
					name:'id',
					blankText: '分类Id为必填项'
					
				},
				{
					xtype: "textfield",
					id:'videoCategoryMagageFormResourceType',
					hidden:true,
					value:'2',
					name:'resourcesType'
					
				}
				,{
					xtype: "textfield",
					fieldLabel:'父分类Id<font color="red">*</font>',
					id:'videoCategoryMagageFormParentId',
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
					var form=Ext.getCmp("videoCategoryMagageForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/category/save',
        		                    success: function(form, action) {
        		                    updateVideoCategoryPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '修改成功');
        		                       videoCategoryTreeStore.proxy.extraParams={};
        		                       videoCategoryTreeStore.load();
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
					updateVideoCategoryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});


//视频分类树
var videoCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '视频管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : videoCategoryTreeStore,
    rootVisible: false,
    listeners : {
    	'itemclick' : function(view,record,item,index,e,eOpts){
	    	   if(videoCategoryTreeStore.getRoot().childNodes[0].id==record.data.id){
	    		   videoGridStore.proxy.extraParams={};  
	    	   }else{
	    		   videoGridStore.proxy.extraParams={catid:record.data.id};
	    	   }
	    	   videoGridStore.load();
	    	   //
	    	   
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
					    text:'修改',
					    icon : $ctx+'/static/images/btn/edit.gif',
					    handler:function(){
					    	if(record.data.resourcesType==0){
                       		Ext.Msg.alert("提示：" ,"公共知识体系不允许删除！");
                       		return;
                       	}
					    	 var form=Ext.getCmp("videoCategoryMagageForm");
					    	 form.reset();
					    	 Ext.getCmp('videoCategoryMagageTitle').setTitle('修改分类信息');
					    	 var id = record.data.id; //节点id
					    	 Ext.getCmp('videoCategoryMagageFormId').setValue(id);
					    	 Ext.getCmp('videoCategoryMagageFormName').setValue(record.data.name);
					    	 Ext.getCmp('videoCategoryMagageFormParentId').setValue(record.parentNode.data.id);
					    	 
					  	  
					    	 Ext.create('Ext.window.Window',
										{
											modal : true,
											height : 320,
										   	width : 380,
										   	region : 'center',
										   	autoWidth:true,
										   	layout: "border",
											items:[updateVideoCategoryPanel],
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
	                        	 var form=Ext.getCmp("videoCategoryMagageForm");
	                        	 form.reset();
	                        	 Ext.getCmp('videoCategoryMagageTitle').setTitle('新增分类信息');
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('videoCategoryMagageFormParentId').setValue(id);
	              	    	  
	                        	 Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 320,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[updateVideoCategoryPanel],
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
	                        text:'上传视频',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        handler:function(){
	                        	var uploadVideoWindow=Ext.create("Ext.window.Window",{
		                    		autoWidth : true,
		                    		modal:true,
		                    		title:'上传视频',
		                    		resizable:false,
		                    		width: 500,
		                    		height:340,
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
													id:'uploadVideoForm',
													width:'100%',
													style:'border-width:0 0 0 0;margin:auto',
													autoScroll : true,
													buttonAlign: 'center',
													items:[
														{
															xtype:'fieldset', 
															title:'视频属性', 
															collapsible:false,
															width : '100%',
															items:[
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
																	fieldLabel:'分类id',
																	hidden:true,
																	name:"catid",
																	value:record.data.id
																},
																{	
																	xtype: "textfield",
																	fieldLabel:'分类名称',
																	labelAlign:'right',
																	labelWidth:66,
																	width:'100%',
																	readOnly:true,
																	value:record.data.text
																},{
															        xtype: 'filefield',
															        name: 'video',
															        fieldLabel: '<span style="color:red;    vertical-align: sub;"> * </span>视频',
															        labelWidth: 66,
															        msgTarget: 'side',
															        allowBlank: false,
															        width:'100%',
															        allowBlank : false,
															        buttonText: '选择视频...'
															    },
																{	
																	xtype: "htmleditor",
																	fieldLabel:'说明',
																	width:'100%',
																	height:120,
																	fontFamilies: ["宋体", "隶书", "黑体"],
																	labelAlign:'right',
																	labelWidth:66,
																	name:'desc'
																}
															
																
																
															]
														}
													
													],
													buttons:[{
													    
														text: '保存',
														
														handler:function(){
//															var myMask = new Ext.LoadMask(Ext.getBody(),{msg:"视频上传中..."}); 
//															myMask.show()
															var form=Ext.getCmp("uploadVideoForm");
															if (form.isValid()) {
																 form.submit({
								                		                	url:$ctx+'/video/save',
								                		                    success: function(form, action) {
								                		                    //	myMask.hide();
								                		                       Ext.Msg.alert('提示', '修改成功');
								                		                       videoGridStore.loadPage(1);
								                		                       uploadVideoWindow.close();
								                		                    },
								                		                    failure: function(form, action) {
								                		                    //	myMask.hide();
								                		                        Ext.Msg.alert('提示', action.result.msg);
								                		                    }
								                		                });
															}else{
																//myMask.hide();
																 Ext.Msg.alert('提示', '表单信息有误,请核查');
															}
															
															
															
															
															
														}
													},{ 
																	text: '返回',
																	handler:function(){
																		
																		uploadVideoWindow.close();
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
	                        	
	                        	
	                        	//window.open("http://localhost:8080/mas/openapi/pages.do?method=upload&appKey=res&userName="+$userName+"&catid="+record.data.id);
	                        }  
	                    },
	                    {  
	                        text:'删除',
	                        icon : $ctx+'/static/images/btn/delete_0906.png',
	                        handler:function(){
	                        	if(record.data.resourcesType==0){
	                        		Ext.Msg.alert("提示：" ,"公共知识体系不允许删除！");
	                        		return;
	                        	}
	                            var tipstr = "是否删除？";											
								Ext.Msg.confirm("提示：",	tipstr,function(btn) {
									if (btn == "yes") {
										var id = record.data.id; //节点id
			                            Ext.Ajax.request({
											url : $ctx+"/category/delete",
											params : { "id" : id },
											disableCaching : false,
											success: function(resp,opts){  
												Ext.Msg.alert("提示：" ,"删除成功！");
												videoCategoryTreeStore.proxy.extraParams={};
												 videoCategoryTreeStore.load();
												
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



//视频查询结果显示
var videoGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'视频列表',
    	width:'85%',
    	height:'100%',
    	autoWidth:true,
    	region : 'center',
    	 bodyStyle: 'background:#fff',
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
				        text:'添加到产品',
				        listeners:{
				        	'click':function(){
				        		mixProductGridStore.loadPage(1);
				        		Ext.getCmp("mixProductItemGrid").setValue("videoGrid");
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
				        		Ext.getCmp('subjectCommonType').setValue(2)
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
    	items:[
    		{
    			xtype:'grid',
    			layout : 'fit',
    			region : 'center',
    			height:'100%',
    			widht:'100%',
    			autoWidth:true,
    			columnLines:true,
    			id:'videoGrid',
    			store: videoGridStore,
    			listeners : {
    				'itemdblclick' : function(view,record, item, index, e,eOpts) {
    					
    					var videoPlayer='<object type="application/x-shockwave-flash" data="/res/static/plugin/player_flv_maxi.swf" style="height:377px;width:500px"> <param name="menu" value="false"><param name="allowFullScreen" value="true"><param name="movie" value="/res/static/plugin/player_flv_maxi.swf"><param name="FlashVars" value="flv=/res/video/getVideo/'+record.data.videoId+'&width=500&height=377&showstop=1&showvolume=1&showtime=1&startimage=/res/video/getVideoPic/'+record.data.videoId+'/0&showfullscreen=1&bgcolor1=189ca8&bgcolor2=085c68&playercolor=085c68"></object>';
    					
    					Ext.create('Ext.window.Window',
								{
									title : record.data.title,
									autoScroll:true,
									constrain : true,
									modal : true,
									width: 520,
									height:420,
//									layout : 'fit',
								//	bodyCls:'itemDetail',
									html:videoPlayer
								}
		   					).show();
    					
    					
    					
    					
    					
    					
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
    					header:"<div style='text-align:center'>上传账号</div>",
    					dataIndex:"createrNickName",
    					align : 'left',
    					width : '13%'
    			    },
    			    {
    					header:"<div style='text-align:center'>上传昵称</div>",
    					dataIndex:"createrNickName",
    					align : 'left',
    					width : '13%'
    			    },
    			    {
    					header:"创建时间",
    					dataIndex:"createDate",
    					align : 'center',
    					width : '13%'
    			    },
    			    {
    					header:"缩略图",
    					dataIndex:"status",
    					align : 'center',
    					width : '15%',
    					renderer:function(value, metaData, record){
    						//b27e6103-99d0-4cfc-be24-e815786b220b
    						if(value==1){
    							return '<image src=video/getVideoPic/'+record.data.videoId+'/2 />';
    						}else{
    							return '<image src='+$ctx+'/static/images/change.jpg />';
    						}
    						
    					}
    						
    			    },
    			   {
    					header:"描述",
    					dataIndex:"desc",
    					align : 'center',
    					width : '19%'
    			    },{
    			    	 xtype:'actioncolumn',
                         header:"操作",
                         align : 'center',
                         dataIndex:"status",
                         width:'8%',
                         items: [
                        	 {

                                 icon: $ctx+'/static/images/btn/edit.png',  // Use a URL in the icon config
                                 tooltip: '编辑',
                                 handler: function(grid, rowIndex, colIndex) {
                                	 var record=videoGridStore.getAt(rowIndex);
                                	 $.ajax({
                                         url: "video/record",
                                         data: {id:record.data.videoId},
                                         async:false,
                                         dataType: 'json',
                                         success: function(msg){
                                        	 var editVideoWindow= Ext.create('Ext.window.Window',{
                                        		 modal : true,
                                                 height : 320,
                                                 width : 380,
                                                 region : 'center',
                                                 autoWidth:true,
                                                 layout: "border",
                                                 items:[
                                                	 {
                                                		 xtype:'panel',
                                                         width:'100%',
                                                         height:'100%',
                                                         region : 'center',
                                                         autoWidth:true,
                                                         autoScroll:true,
                                                         items:[
                                                        	 {
                                                                 xtype:'form',
                                                                 id:'editVideoForm',
                                                                 width:'100%',
                                                                 style:'border-width:0 0 0 0;margin:auto',
                                                                 autoScroll : true,
                                                                 buttonAlign: 'center',
                                                                 items:[
                                                                	 {
                                                                         xtype:'fieldset', 
                                                                         title:'视频属性', 
                                                                         collapsible:false,
                                                                         width : '100%',
                                                                         items:[
                                                                        	 {   
                                                                                 xtype: "textfield",
                                                                                 hidden:true,
                                                                                 name:'id',
                                                                                 value:msg.id
                                                                             },
                                                                             {   
                                                                                 xtype: "textfield",
                                                                                 fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>标题',
                                                                                 labelAlign:'right',
                                                                                 labelWidth:66,
                                                                                 width:'100%',
                                                                                 name:'title',
                                                                                 value:msg.title,
                                                                                 allowBlank : false
                                                                             },
                                                                             {   
                                                                                 xtype: "htmleditor",
                                                                                 fieldLabel:'说明',
                                                                                 width:'100%',
                                                                                 height:120,
                                                                                 fontFamilies: ["宋体", "隶书", "黑体"],
                                                                                 labelAlign:'right',
                                                                                 labelWidth:66,
                                                                                 value:msg.desc,
                                                                                 name:'desc'
                                                                             }
                                                                         ]
                                                                         
                                                                     }
                                                                	 	],
                                                                	 	buttons:[
                                                                            {
                                                                                text: '保存',
                                                                                handler:function(){
                                                                                    var form=Ext.getCmp("editVideoForm");
                                                                                    if (form.isValid()) {
                                                                                         form.submit({
                                                                                                    url:$ctx+'/video/edit',
                                                                                                    success: function(form, action) {
                                                                                                       Ext.Msg.alert('提示', '修改成功');
                                                                                                       videoGridStore.loadPage(1);
                                                                                                       editVideoWindow.close();
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
                                                                                    
                                                                                    editVideoWindow.close();
                                                                                }
                                                                            }
                                                                        ]
                                                        	 }
                                                        	 ]
                                                	 }
                                                 ]
                                        	 }).show();
                                         }
                                	 });
                                	 
                                 }
                        	 },{},{
                                 icon: $ctx+'/static/images/btn/unokay.png',  // Use a URL in the icon config
                                 tooltip: '删除',
                                 handler: function(grid, rowIndex, colIndex) {
                                     Ext.MessageBox.confirm(   
                                             "请确认"  
                                            ,"确定删除吗？"  
                                            ,function( button,text ){  
                                                if( button == 'yes'){
                                                    var record=videoGridStore.getAt(rowIndex);
                                                     $.ajax({
                                                            url: "video/delete",
                                                            data: {id:record.data.videoId},
                                                            async:false,
                                                            dataType: 'json',
                                                            success: function(msg){
                                                                Ext.Msg.alert('提示', '删除成功');
                                                                videoGridStore.loadPage(1);
                                                                
                                                            }});
                                                }  
                                            }   
                                        );
                                     
                                     
                                     
                                 }
                             }
                        	 ]
                         
    			    }
    	]}],
    	bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:videoGridStore,
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


var videoMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [videoCategoryWestPanel, videoGridPanel ]
});