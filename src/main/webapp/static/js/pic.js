//词条表格数据
var picGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['title','photoid','role','id','ceaterUser','createrNickName','createTime'],
	pageSize:20,
	proxy : {
		type : 'ajax',
		url : 'pcrc/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	},
	listeners:{
		load:function(){
			//alert(1);
//			$(".full-length2").html('').myPlugin({'store':picGridStore,'flag':1});
			$(".fullScreenPic").bigic();
//			$(".tcdPageCode").createPage({
//		        pageCount:Math.ceil(picGridStore.totalCount/20),
//		        current:picGridStore.currentPage,
//		        backFn:function(p){
//		        	picGridStore.loadPage(p);
//		        }
//		    });
		}
	}
	

});


var picCategoryTreeStore= Ext.create('Ext.data.TreeStore', {
	autoLoad: false,
	proxy:{
		type : 'ajax',
		url : 'category/getSons?resourcesType=1'
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
						record.data.text='图片库';
					}else{
					if(record.data.resourcesType==1){
						record.data.text=record.data.name+"[图片]";
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

var updatePicCategoryPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	items: [
	{
	xtype:'form',
	width:350,
	id:"picCategoryMagageForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			id:'picCategoryMagageTitle',
			title:'新增分类信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'分类名称<font color="red">*</font>',
					id:'picCategoryMagageFormName',
					name:'name',
					allowBlank: false,
					blankText: '分类名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'分类Id<font color="red">*</font>',
					id:'picCategoryMagageFormId',
					hidden:true,
					name:'id',
					blankText: '分类Id为必填项'
					
				},
				{
					xtype: "textfield",
					id:'picCategoryMagageFormResourceType',
					hidden:true,
					value:'1',
					name:'resourcesType'
					
				}
				,{
					xtype: "textfield",
					fieldLabel:'父分类Id<font color="red">*</font>',
					id:'picCategoryMagageFormParentId',
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
					var form=Ext.getCmp("picCategoryMagageForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/category/save',
        		                    success: function(form, action) {
        		                    updatePicCategoryPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '修改成功');
        		                       picCategoryTreeStore.proxy.extraParams={};
        		                       picCategoryTreeStore.load();
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
					updatePicCategoryPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});



//词库分类树
var picCategoryWestPanel = Ext.create('Ext.tree.Panel', {
    title: '词条管理',
    width: 200,
    heigth: 'auto',
    split: true, //显示分隔条
    region: 'west',
    collapsible: true,
    store : picCategoryTreeStore,
    rootVisible: false,
    listeners : {
	       'itemclick' : function(view,record,item,index,e,eOpts){
	    	   picGridStore.proxy.extraParams={catid:record.id};
	    	   picGridStore.loadPage(1);
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
					    text:'修改',
					    icon : $ctx+'/static/images/btn/edit.gif',
					    handler:function(){
					    	if(record.data.resourcesType==0){
                        		Ext.Msg.alert("提示：" ,"公共知识体系不允许删除！");
                        		return;
                        	}
					    	 var form=Ext.getCmp("picCategoryMagageForm");
					    	 form.reset();
					    	 Ext.getCmp('picCategoryMagageTitle').setTitle('修改分类信息');
					    	 var id = record.data.id; //节点id
					    	 Ext.getCmp('picCategoryMagageFormId').setValue(id);
					    	 Ext.getCmp('picCategoryMagageFormName').setValue(record.data.name);
					    	 Ext.getCmp('picCategoryMagageFormParentId').setValue(record.parentNode.data.id);
					    	 
					  	  
					    	 Ext.create('Ext.window.Window',
										{
											modal : true,
											height : 320,
										   	width : 380,
										   	region : 'center',
										   	autoWidth:true,
										   	layout: "border",
											items:[updatePicCategoryPanel],
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
	                        	 var form=Ext.getCmp("picCategoryMagageForm");
	                        	 form.reset();
	                        	 Ext.getCmp('picCategoryMagageTitle').setTitle('新增分类信息');
	                        	 var id = record.data.id; //节点id
	                        	 Ext.getCmp('picCategoryMagageFormParentId').setValue(id);
	              	    	  
	                        	 Ext.create('Ext.window.Window',
											{
												modal : true,
												height : 320,
											   	width : 380,
											   	region : 'center',
											   	autoWidth:true,
											   	layout: "border",
												items:[updatePicCategoryPanel],
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
												picCategoryTreeStore.proxy.extraParams={};
												 picCategoryTreeStore.load();
												
					                        },  
					                        failure: function(resp,opts){  
					                        	Ext.Msg.alert("提示：" ,"删除失败,其重试");
					                        }
										});
									}
								});
	                        }  
	                    },{
	                    	text:'上传图片',
	                        icon : $ctx+'/static/images/btn/add_0906.png',
	                        id:'liao_abc',
	                        handler:function(){
	                        	$("#uploadPic2").trigger('click');
	                        }
	                        /*handler:function(){
	                        	var window=Ext.create('Ext.window.Window',
										{
											modal : true,
											height : 320,
										   	width : 380,
										   	region : 'center',
										   	autoWidth:true,
										   	layout: "border",
											items:[{
												width:'85%',
												xtype:'panel',
											   	autoWidth:true,
											   	region : 'center',
											   	items: [
												{
												xtype:'form',
												width:350,
												id:"addPicForm",
												style:'margin:auto',
												buttonAlign: 'center',
												items:[
													{ 
														xtype:'fieldset',
														title:'上传图片', 
														collapsible:false,
														width : 350,
														items:[
															{	
																xtype: "textfield",
																fieldLabel:'图片名称<font color="red">*</font>',
																name:'title',
																allowBlank: false,
																blankText: '图片名称为必填项'
															},{
																xtype:"filefield",
																id : 'file-idx',
																fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>图片',
																buttonText:'上传图片',
																name:'image'
//																
															},
															{
																xtype: "textfield",
																hidden:true,
																name:'cid',
																value:record.data.id
															}
															
													        
															
														]
													}
												],
												buttons:[
													{
														text: '保存',
														listeners:{
															click:function(){
																var form=Ext.getCmp("addPicForm");
																
																var furl="";  
											                    furl = form.form.findField('image').getValue();
//											                    
											                    var type = furl.substring(furl.lastIndexOf("."), furl.length).toLowerCase();
											                   
											                    if (furl == "" || furl == null) {  
											                    	Ext.Msg.alert('提示框','请上传图片');
											                        return;  
											                    }  
											                    if (furl != "" ){
											                    	 if (type != '.jpg' && type != '.jpeg' && type != '.bmp' && type != '.gif' && type != '.png') {  
													                    	Ext.Msg.alert('提示框','仅支持jpg、jpeg、bmp、gif、png格式的图片');  
													                        return;  
													                    }  
											                    }
																
																
																if (form.isValid()) {
																	 form.submit({
											        		                	url:$ctx+'/photo/add',
											        		                    success: function(form, action) {
											        		                       picGridStore.loadPage(1);
											        		                       window.close();
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
																window.close();
															}
														}
														
													}
												]


												
											}]}]
											
										}
				   					).show();
	                        }*/
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

var picOutlineChecked=[];
//图片查询结果显示
var picGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'图表',
    	width:'85%',
    	height:'100%',
    	autoWidth:true,
    	region : 'center',
    	 bodyStyle: 'background:#fff',
    	layout:{
    		type : "border"
    	},
    	autoScroll:true,
    	listeners:{
    		'afterrender':function(){
    			//添加监听事件
    			$("#picAddToProductButton").on('click',function(){
    				picOutlineChecked=[];
    				$.each($('.picOutline'),function(i,n){
    					if(n.checked){
    						picOutlineChecked.push(n.value);
    					}
    				});
    				if(picOutlineChecked.length==0){
    					 Ext.Msg.alert('提示', '请至少选择一张图片');
    					 return;
    				}else{

		        		mixProductGridStore.loadPage(1);
		        		Ext.getCmp("mixProductItemGrid").setValue('picGrid');
		        		Ext.create('Ext.window.Window',
								{
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
    			});
    			
    			

    			//添加监听事件
    			$("#picAddToSubjectButton").on('click',function(){
    				picOutlineChecked=[];
    				$.each($('.picOutline'),function(i,n){
    					if(n.checked){
    						picOutlineChecked.push(n.value);
    					}
    				});
    				if(picOutlineChecked.length==0){
    					 Ext.Msg.alert('提示', '请至少选择一张图片');
    					 return;
    				}else{

    					subjectCommonGridStore.loadPage(1);
		        		Ext.getCmp("subjectCommonType").setValue('1');
		        		Ext.create('Ext.window.Window',
								{
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
    			});
    			
    			
    		
    			
    			
    		}
    	},
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
				        		Ext.getCmp("mixProductItemGrid").setValue("picGrid");
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
				        		Ext.getCmp('subjectCommonType').setValue(1)
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
    			id:'picGrid',
    			autoWidth:true,
    			columnLines:true,
    			store: picGridStore,
    			listeners : {
    				'itemdblclick' : function(view,record, item, index, e,eOpts) {
    					//window.open("http://localhost:8080/mas/openapi/pages.do?method=exPlay&appKey=res&id="+record.data.masId);
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
    					style:'vertical-align: middle;',
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
    					width : '7%'
    			    },
    			    {
    					header:"缩略图",
    					dataIndex:"photoid",
    					align : 'center',
    					width : '15%',
    					renderer:function(value){
    						return '<a href="#" class="fullScreenPic" src="photo/getImage?id='+value+'"><image class="smallPic" src='+$ctx+'/photo/getSmallImage?id='+value+' /></a>';
    						
    					}
    						
    			    },
    			   {
    					header:"描述",
    					dataIndex:"desc",
    					align : 'center',
    					width : '15%'
    			    },
    			    {
   			    	 xtype:'actioncolumn',
                        header:"操作",
                        align : 'center',
                        dataIndex:"status",
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
                                                   var record=picGridStore.getAt(rowIndex);
                                                    $.ajax({
                                                           url: "photo/delete",
                                                           data: {id:record.data.photoid},
                                                           async:false,
                                                           dataType: 'json',
                                                           success: function(msg){
                                                        	   if(msg.success){
                                                        		   Ext.Msg.alert('提示', '删除成功');
                                                                   picGridStore.loadPage(1);
                                                        	   }else{
                                                        		   Ext.Msg.alert('提示', msg.msg);
                                                        	   }
                                                             
                                                               
                                                           }});
                                               }  
                                           }   
                                       );
                                    
                                    
                                    
                                }
                            }
                        	]
    			    }
    			    
    			]
    		}
    	
			
		],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:picGridStore,
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
//    	html:'<div id="picAddToProductButton" style="height：24px">'+
//    		'<a class="x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-toolbar-small" hidefocus="on" unselectable="on" role="button" aria-hidden="false" aria-disabled="false" id="itemSearchButton2" tabindex="0" data-componentid="itemSearchButton2" style="right: auto; left: 65px; top: 0px; margin: 0px;"><span id="itemSearchButton2-btnWrap" data-ref="btnWrap" role="presentation" unselectable="on" style="" class="x-btn-wrap x-btn-wrap-default-toolbar-small "><span id="itemSearchButton2-btnEl" data-ref="btnEl" role="presentation" unselectable="on" style="" class="x-btn-button x-btn-button-default-toolbar-small x-btn-text    x-btn-button-center "><span id="itemSearchButton2-btnIconEl" data-ref="btnIconEl" role="presentation" unselectable="on" class="x-btn-icon-el x-btn-icon-el-default-toolbar-small  " style=""></span><span id="itemSearchButton2-btnInnerEl" data-ref="btnInnerEl" unselectable="on" class="x-btn-inner x-btn-inner-default-toolbar-small">添加到产品</span></span></span></a></div><div class="full-length2"></div>'+
//    		'<div id="picAddToSubjectButton" style="height：24px"><a class="x-btn x-unselectable x-box-item x-toolbar-item x-btn-default-toolbar-small" hidefocus="on" unselectable="on" role="button" aria-hidden="false" aria-disabled="false" id="itemSearchButton3" tabindex="0" data-componentid="itemSearchButton3" style="right: auto; left: 165px; top: 0px; margin: 0px;"><span id="itemSearchButton3-btnWrap" data-ref="btnWrap" role="presentation" unselectable="on" style="" class="x-btn-wrap x-btn-wrap-default-toolbar-small "><span id="itemSearchButton3-btnEl" data-ref="btnEl" role="presentation" unselectable="on" style="" class="x-btn-button x-btn-button-default-toolbar-small x-btn-text    x-btn-button-center "><span id="itemSearchButton3-btnIconEl" data-ref="btnIconEl" role="presentation" unselectable="on" class="x-btn-icon-el x-btn-icon-el-default-toolbar-small  " style=""></span><span id="itemSearchButton3-btnInnerEl" data-ref="btnInnerEl" unselectable="on" class="x-btn-inner x-btn-inner-default-toolbar-small">添加到专题</span></span></span></a></div>'+
//    	'<div class="tcdPageCode" style="float:left"></div>'
    	
});



var picMainPanel = new Ext.Panel({
	height : "85%",
	width : "100%",
	border : 5,
	region : 'center',
	autoWidth:true,
	layout: "border",
	items : [picCategoryWestPanel, picGridPanel ]
});