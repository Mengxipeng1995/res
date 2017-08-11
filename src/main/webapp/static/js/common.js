var mixProductGridStore=Ext.create("Ext.data.Store",{
	autoLoad: false,
	fields:['id','name','createrNickName','status','createDate'],
	proxy : {
		type : 'ajax',
		url : 'product/list?type=3',
		reader : {
			root : 'content',
			totalProperty : 'totalElements'
		},
		simpleSortMode : true
	}
	

});

var subjectCommonGridStore=Ext.create("Ext.data.Store",{
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

//专题查询结果显示
var subjectCommonGridPanel=Ext.create('Ext.panel.Panel',{
    //	title:'专题列表',
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
			store: subjectCommonGridStore,
			id:'subjectCommonGrid',
			selModel:Ext.create('Ext.selection.CheckboxModel'),
			dockedItems:[
							{
									xtype : 'toolbar',
									//hidden:true,
									enableOverflow:true,
									items :[
										{
					                    xtype: 'textfield',
					                    hidden:true,
					                    id:"subjectCommonType"
										},
									        {
										        xtype: 'button',
										        text:'添加到专题',
										        listeners:{
										        	'click':function(){
										        		var subjectRecords=getGridSelectionRecords('subjectCommonGrid');
										        		if (subjectRecords.length != 1){
															Ext.Msg.alert('提示', '请选择一个专题进行添加');
														} else {
															var ids= new Array(),type=Ext.getCmp('subjectCommonType').getValue(),itemRecords;
															if(type=='0'){
																//条目
																itemRecords=getGridSelectionRecords('itemGridPanel');
																$.each(itemRecords,function(i,record){
																	ids.push(record.id);
																});
															}else if(type=='1'){
																//图片
																itemRecords=getGridSelectionRecords('picGrid');
																$.each(itemRecords,function(i,record){
																	ids.push(record.id);
																});
															}else{
																//视频
																itemRecords=getGridSelectionRecords('videoGrid');
																$.each(itemRecords,function(i,record){
																	ids.push(record.id);
																});
															}
										        			
															
															$.ajax({
									     						   url: "subject/addItemToSubject",
									     						   data: {'items':ids,'sid':subjectRecords[0].id,'type':type},
									     						   async:false,
									     						  traditional: true,
									     						   dataType: 'json',
									     						   success: function(msg){
									     							  if(msg.success){
									     								 Ext.Msg.alert('提示', '添加成功');
									     							  }else{
									     								  Ext.Msg.alert('提示',msg.msg);
									     							  }
									     							  
									     							 // userStore.load();
									     						   }
									     						}); 
															
										        		
										        		
										        	}
										        }
										       
										    }}
									        ]
							}
			             ],
			columns : [
				{
				header:"序号",
				xtype : "rownumberer",
				align : 'center',
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
			    }
			]
		}],
		bbar : [
		    {
				xtype : 'pagingtoolbar',
				store:subjectCommonGridStore,
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


var mixProductGridPanel=Ext.create('Ext.panel.Panel',{
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
		id:'mixProductGridPanel',
		autoWidth:true,
		columnLines:true,
		store: mixProductGridStore,
		listeners : {
			'itemdblclick' : function(view,record, item, index, e,eOpts) {
				
			}
		},
		selModel:Ext.create('Ext.selection.CheckboxModel'),
		dockedItems:[
						{
								xtype : 'toolbar',
								//hidden:true,
								enableOverflow:true,
								items :[
								        {
						                    xtype: 'textfield',
						                    hidden:true,
						                    id:"mixProductItemGrid"
						                },
								        {
									        xtype: 'button',
									        text:'添加到产品',
									        listeners:{
									        	'click':function(){
									        		var productRecords=getGridSelectionRecords('mixProductGridPanel');
									        		if (productRecords.length != 1){
														Ext.Msg.alert('提示', '请选择一个产品进行添加');
													} else {
														var ids= new Array(),type;
														var gridName=Ext.getCmp('mixProductItemGrid').getValue();
														if(gridName=='itemGridPanel'){
										        			type=0;
										        			var itemRecords=getGridSelectionRecords(gridName);
										        			$.each(itemRecords,function(i,record){
																ids.push(record.data.itemid);
															});
										        		}else if(gridName=='picGrid'){
										        			type=1;
										        			var itemRecords=getGridSelectionRecords(gridName);
										        			$.each(itemRecords,function(i,record){
																ids.push(record.data.photoid);
															});
										        		}else if(gridName=='videoGrid'){
										        			type=2;
										        			var itemRecords=getGridSelectionRecords(gridName);
										        			$.each(itemRecords,function(i,record){
																ids.push(record.data.id);
															});
										        		}
										        		
														
														$.ajax({
								     						   url: "product/addItemToProduct",
								     						   data: {'items':ids,'pid':productRecords[0].id,'type':type},
								     						   async:false,
								     						  traditional: true,
								     						   dataType: 'json',
								     						   success: function(msg){
								     							  if(msg.success){
								     								 Ext.Msg.alert('提示', '添加成功');
								     							  }else{
								     								  Ext.Msg.alert('提示',msg.msg);
								     							  }
								     							  
								     							 // userStore.load();
								     						   }
								     						}); 
														
									        		
									        		
									        	}
									        }
									       
									    }}
								        ]
						}
		             ],
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
		    }
		]
	}],
	bbar : [
	    {
			xtype : 'pagingtoolbar',
			store:mixProductGridStore,
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


var createItemProductPanel=Ext.create('Ext.panel.Panel',{
   	width:'85%',
   	autoWidth:true,
   	region : 'center',
   	items: [
	{
	xtype:'form',
	width:350,
	id:"createItemProductForm",
	style:'margin:auto',
	buttonAlign: 'center',
//	/margin  : '15 0 0 0',
	items:[
		{ 
			xtype:'fieldset',
			title:'新增产品信息', 
			collapsible:false,
			width : 350,
			items:[
				{	
					xtype: "textfield",
					fieldLabel:'产品名称<font color="red">*</font>',
					id:'createItemProductName',
					name:'name',
					allowBlank: false,
					blankText: '分类名称为必填项'
				},
				{
					xtype: "textfield",
					fieldLabel:'分类Id<font color="red">*</font>',
					id:'createItemProductCatId',
					hidden:true,
					name:'catid',
					blankText: '分类Id为必填项'
					
				},
				{
					xtype: "textfield",
					fieldLabel:'分类名称<font color="red">*</font>',
					id:'createItemProductCatName',
					readOnly:true
					
				},
				{
					xtype: "textfield",
					hidden:true,
					id:'createItemProductType',
					value:'1',
					name:'type'
					
				}
				
				
		        
				
			]
		}
	],
	buttons:[
		{
			text: '保存',
			listeners:{
				click:function(){
					var form=Ext.getCmp("createItemProductForm");
					if (form.isValid()) {
						 form.submit({
        		                	url:$ctx+'/product/savePubProduct',
        		                    success: function(form, action) {
        		                    if(Ext.getCmp('createItemProductType').getValue(3)){
        		                    	productGridStore.loadPage(1);
        		                    }
        		                    createItemProductPanel.findParentByType("window").close();
        		                       Ext.Msg.alert('提示', '修改成功');
        		                      
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
					createItemProductPanel.findParentByType("window").close();
				}
			}
			
		}
	]

}]});
