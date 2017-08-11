var keywordGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','keywords','createDate','createUser','keyWordsType','typeName','modifyDate'],
	pageSize : 20,
	proxy : {
		type : 'ajax',
		url : 'keyword/search',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
});

//词条
var keywordGridPanel=Ext.create('Ext.panel.Panel',{
    	title:'关键词列表',
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
 			items :[{
		        xtype: 'button',
		        text:'新增',
		        listeners:{
		        	'click':function(){
		        		var addKeyWordWindow=Ext.create("Ext.window.Window",{
                    		autoWidth : true,
                    		modal:true,
                    		title:'新增关键词',
                    		resizable:false,
                    		width: 400,
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
											id:'addKeyWordForm',
											width:'100%',
											style:'border-width:0 0 0 0;margin:auto',
											autoScroll : true,
											buttonAlign: 'center',
											items:[
												{
													xtype:'fieldset', 
													title:'关键词基本属性', 
													collapsible:false,
													width : '100%',
													items:[
														{	
															xtype: "textfield",
															fieldLabel:'<span style="color:red;    vertical-align: sub;"> * </span>关键词',
															labelAlign:'right',
															labelWidth:66,
															width:'100%',
															name:'keyword',
															allowBlank : false
														}
														
													
													]
												}
											
											],
											buttons:[{
											    
												text: '保存',
												handler:function(){
													var form=Ext.getCmp("addKeyWordForm");
													if (form.isValid()) {
														 form.submit({
						                		                	url:$ctx+'/keyword/save',
						                		                    success: function(form, action) {
						                		                       Ext.Msg.alert('提示', '修改成功');
						                		                       keywordGridStore.load();
						                		                       addKeyWordWindow.close();
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
																
																addKeyWordWindow.close();
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
		       
		    }]
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
			store: keywordGridStore,
			selModel:Ext.create('Ext.selection.CheckboxModel'), 
			columns : [
				{
					header:"ID",
					dataIndex:"id",
					align : 'center',
					width : '5%'
				},
				{
					header:"关键词",
					dataIndex:"keywords",
					align : 'center',
					width : '37%'
				},
			    {
					header:"类型",
					dataIndex:"typeName",
					align : 'center',
					width : '15%'
			    },
			    {
					header:"创建者",
					dataIndex:"createUser",
					align : 'center',
					width : '24%'
			    },
				{
					header:"创建时间",
					dataIndex:"createDate",
					align : 'center'
				},{

	                xtype:'actioncolumn',
	                header:"操作",
	                align : 'center',
	                width:'8%',
	                items: [
							{
							    	icon: $ctx+'/static/images/btn/del.gif',  // Use a URL in the icon config
								    tooltip: '删除',
								    handler:function(grid, rowIndex, colIndex){
								    	var record=keywordGridStore.getAt(rowIndex);
								    	$.ajax({
											   url: "keyword/delete",
											   data: {id:record.data.id},
											   async:false,
											   dataType: 'json',
											   success: function(data){
												   keywordGridStore.loadPage(1);
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
				store:keywordGridStore,
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