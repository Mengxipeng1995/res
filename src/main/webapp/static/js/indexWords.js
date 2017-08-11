var indexGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','itemid','indexid','title','etitle','indexName'],
	pageSize : 20,
	proxy : {
		type : 'ajax',
		url : 'itemrel/outline',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
});


var indexGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'索引词列表',
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
 			items :[
			        {
				        xtype: 'textfield',
				        fieldLabel: '索引词',
				        labelWidth:50,
				        id:'indexWordSearchWord',
				        listeners:{
 							'specialKey':function(tf,e){
 								if (e.keyCode == 13) {
 									Ext.getCmp('indexWordSearchButton').fireEvent('click');
 							        }
 							}
 							
 						}
				    }, {
				        xtype: 'button',
				        text:'查询',
				        id:'indexWordSearchButton',
				        listeners:{
				        	'click':function(){
				        		indexGridStore.proxy.extraParams={searchWord:Ext.getCmp("indexWordSearchWord").getValue()};
				        		indexGridStore.loadPage(1);
				        	}
				        }
				       
				    },
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
			store: indexGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'), 
			listeners:{
					'itemdblclick' : function(view,record, item, index, e,eOpts) {
					
					$.ajax({
						   url: "item/findById",
						   data: {id:record.data.itemid},
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
					header:"索引词",
					dataIndex:"indexName",
					align : 'center',
					width : '30%'
				},
			    {
					header:"条目标题",
					dataIndex:"title",
					align : 'center',
					width : '37%'
			    },
			    {
					header:"英文标题",
					dataIndex:"etitle",
					align : 'center',
					width : '24%'
			    },{

	                xtype:'actioncolumn',
	                header:"操作",
	                align : 'center',
	                width:'8%',
	                items: [
							{
								icon: $ctx+'/static/images/btn/edit.gif',  // Use a URL in the icon config
							    tooltip: '编辑',
							    handler: function(grid, rowIndex, colIndex) {
							    	var record=indexGridStore.getAt(rowIndex);
							    	

							    	var indexEditWindow=Ext.create("Ext.window.Window",{
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
										   	xtype:'panel',
											width:'100%',
										   	height:'100%',
										   	region : 'center',
										   	autoWidth:true,
										   	autoScroll:true,
										   	items: [
												{
													xtype:'form',
													id:'updateIndexForm',
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
																	name:'id',
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
																	value:record.data.indexName,
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
															var form=Ext.getCmp("updateIndexForm");
															if (form.isValid()) {
																 form.submit({
								                		                	url:$ctx+'/itemrel/save',
								                		                    success: function(form, action) {
								                		                       Ext.Msg.alert('提示', '修改成功');
								                		                       indexGridStore.load();
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

							    	
							    }},{
							    	icon: $ctx+'/static/images/btn/del.gif',  // Use a URL in the icon config
								    tooltip: '删除',
								    handler:function(grid, rowIndex, colIndex){
								    	var record=indexGridStore.getAt(rowIndex);
								    	$.ajax({
											   url: "itemrel/delete",
											   data: {id:record.data.id},
											   async:false,
											   dataType: 'json',
											   success: function(data){
												   indexGridStore.loadPage(1);
											   }
								    });
							    }
							}
	                        ]
			    }
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:indexGridStore,
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